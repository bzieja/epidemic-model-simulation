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
public class TheEndOfTheDayEvent extends Event {

    Model model;
    List<Person> workers;

    public TheEndOfTheDayEvent(Model model) {
        this.model = model;
        this.workers = model.getWorkers();
    }

    @Override
    public void start() {
        long totalNumberOfWorkersWhichShouldGoBackFromWork = workers.stream().filter(w -> HealthState.workable.contains(w.getHealthState())).count();
        long numberOfPeopleWhichGoesBackFromWorkInThisTour = totalNumberOfWorkersWhichShouldGoBackFromWork / AppConfig.NUMBER_OF_GROUPS_GOING_BACK_HOME;

        List<Person> persons = workers.stream()
                .filter(w -> HealthState.workable.contains(w.getHealthState()) && w.getCurrentDestinationBuilding() != Building.SPAWN)
                .collect(Collectors.toList());
        Collections.shuffle(persons);

        persons.stream()
                .limit(numberOfPeopleWhichGoesBackFromWorkInThisTour)
                .forEach(p -> {
                    p.setDestinationCells(model.getAllCellsCoordinatesByColor(Building.SPAWN.getColor()));
                    p.setCurrentDestinationBuilding(Building.SPAWN);
                    p.setRouteMap(Building.SPAWN.getRouteMap());
                });
    }
}
