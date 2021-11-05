package pl.bzieja.pandemicmodel.model.person;

import javafx.beans.property.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bzieja.pandemicmodel.model.AppConfig;
import pl.bzieja.pandemicmodel.model.Model;
import pl.bzieja.pandemicmodel.model.cell.Building;
import pl.bzieja.pandemicmodel.model.cell.Cell;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Bartłomiej Zieja
 */
@Component
public class InfectionManager {
    private final Logger logger = LoggerFactory.getLogger(InfectionManager.class);

    Model model;
    IntegerProperty numberOfHealthWorkers = new SimpleIntegerProperty(0);
    IntegerProperty numberOfSymptomaticallyIll = new SimpleIntegerProperty(0);
    IntegerProperty numberOfAsymptomaticallyIll = new SimpleIntegerProperty(0);
    IntegerProperty numberOfQuarantined = new SimpleIntegerProperty(0);
    IntegerProperty numberOfConvalescent = new SimpleIntegerProperty(0);


    @Autowired
    public InfectionManager(Model model) {
        this.model = model;
    }

    public void doTickRoutine() {
        countInfectedNeighboursForWorkers();
        updateProbabilityOfBeingInfectedForWorkers();
        infect();
    }


    public void doEndOfDayInfectionSummary() {
        logger.info("End of day infection manager routine");
        putInQuarantaine();
        checkHealing();
    }

    /**
     * Increase number of days in special state for every not healthy person by one.
     * If person days in special state is equal to the special state days, then this person health state should be transformed.
     * Allowed health state transformations in this method:
     * "NOT_HEALTHY" -> "CONVALESCENT"
     * "CONVALESCENT" -> "HEALTHY" (can be infected again)
     */
    private void checkHealing() {
        model.getWorkers().stream().filter(p -> !HealthState.HEALTHY.equals(p.getHealthState()))
                .forEach(person -> {
                    //if end of special state for person
                    if (person.getInteractionState().getDaysInSpecialState() == person.getHealthState().getDurationTimeOfSpecialStateInDays()) {
                        //end of immunity period
                        if (person.getHealthState().equals(HealthState.CONVALESCENT)) {
                            transitionFromToProperty(person.getHealthState(), HealthState.HEALTHY);
                            person.setHealthState(HealthState.HEALTHY);
                        } else {    //end of disease
                            transitionFromToProperty(person.getHealthState(), HealthState.CONVALESCENT);
                            person.setHealthState(HealthState.CONVALESCENT);
                        }
                    }
                    person.getInteractionState().increaseNumberOfDaysInSpecialState();
                });

    }

    private void putInQuarantaine() {
        model.getWorkers().stream().filter(p -> p.getHealthState().equals(HealthState.SYMPTOMATICALLY_ILL)).forEach(
                person -> {
                    transitionFromToProperty(person.getHealthState(), HealthState.QUARANTINED);
                    person.setHealthState(HealthState.QUARANTINED);
                });
    }

    private void countInfectedNeighboursForWorkers() {
        model.getWorkers().stream().filter(w -> !HealthState.stayAHome.contains(w.getHealthState())
                && HealthState.canBeInfected.contains(w.getHealthState()))
                .forEach(w -> {

                    long infectedNeighbours = model.getWorkers().stream()
                            .filter(w1 -> HealthState.canInfect.contains(w1.getHealthState())
                                    && isInRange(w, w1)).count();

                    w.getInteractionState().setInfectedPersonsInRange((int) infectedNeighbours);
                });
    }

    private boolean isInRange(Person person, Person anotherPerson) {
        if (Building.buildings.contains(new Cell(person.getX(), person.getY(), false, null))) {
            return person.calculateDistanceToAnotherPerson(anotherPerson) <= AppConfig.INFECTION_RADIUS_IN_THE_BUILDING;
        } else {
            return person.calculateDistanceToAnotherPerson(anotherPerson) <= AppConfig.INFECTION_RADIUS_OUTSIDE;
        }
    }

    private void updateProbabilityOfBeingInfectedForWorkers() {
        model.getWorkers().stream().filter(w -> HealthState.canBeInfected.contains(w.getHealthState()))
                .forEach(w -> {
                    int infectedPersonsInRange = w.getInteractionState().getInfectedPersonsInRange();
                    if (infectedPersonsInRange == 0) {
                        w.getInteractionState().setProbabilityOfBeingInfected(0);
                    } else if (Building.SPAWN.getCellsWhichBelongsToGivenBuilding().contains(new Cell(w.getX(), w.getY(), false, null))) {  //in the building
                        w.getInteractionState().setProbabilityOfBeingInfected(
                                w.getInteractionState().getProbabilityOfBeingInfected() + infectedPersonsInRange * AppConfig.PROBABILITY_OF_BEING_INFECTED_BY_ONE_PERSON_IN_THE_BUILDING
                        );
                    } else {    //outside
                        w.getInteractionState().setProbabilityOfBeingInfected(
                                w.getInteractionState().getProbabilityOfBeingInfected() + infectedPersonsInRange * AppConfig.PROBABILITY_OF_BEING_INFECTED_BY_ONE_PERSON_OUTSIDE
                        );
                    }
                });
    }

