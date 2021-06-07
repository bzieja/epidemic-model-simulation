package pl.bzieja.pandemicmodel.model;

import org.springframework.stereotype.Component;
import pl.bzieja.pandemicmodel.model.cell.Building;
import pl.bzieja.pandemicmodel.model.cell.Cell;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Model {

    private Cell[][] map;
    private List<Person> workers;

    public boolean isCellWalkable(int x, int y) {
        return map[x][y].isWalkable();
    }

//    public Color getCellColor(int x, int y) {
//
//        //check if someone from workers is on that cell
//        long passerbyCounter = workers.stream().filter(p -> p.getX() == x && p.getY() == y).count();
//
//        if (map[x][y].getDefaultColor().equals(Building.SPAWN.getColor())) {
//            return Building.PATH.getColor();
//        } else if (passerbyCounter == 1) {
//            return Building.WORKER.getColor();
//        } else if (passerbyCounter > 1) {
//            return Building.CROWD.getColor();
//        } else {
//            return map[x][y].getDefaultColor();
//        }
//    }


    public int getMapVerticalDimension() {
        return map.length;
    }

    public int getMapHorizontalDimension() {
        return map[0].length;
    }

    public int[] getRandomCellCoordinateByColor(Color color) {
       List<Cell> list = Arrays.stream(map).flatMap(Stream::of).filter(c -> c.getDefaultColor().equals(color)).collect(Collectors.toList());
       return list.get(new Random().nextInt(list.size())).getCoordinates();
    }

    public List<Cell> getAllCellsCoordinatesByColor(Color color) {
        return Arrays.stream(map).flatMap(Stream::of).filter(c -> c.getDefaultColor().equals(color)).collect(Collectors.toList());
    }

    public void setMap(Cell[][] map) {
        this.map = map;
    }

    public void setWorkers(List<Person> workers) {
        this.workers = workers;
    }

    //it will be uneccessary later, just for demonstraiting main animating loop now
    public boolean areAllWorkersAtTheirDestinationPoints() {
        return workers.stream().allMatch(Person::isAtTheDestinationPoint);
    }

    public void moveWorkers() {
        workers.forEach(Person::makeMove);
    }

    public synchronized Cell getCellByCoordinates(int x, int y) {
        return map[x][y];
    }

    public void workersToWork() {
        workers.forEach(p -> {
            p.setDestinationCells(getAllCellsCoordinatesByColor(p.getWorkplace().getColor()));
            p.setRouteMap(p.getWorkplace().getRouteMap());
        });
    }

    public void workersToLunch() {
        workers.forEach(p -> {
            Building building = new ArrayList<>(Building.gastronomy).get(new Random().nextInt(Building.gastronomy.size()));
            p.setDestinationCells(getAllCellsCoordinatesByColor(building.getColor()));
            p.setRouteMap(building.getRouteMap());
        });
    }

    public void workersGoBackHome() {
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

//    public synchronized void actualizeColorOfCells() {
//        Arrays.stream(map).flatMap(Stream::of).forEach(c -> {
//            int x = c.getX();
//            int y = c.getY();
//
//            long passerbyCounter = workers.stream().filter(p -> p.getX() == x && p.getY() == y).count();
//
//            if (map[x][y].getDefaultColor().equals(Building.SPAWN.getColor())) {
//
//            } else if (passerbyCounter == 1) {
//                c.setColor(Building.WORKER.getColor());
//            } else if (passerbyCounter > 1) {
//                c.setColor(Building.CROWD.getColor());
//            } else {
//                c.resetColorToDefault();
//            }
//
//        });
//    }

//    public Color getCellColor(int i, int j) {
//        return map[i][j].getColor();
//    }

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
