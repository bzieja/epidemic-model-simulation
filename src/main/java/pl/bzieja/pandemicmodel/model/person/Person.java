package pl.bzieja.pandemicmodel.model.person;

import pl.bzieja.pandemicmodel.model.cell.Building;
import pl.bzieja.pandemicmodel.model.cell.Cell;

import java.util.*;
import java.util.stream.Collectors;

public class Person {

    private int [][] routeMap;
    private int previousX;
    private int previousY;
    private int x;
    private int y;
    private final Building workplace;
    List<Cell> destinationCells;
    private HealthState healthState;
    private Building currentDestinationBuilding;

    public Person(int[] coordinates, Building workplace, List<Cell> destinationCells) {
        this.x = coordinates[0];
        this.y = coordinates[1];
        previousX = coordinates[0];
        previousY = coordinates[1];
        this.workplace = workplace;
        this.destinationCells = destinationCells;
        this.currentDestinationBuilding = null;
        this.healthState = HealthState.HEALTHY;
    }

    public boolean isAtTheDestinationPoint() {
        return destinationCells.stream().anyMatch(c -> c.getX() == x && c.getY() == y);
    }

    public void goToWorkplace() {
        routeMap = workplace.getRouteMap();
    }

    public void goWalkAroundTheBuilding() {
        routeMap = Building.BUILDING_INTERIOR.getRouteMap();
    }

    public void goLunch() {
        routeMap = new ArrayList<>(Building.gastronomy).get(new Random().nextInt(Building.gastronomy.size())).getRouteMap();
    }

    public void goBackHome() {
        routeMap = Building.SPAWN.getRouteMap();
    }

    public synchronized void makeMove() {


        List<Map.Entry<String, Integer>> list = new ArrayList<>();
        int xDimension = routeMap.length;
        int yDimension = routeMap[0].length;

        //up
        if (x - 1 > 0 ) {
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

        //String direction = list.stream().filter(e -> e.getValue() >= 0).sorted(Comparator.comparingInt(Map.Entry::getValue)).collect(Collectors.toList()).get(0).getKey();
        list.sort(Comparator.comparingInt(Map.Entry::getValue));
        var listOfPotentialMoves = list.stream().filter(c -> c.getValue().equals(list.get(0).getValue())).collect(Collectors.toList());
        String direction = listOfPotentialMoves.get(new Random().nextInt(listOfPotentialMoves.size())).getKey();

        previousX = x;
        previousY = y;

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

    public synchronized void setRouteMap(int[][] routeMap) {
        this.routeMap = routeMap;
    }

    public synchronized void setDestinationCells(List<Cell> destinationCells) {
        this.destinationCells = destinationCells;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPreviousX() {
        return previousX;
    }

    public int getPreviousY() {
        return previousY;
    }

    public Building getWorkplace() {
        return workplace;
    }

    public HealthState getHealthState() {
        return healthState;
    }

    public void setCurrentDestinationBuilding(Building building) {
        this.currentDestinationBuilding = building;
    }

    public Building getCurrentDestinationBuilding() {
        return currentDestinationBuilding;
    }

    public int[][] getRouteMap() {
        return routeMap;
    }
}
