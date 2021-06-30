package pl.bzieja.pandemicmodel.model.events;

import pl.bzieja.pandemicmodel.model.AppConfig;
import pl.bzieja.pandemicmodel.model.Model;
import pl.bzieja.pandemicmodel.model.cell.Building;
import pl.bzieja.pandemicmodel.model.person.HealthState;
import pl.bzieja.pandemicmodel.model.person.Person;

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
                    //p.setDestinationCells(model.getAllCellsCoordinatesByColor(Building.SPAWN.getColor()));  //???
                    p.setDestinationCells(model.getAllCellsCoordinatesByColor(Building.SPAWN.getColor()));  //???
                    p.setCurrentDestinationBuilding(Building.SPAWN);
                    p.setRouteMap(Building.SPAWN.getRouteMap());
                });
    }
}
