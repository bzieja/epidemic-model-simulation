package pl.bzieja.pandemicmodel.model;

import org.springframework.stereotype.Component;
import pl.bzieja.pandemicmodel.model.cell.Building;
import pl.bzieja.pandemicmodel.model.cell.Cell;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Model {

    private Cell[][] map;
    private List<Person> workers;

    public boolean isCellWalkable(int x, int y) {
        return map[x][y].isWalkable();
    }

    public Color getCellColor(int x, int y){

        //check if someone from workers is on that cell
        long passerbyCounter = workers.stream().filter(p -> Arrays.equals(p.getCoordinates(), new int[]{x, y})).count();

        if (passerbyCounter == 1) {
            return Building.WORKER.getColor();
        } else if (passerbyCounter > 1) {
            return Building.CROWD.getColor();
        } else {
            return map[x][y].getColor();
        }
    }

    public int getMapVerticalDimension() {
        return map.length;
    }

    public int getMapHorizontalDimension() {
        return map[0].length;
    }

    public int[] getCellCoordinateByColor(Color color) {
        //return Arrays.stream(map).flatMap(Stream::of).filter(e ->e.getColor().equals(color)).findFirst().orElseThrow().getCoordinates();

        List<Cell> list = new ArrayList<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j].getColor().equals(color)) {
                    list.add(map[i][j]);
                }
            }
        }

        return list.get(new Random().nextInt(list.size())).getCoordinates();

       //return Arrays.stream(map).flatMap(Stream::of).filter(e ->e.getColor().equals(color)).sorted((o1, o2) -> ThreadLocalRandom.current().nextInt(-1, 2)).findAny().orElseThrow().getCoordinates();
    }

    public List<Cell> getAllCellsCoordinatesByColor(Color color) {
        List<Cell> list = new ArrayList<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j].getColor().equals(color)) {
                    list.add(map[i][j]);
                }
            }
        }
        return list;
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

    public synchronized void moveWorkers() {
        workers.forEach(Person::makeMove);
    }

    public synchronized Cell getCellByCoordinates(int x, int y) {
        return map[x][y];
    }
}
