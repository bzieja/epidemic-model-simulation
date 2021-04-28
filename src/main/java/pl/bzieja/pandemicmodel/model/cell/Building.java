package pl.bzieja.pandemicmodel.model.cell;

import java.awt.*;
import java.util.Arrays;

//if building has more than one door -> each door should have different color and different route
//exception - spawn point

public enum Building {

    WORKER  (0, new Color(Integer.parseInt("f22d2d", 16))),
    CROWD   (0, new Color(Integer.parseInt("751461", 16))),
    SPAWN   (0, new Color(Integer.parseInt("793a13", 16))),
    PATH    (0, new Color(Integer.parseInt("8b4513", 16))),
    PARKING (0, new Color(Integer.parseInt("ae3939", 16))),

    B5      (5, new Color(Integer.parseInt("80473e", 16)));

    private final int numberOfWorkers;
    private final Color color;
    private int[][] routeMap;

    Building(int numberOfWorkers, Color color) {
        this.numberOfWorkers = numberOfWorkers;
        this.color = color;
    }

    public int getNumberOfWorkers() {
        return numberOfWorkers;
    }

    public Color getColor() {
        return color;
    }

    public void setRouteMap(int[][] routeMap) {
        this.routeMap = routeMap;
    }

    public int[][] getRouteMap() {
        return routeMap;
    }
}
