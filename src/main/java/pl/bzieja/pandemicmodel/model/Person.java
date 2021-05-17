package pl.bzieja.pandemicmodel.model;

import pl.bzieja.pandemicmodel.model.cell.Building;

import java.util.*;

public class Person {

    private int [][] routeMap;
    private int x;
    private int y;
    private Building workplace;
    private int[] destinationCoordinates;
    //isActive ?

    public Person(int[] coordinates, Building workplace, int[] destinationCoordinates) {
        this.x = coordinates[0];
        this.y = coordinates[1];
        this.workplace = workplace;
        this.destinationCoordinates = destinationCoordinates;
    }

    public int[] getCoordinates() {
        return new int[]{this.x, this.y};
    }

    public boolean isAtTheDestinationPoint() {
        return x == destinationCoordinates[0] && y == destinationCoordinates[1];
    }

    public synchronized void makeMove() {
        routeMap = workplace.getRouteMap();
        List<Map.Entry<String, Integer>> list = new ArrayList<>();
        int xDimension = routeMap.length;
        int yDimension = routeMap[0].length;

        //up
        if (x - 1 > 0) {
            list.add(new AbstractMap.SimpleEntry<>("UP", routeMap[x-1][y]));
        }

        //up-right corner
        if (x - 1 > 0 && y + 1 < yDimension) {
            list.add(new AbstractMap.SimpleEntry<>("UP-RIGHT", routeMap[x-1][y+1]));
        }

        //right
        if (y + 1 < yDimension) {
            list.add(new AbstractMap.SimpleEntry<>("RIGHT", routeMap[x][y+1]));
        }

        //down-right corner
        if (x + 1 < xDimension && y + 1 < yDimension) {
            list.add(new AbstractMap.SimpleEntry<>("DOWN-RIGHT", routeMap[x+1][y+1]));
        }

        //down
        if (x + 1 < xDimension) {
            list.add(new AbstractMap.SimpleEntry<>("DOWN", routeMap[x+1][y]));
        }

        //down-left corner
        if (x + 1 < xDimension && y - 1 > 0) {
            list.add(new AbstractMap.SimpleEntry<>("DOWN-LEFT", routeMap[x+1][y-1]));
        }

        //left corner
        if (y - 1 > 0) {
            list.add(new AbstractMap.SimpleEntry<>("LEFT", routeMap[x][y-1]));
        }

        //up-left corner
        if (x - 1 > 0 && y - 1 > 0 ) {
            list.add(new AbstractMap.SimpleEntry<>("UP-LEFT", routeMap[x-1][y-1]));
        }

        String direction = list.stream().filter(e -> e.getValue() >= 0).min(Comparator.comparingInt(Map.Entry::getValue)).get().getKey();


        switch (direction) {
            case "UP":
                this.x--;
                break;
            case "UP-RIGHT":
                this.x--;
                this.y++;
                break;
            case "RIGHT":
                this.y++;
                break;
            case "DOWN-RIGHT":
                this.x++;
                this.y++;
                break;
            case "DOWN":
                this.x++;
                break;
            case "DOWN-LEFT":
                this.x++;
                this.y--;
                break;
            case "LEFT":
                this.y--;
                break;
            case "UP-LEFT":
                this.x--;
                this.y--;
                break;
            default:
                System.err.println("No direction");
        }


    }
}
