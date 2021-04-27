package pl.bzieja.pandemicmodel.model;

import pl.bzieja.pandemicmodel.model.cell.Building;

import java.util.List;

public class Person {

    private int x;
    private int y;
    private int[] workplaceCoordinates;
    private int[] destinationCoordinates;
    //isActive ?
    private List<Integer[]> nextMoves;


    public Person(int[] coordinates, int[] workplaceCoordinates) {
        this.x = coordinates[0];
        this.y = coordinates[1];
        this.workplaceCoordinates = workplaceCoordinates;
        System.out.println("Hello! I am new worker with coordinates: " + coordinates[0] + ", " + coordinates[1]);
    }


}