    private void infect() {
        model.getWorkers().stream().filter(w -> HealthState.canBeInfected.contains(w.getHealthState())
        && !Building.SPAWN.getCellsWhichBelongsToGivenBuilding().contains(new Cell(w.getX(), w.getY(), false, null)))
                .forEach(w -> {
                    double rand = new Random().nextInt(100000) * 0.001;
                    if (w.getInteractionState().getProbabilityOfBeingInfected() >= rand) {  //GETS INFECTED

                        if (AppConfig.IS_VACCINATION) {
                            double willTheVaccineProtectRand = new Random().nextInt(100000) * 0.001;
                            if (AppConfig.VACCINE_EFFECTIVENESS >= willTheVaccineProtectRand) { //vaccine protect
                                return;
                            }
                        }

                        int randIfAsymptomatically = new Random().nextInt(100);
                        logger.debug("Current rand {}", randIfAsymptomatically);
                        if (AppConfig.PROBABILITY_OF_BEING_ASYMPTOMATICALLY_INFECTED > randIfAsymptomatically) { //ASYMPTOMATICALLY
                            transitionFromToProperty(w.getHealthState(), HealthState.ASYMPTOMATICALLY_ILL);
                            w.setHealthState(HealthState.ASYMPTOMATICALLY_ILL);
                        } else {
                            transitionFromToProperty(w.getHealthState(), HealthState.SYMPTOMATICALLY_ILL); //SYMPTOMATICALLY
                            w.setHealthState(HealthState.SYMPTOMATICALLY_ILL);
                        }

                    }

                });
    }

    public void infectPatientsZero() {
        numberOfHealthWorkers.setValue((int) model.getWorkers().stream().filter(w -> w.getHealthState().equals(HealthState.HEALTHY)).count());

        List<Person> persons = model.getWorkers().stream()
                .filter(w -> HealthState.canBeInfected.contains(w.getHealthState()))
                .collect(Collectors.toList());

        Collections.shuffle(persons);

        persons.stream().limit(AppConfig.NUMBER_OF_INITIALLY_INFECTED_WORKERS)
                .forEach(w -> {
                    transitionFromToProperty(w.getHealthState(), HealthState.SYMPTOMATICALLY_ILL);
                    w.setHealthState(HealthState.SYMPTOMATICALLY_ILL);
                });

    }

    public void writeDataToCSV(String day, LocalTime lt) {
        //healthy, symptomatically ill, aymptomatically ill, numberOfQuarantined, numberOfConvalescent
        logger.info("Logging to file, day: {}, local time: {}", day, lt);
        try(PrintWriter writer = new PrintWriter(new FileWriter("src/main/resources/ " + AppConfig.FILE_WITH_RESULT_NAME + " .txt", true))) {
            writer.write("day: " + day + " time: " + lt + ";"
                    + numberOfHealthWorkers.getValue() + ";"
                    + numberOfSymptomaticallyIll.getValue() + ";"
                    + numberOfAsymptomaticallyIll.getValue() + ";"
                    + numberOfQuarantined.getValue() + ";"
                    + numberOfConvalescent.getValue() + ";\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNumberOfHealthWorkers() {
        return numberOfHealthWorkers.get();
    }

    public IntegerProperty numberOfHealthWorkersProperty() {
        return numberOfHealthWorkers;
    }

    public int getNumberOfSymptomaticallyIll() {
        return numberOfSymptomaticallyIll.get();
    }

    public IntegerProperty numberOfSymptomaticallyIllProperty() {
        return numberOfSymptomaticallyIll;
    }

    public int getNumberOfAsymptomaticallyIll() {
        return numberOfAsymptomaticallyIll.get();
    }

    public IntegerProperty numberOfAsymptomaticallyIllProperty() {
        return numberOfAsymptomaticallyIll;
    }

    public int getNumberOfQuarantined() {
        return numberOfQuarantined.get();
    }

    public IntegerProperty numberOfQuarantinedProperty() {
        return numberOfQuarantined;
    }

    public int getNumberOfConvalescent() {
        return numberOfConvalescent.get();
    }

    public IntegerProperty numberOfConvalescentProperty() {
        return numberOfConvalescent;
    }

    private void transitionFromToProperty(HealthState from, HealthState to) {
        decreaseProperty(from);
        increaseProperty(to);
    }

    private void increaseProperty(HealthState healthState) {
        switch (healthState) {
            case HEALTHY: numberOfHealthWorkers.setValue(numberOfHealthWorkers.getValue() + 1);
            break;
            case QUARANTINED: numberOfQuarantined.setValue(numberOfQuarantined.getValue() + 1);
            break;
            case CONVALESCENT: numberOfConvalescent.setValue(numberOfConvalescent.getValue() + 1);
            break;
            case SYMPTOMATICALLY_ILL: numberOfSymptomaticallyIll.setValue(numberOfSymptomaticallyIll.getValue() + 1);
            break;
            case ASYMPTOMATICALLY_ILL: numberOfAsymptomaticallyIll.setValue(numberOfAsymptomaticallyIll.getValue() + 1);
            break;
            default:
                logger.info("Unknown Health State when increasing: {}", healthState);
        }
    }

    private void decreaseProperty(HealthState healthState) {
        switch (healthState) {
            case HEALTHY: numberOfHealthWorkers.setValue(numberOfHealthWorkers.getValue() - 1);
                break;
            case QUARANTINED: numberOfQuarantined.setValue(numberOfQuarantined.getValue() - 1);
                break;
            case CONVALESCENT: numberOfConvalescent.setValue(numberOfConvalescent.getValue() - 1);
                break;
            case SYMPTOMATICALLY_ILL: numberOfSymptomaticallyIll.setValue(numberOfSymptomaticallyIll.getValue() - 1);
                break;
            case ASYMPTOMATICALLY_ILL: numberOfAsymptomaticallyIll.setValue(numberOfAsymptomaticallyIll.getValue() - 1);
                break;
            default:
                logger.info("Unknown Health State when decreasing: {}", healthState);
        }
    }


}
