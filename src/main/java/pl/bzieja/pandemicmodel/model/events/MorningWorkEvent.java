package pl.bzieja.pandemicmodel.model.events;

import pl.bzieja.pandemicmodel.model.AppConfig;
import pl.bzieja.pandemicmodel.model.Model;
import pl.bzieja.pandemicmodel.model.person.HealthState;
import pl.bzieja.pandemicmodel.model.person.Person;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bartłomiej Zieja
 */
public class MorningWorkEvent extends Event {

    Model model;
    List<Person> workers;

    public MorningWorkEvent(Model model) {
        this.model = model;
        this.workers = model.getWorkers();
    }

    @Override
    public synchronized void start() {
        long totalNumberOfWorkersWhichShouldGoToWork = workers.stream().filter(w -> HealthState.workable.contains(w.getHealthState())).count();
        long numberOfPeopleWhichGoesToWorkInThisTour = totalNumberOfWorkersWhichShouldGoToWork / AppConfig.NUMBER_OF_GROUPS_GOING_TO_WORK;

        List<Person> persons = workers.stream()
                .filter(w -> HealthState.workable.contains(w.getHealthState())) //&& w.getCurrentDestinationBuilding() != w.getWorkplace()
                .collect(Collectors.toList());
        Collections.shuffle(persons);

        persons.stream()
                .limit(numberOfPeopleWhichGoesToWorkInThisTour)
                .forEach(p -> {
                    p.setDestinationCells(model.getAllCellsCoordinatesByColor(p.getWorkplace().getColor()));
                    p.setCurrentDestinationBuilding(p.getWorkplace());
                    p.setRouteMap(p.getWorkplace().getRouteMap());
                });
    }
}
