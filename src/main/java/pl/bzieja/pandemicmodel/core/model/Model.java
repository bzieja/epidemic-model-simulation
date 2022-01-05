package pl.bzieja.pandemicmodel.core.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.bzieja.pandemicmodel.core.AppConfig;
import pl.bzieja.pandemicmodel.core.cell.Cell;
import pl.bzieja.pandemicmodel.core.person.HealthState;
import pl.bzieja.pandemicmodel.core.person.Person;

import java.awt.Color;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Model {

    private final Logger logger = LoggerFactory.getLogger(Model.class);
    private Cell[][] worldMap;
    private List<Person> workers = new ArrayList<>();

    public int[] getRandomCellCoordinateByColor(Color color) {
       List<Cell> list = Arrays.stream(worldMap).flatMap(Stream::of).filter(c -> c.getDefaultColor().equals(color)).collect(Collectors.toList());
       return list.get(new Random().nextInt(list.size())).getCoordinates();
    }

    public List<Cell> getAllCellsCoordinatesByColor(Color color) {
        return Arrays.stream(worldMap).flatMap(Stream::of).filter(c -> c.getDefaultColor().equals(color)).collect(Collectors.toList());
    }

    public boolean isCellWalkable(int x, int y) {
        return worldMap[x][y].isWalkable();
    }

    public int getMapVerticalDimension() {
        return worldMap.length;
    }

    public int getMapHorizontalDimension() {
        return worldMap[0].length;
    }

    public void setWorldMap(Cell[][] worldMap) {
        this.worldMap = worldMap;
    }

    public void setWorkers(List<Person> workers) {
        logger.info("Adding workers to model");
        this.workers.addAll(workers);
    }

    public void moveWorkers() {
        logger.debug("Moving workers");
        workers.stream().filter(w -> w.getRouteMap() != null).forEach(Person::makeMove);
    }

    public synchronized Cell getCellByCoordinates(int x, int y) {
        return worldMap[x][y];
    }

    public void sendPartOfWorkersToWorkFromHome() {
        logger.info("Send part of workers to work method");

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

    public void workersToLunch() {
        logger.info("Workers to lunch method");

        workers.forEach(p -> {
            Building building = new ArrayList<>(Building.gastronomy).get(new Random().nextInt(Building.gastronomy.size()));
            p.setDestinationCells(getAllCellsCoordinatesByColor(building.getColor()));
            p.setCurrentDestinationBuilding(building);
            p.setRouteMap(building.getRouteMap());
        });
    }

    public void setSpawnAsADestinationPointForEachWorker() {
        logger.info("Set spawn as a destination point for each worker method");

        workers.forEach(p -> {
            p.setDestinationCells(getAllCellsCoordinatesByColor(Building.SPAWN.getColor()));
            p.setRouteMap(Building.SPAWN.getRouteMap());
        });
    }

    public void workersGoAroundBuildingIfAreAtDestinationPoint() {
        logger.debug("Workers go around buildings method");
        workers.stream().filter(Person::isAtTheDestinationPoint).forEach(p -> {
            p.setRouteMap(Building.BUILDING_INTERIOR.getRouteMap());
        });

//        workers.stream().filter(p -> p.isAtTheDestinationPoint() && p.getCurrentDestinationBuilding() != Building.SPAWN).forEach(p -> {
//            p.setRouteMap(Building.BUILDING_INTERIOR.getRouteMap());
//        });
    }

    public  Color getCellColor(int x, int y){

        //check if someone from workers is on that cell
        List<Person> persons = workers.stream().filter(p -> p.getX() == x && p.getY() == y).collect(Collectors.toList());
        long passerbyCounter = persons.size();

        if (worldMap[x][y].getDefaultColor().equals(Building.SPAWN.getColor())) {
            return Building.PATH.getColor();
        } else if (passerbyCounter == 1) {
            //health color logic
            HealthState healthState = persons.get(0).getHealthState();
            if (healthState.equals(HealthState.SYMPTOMATICALLY_ILL)) {
                return HealthState.SYMPTOMATICALLY_ILL.getColor();
            } else if (healthState.equals(HealthState.ASYMPTOMATICALLY_ILL)) {
                return HealthState.ASYMPTOMATICALLY_ILL.getColor();
            } else if (healthState.equals(HealthState.QUARANTINED)) {
                return HealthState.QUARANTINED.getColor();
            } else if (healthState.equals(HealthState.CONVALESCENT)) {
                return HealthState.CONVALESCENT.getColor();
            } else {
                return HealthState.HEALTHY.getColor();
            }
        } else if (passerbyCounter > 1) {
            return Building.CROWD.getColor();
        } else {
            return worldMap[x][y].getDefaultColor();
        }
    }

    public List<Person> getWorkers() {
        return workers;
    }

}
