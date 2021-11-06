package pl.bzieja.pandemicmodel.core.events;

import pl.bzieja.pandemicmodel.core.AppConfig;
import pl.bzieja.pandemicmodel.core.model.Model;
import pl.bzieja.pandemicmodel.core.model.Building;
import pl.bzieja.pandemicmodel.core.person.HealthState;
import pl.bzieja.pandemicmodel.core.person.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Bartłomiej Zieja
 */
public class DinnerStartEvent extends Event {

    Model model;
    List<Person> workers;
    public DinnerStartEvent(Model model) {
        this.model = model;
        this.workers = model.getWorkers();
    }

    @Override
    public void start() {
        long totalNumberOfWorkersWhichShouldGoForDinner = workers.stream().filter(w -> HealthState.workable.contains(w.getHealthState())).count() * AppConfig.PERCENTAGE_OF_PEOPLE_GOING_TO_THE_DINNER / 100;
        long numberOfPeopleWhichGoesForDinnerInThisTour = totalNumberOfWorkersWhichShouldGoForDinner / AppConfig.NUMBER_OF_GROUPS_GOING_TO_DINNER;

        List<Person> persons = workers.stream()
                .filter(w -> HealthState.workable.contains(w.getHealthState()) && !Building.gastronomy.contains(w.getCurrentDestinationBuilding()))
                .collect(Collectors.toList());
        Collections.shuffle(persons);

        persons.stream()
                .limit(numberOfPeopleWhichGoesForDinnerInThisTour)
                .forEach(p -> {
                    Building building = new ArrayList<>(Building.gastronomy).get(new Random().nextInt(Building.gastronomy.size()));
                    p.setDestinationCells(model.getAllCellsCoordinatesByColor(building.getColor()));
                    p.setCurrentDestinationBuilding(building);
                    p.setRouteMap(building.getRouteMap());
                });
    }
}
