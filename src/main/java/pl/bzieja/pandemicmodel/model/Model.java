package pl.bzieja.pandemicmodel.model;

import org.springframework.stereotype.Component;
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
    private int[][] routeMap;
    private List<Person> workers;


    public boolean isCellWalkable(int x, int y) {
        return map[x][y].isWalkable();
    }

    public Color getCellColor(int x, int y){
        //to do: if cell stores people -> special color for "crowd"
        return map[x][y].getColor();
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

    public void setMap(Cell[][] map) {
        this.map = map;
    }

    public void setRouteMap(int[][] routeMap) {
        this.routeMap = routeMap;
    }
}
