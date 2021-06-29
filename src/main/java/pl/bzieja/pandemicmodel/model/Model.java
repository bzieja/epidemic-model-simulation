package pl.bzieja.pandemicmodel.model;

import org.springframework.stereotype.Component;
import pl.bzieja.pandemicmodel.model.cell.Building;
import pl.bzieja.pandemicmodel.model.cell.Cell;
import pl.bzieja.pandemicmodel.model.person.HealthState;
import pl.bzieja.pandemicmodel.model.person.Person;

import java.awt.Color;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Model {

    private Cell[][] map;
    private List<Person> workers = new ArrayList<>();

    public int[] getRandomCellCoordinateByColor(Color color) {
       List<Cell> list = Arrays.stream(map).flatMap(Stream::of).filter(c -> c.getDefaultColor().equals(color)).collect(Collectors.toList());
       return list.get(new Random().nextInt(list.size())).getCoordinates();
    }

    public List<Cell> getAllCellsCoordinatesByColor(Color color) {
        return Arrays.stream(map).flatMap(Stream::of).filter(c -> c.getDefaultColor().equals(color)).collect(Collectors.toList());
    }

    public boolean isCellWalkable(int x, int y) {
        return map[x][y].isWalkable();
    }

    public int getMapVerticalDimension() {
        return map.length;
    }

    public int getMapHorizontalDimension() {
        return map[0].length;
    }

    public void setMap(Cell[][] map) {
        this.map = map;
    }

    public void setWorkers(List<Person> workers) {
        this.workers.addAll(workers);
    }

    public void moveWorkers() {
        workers.stream().filter(w -> w.getRouteMap() != null).forEach(Person::makeMove);
    }

    public synchronized Cell getCellByCoordinates(int x, int y) {
        return map[x][y];
    }

    public void sendPartOfWorkersToWorkFromHome() {
        long totalNumberOfWorkersWhichShouldGoToWork = workers.stream().filter(w -> HealthState.workable.contains(w.getHealthState())).count();
        long numberOfPeopleWhichGoesToWorkInThisTour = totalNumberOfWorkersWhichShouldGoToWork / AppConfig.NUMBER_OF_GROUPS_GOING_TO_WORK;

        List<Person> persons = workers.stream()
                .filter(w -> HealthState.workable.contains(w.getHealthState()) && w.getCurrentDestinationBuilding() != w.getWorkplace())
                .collect(Collectors.toList());
        Collections.shuffle(persons);

        persons.stream()
                .limit(numberOfPeopleWhichGoesToWorkInThisTour)
                .forEach(p -> {
                    p.setDestinationCells(getAllCellsCoordinatesByColor(p.getWorkplace().getColor()));
                    p.setCurrentDestinationBuilding(p.getWorkplace());
                    p.setRouteMap(p.getWorkplace().getRouteMap());
                });
    }

    public void sendPartOfWorkersForDinner() {
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
                    p.setDestinationCells(getAllCellsCoordinatesByColor(building.getColor()));
                    p.setCurrentDestinationBuilding(building);
                    p.setRouteMap(building.getRouteMap());
                });

    }

    public void workersToLunch() {

        workers.forEach(p -> {
            Building building = new ArrayList<>(Building.gastronomy).get(new Random().nextInt(Building.gastronomy.size()));
            p.setDestinationCells(getAllCellsCoordinatesByColor(building.getColor()));
            p.setCurrentDestinationBuilding(building);
            p.setRouteMap(building.getRouteMap());
        });
    }

    public void setSpawnAsADestinationPointForEachWorker() {
        workers.forEach(p -> {
            p.setDestinationCells(getAllCellsCoordinatesByColor(Building.SPAWN.getColor()));
            p.setRouteMap(Building.SPAWN.getRouteMap());
        });
    }

    public void workersGoAroundBuildingIfAreAtDestinationPoint() {
        workers.stream().filter(Person::isAtTheDestinationPoint).forEach(p -> {
            p.setRouteMap(Building.BUILDING_INTERIOR.getRouteMap());
        });
    }

    public  Color getCellColor(int x, int y){

        //check if someone from workers is on that cell
        long passerbyCounter = workers.stream().filter(p -> p.getX() == x && p.getY() == y).count();

        if (map[x][y].getDefaultColor().equals(Building.SPAWN.getColor())) {
            return Building.PATH.getColor();
        } else if (passerbyCounter == 1) {
            return Building.WORKER.getColor();
        } else if (passerbyCounter > 1) {
            return Building.CROWD.getColor();
        } else {
            return map[x][y].getDefaultColor();
        }
    }

    public List<Person> getWorkers() {
        return workers;
    }

}
