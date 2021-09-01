package pl.bzieja.pandemicmodel.model.person;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.bzieja.pandemicmodel.model.cell.Building;
import pl.bzieja.pandemicmodel.model.cell.Cell;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {

    @JsonIgnore
    private int [][] routeMap;
    private int previousX;
    private int previousY;
    private int x;
    private int y;
    private final Building workplace;
    @JsonIgnore
    List<Cell> destinationCells;
    private HealthState healthState;
    private InteractionState interactionState;
    private Building currentDestinationBuilding;

    public Person() {
        this.workplace = null;
    }

    public Person(int[] coordinates, Building workplace, List<Cell> destinationCells) {
        this.x = coordinates[0];
        this.y = coordinates[1];
        previousX = coordinates[0];
        previousY = coordinates[1];
        this.workplace = workplace;
        this.destinationCells = destinationCells;
        this.currentDestinationBuilding = Building.SPAWN;
        this.healthState = HealthState.HEALTHY;
        this.interactionState = new InteractionState();
    }

    public boolean isAtTheDestinationPoint() {
        //return destinationCells.stream().anyMatch(c -> c.getX() == x && c.getY() == y);
         if (currentDestinationBuilding.equals(Building.SPAWN)) {
             return false;
        } else {
             return currentDestinationBuilding.getCellsWhichBelongsToGivenBuilding().contains(new Cell(x, y, false, null));
        }
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

    public InteractionState getInteractionState() {
        return interactionState;
    }

    public double calculateDistanceToAnotherPerson(Person person) {
        return Math.sqrt((person.getY() - y) * (person.getY() - y) + (person.getX() - x) * (person.getX() - x));
    }

    public void setHealthState(HealthState healthState) {
        this.healthState = healthState;
    }

    public List<Cell> getDestinationCells() {
        return destinationCells;
    }

    @Override
    public String toString() {
        return "routeMap" + " " + Arrays.deepToString(routeMap) +
                "previousX" + " " + previousX +
                "previousY" + " " + previousY +
                "x" + " " + x +
                "y" + " " + y +
                "workplace" + " " + workplace +
                "destinationCells" + " " + destinationCells +
                "healthState" + " " + healthState +
                "interactionState" + " " + interactionState +
                "currentDestinationBuilding" + " " + currentDestinationBuilding;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return previousX == person.previousX && previousY == person.previousY && x == person.x && y == person.y && Arrays.equals(routeMap, person.routeMap) && workplace == person.workplace && Objects.equals(destinationCells, person.destinationCells) && healthState == person.healthState && Objects.equals(interactionState, person.interactionState) && currentDestinationBuilding == person.currentDestinationBuilding;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(previousX, previousY, x, y, workplace, destinationCells, healthState, interactionState, currentDestinationBuilding);
        result = 31 * result + Arrays.hashCode(routeMap);
        return result;
    }

    //    @SuppressWarnings("unchecked")
//    @JsonProperty("destinationCells")
//    private void unpackNested(Map<String,Object> destinationCells) {
//        this.x = (int) destinationCells.get("x");
//        this.y = (int) destinationCells.get("y");
//        this.walkable = (boolean) destinationCells.get("walkable");
//        this.defaultColor = (Color) destinationCells.get("defaultColor");
//        Map<String,String> owner = (Map<String,String>)destinationCells.get("owner");
//        this.ownerName = owner.get("name");
//    }
}
