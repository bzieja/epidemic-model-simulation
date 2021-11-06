package pl.bzieja.pandemicmodel.core.events;

import pl.bzieja.pandemicmodel.core.AppConfig;
import pl.bzieja.pandemicmodel.core.model.Model;
import pl.bzieja.pandemicmodel.core.model.Building;
import pl.bzieja.pandemicmodel.core.person.HealthState;
import pl.bzieja.pandemicmodel.core.person.Person;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bartłomiej Zieja
 */
public class DinnerEndsEvent extends Event {

    Model model;
    List<Person> workers;

    public DinnerEndsEvent(Model model) {
        this.model = model;
        this.workers = model.getWorkers();
    }

    @Override
    public synchronized void start() {
        long totalNumberOfWorkersWhichShouldGoBackForDinner = workers.stream().filter(w -> HealthState.workable.contains(w.getHealthState())).count() * AppConfig.PERCENTAGE_OF_PEOPLE_GOING_TO_THE_DINNER / 100;
        long numberOfPeopleWhichGoesForDinnerInThisTour = totalNumberOfWorkersWhichShouldGoBackForDinner / AppConfig.NUMBER_OF_GROUPS_GOING_TO_DINNER;

        List<Person> persons = workers.stream()
                .filter(w -> HealthState.workable.contains(w.getHealthState())
                        && Building.gastronomy.contains(w.getCurrentDestinationBuilding())
                        && !Building.gastronomy.contains(w.getWorkplace()))
                .collect(Collectors.toList());
        Collections.shuffle(persons);

        persons.stream()
                .limit(numberOfPeopleWhichGoesForDinnerInThisTour)
                .forEach(p -> {
                    p.setDestinationCells(model.getAllCellsCoordinatesByColor(p.getWorkplace().getColor()));
                    p.setCurrentDestinationBuilding(p.getWorkplace());
                    p.setRouteMap(p.getWorkplace().getRouteMap());
                });
    }
}
