package pl.bzieja.pandemicmodel.model.person;

import javafx.beans.property.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bzieja.pandemicmodel.model.AppConfig;
import pl.bzieja.pandemicmodel.model.Model;
import pl.bzieja.pandemicmodel.model.cell.Building;
import pl.bzieja.pandemicmodel.model.cell.Cell;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Bartłomiej Zieja
 */
@Component
public class InfectionManager {
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

    public void turnRoutine() {
        countInfectedNeighboursForWorkers();
        updateProbabilityOfBeingInfectedForWorkers();
        infect();
    }

    private void countInfectedNeighboursForWorkers() {
        model.getWorkers().stream().filter(w -> !HealthState.stayAHome.contains(w.getHealthState())
                && HealthState.canBeInfected.contains(w.getHealthState()))
                .forEach(w -> {

                    long infectedNeighbours = model.getWorkers().stream()
                            .filter(w1 -> HealthState.canInfect.contains(w1.getHealthState())
                                    && w.calculateDistanceToAnotherPerson(w1) <= AppConfig.INFECTION_RADIUS).count();

                    w.getInteractionState().setInfectedPersonsInRange((int) infectedNeighbours);
                });
    }

    private void updateProbabilityOfBeingInfectedForWorkers() {
        model.getWorkers().stream().filter(w -> HealthState.canBeInfected.contains(w.getHealthState()))
                .forEach(w -> {
                    int infectedPersonsInRange = w.getInteractionState().getInfectedPersonsInRange();
                    if (infectedPersonsInRange == 0) {
                        w.getInteractionState().setProbabilityOfBeingInfected(0);
                    } else {
                        w.getInteractionState().setProbabilityOfBeingInfected(
                                w.getInteractionState().getProbabilityOfBeingInfected() + infectedPersonsInRange * AppConfig.PROBABILITY_OF_BEING_INFECTED_BY_ONE_PERSON
                        );
                    }
                });
    }

    private void infect() {
        model.getWorkers().stream().filter(w -> HealthState.canBeInfected.contains(w.getHealthState())
        && !Building.SPAWN.getCellsWhichBelongsToGivenBuilding().contains(new Cell(w.getX(), w.getY(), false, null)))
                .forEach(w -> {
                    double rand = new Random().nextInt(10000) * 0.01;
                    if (w.getInteractionState().getProbabilityOfBeingInfected() >= rand) {
                        if (rand % 3 == 0) {
                            w.setHealthState(HealthState.ASYMPTOMATICALLY_ILL);
                            numberOfAsymptomaticallyIll.setValue(numberOfAsymptomaticallyIll.getValue() + 1);
                            numberOfHealthWorkers.setValue(numberOfHealthWorkers.getValue() - 1);
                        } else {
                            w.setHealthState(HealthState.SYMPTOMATICALLY_ILL);
                            numberOfSymptomaticallyIll.setValue(numberOfSymptomaticallyIll.getValue() + 1);
                            numberOfHealthWorkers.setValue(numberOfHealthWorkers.getValue() - 1);
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
                    w.setHealthState(HealthState.SYMPTOMATICALLY_ILL);
                    numberOfSymptomaticallyIll.setValue(numberOfSymptomaticallyIll.getValue() + 1);
                    numberOfHealthWorkers.setValue(numberOfHealthWorkers.getValue() - 1);
                });

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
}
