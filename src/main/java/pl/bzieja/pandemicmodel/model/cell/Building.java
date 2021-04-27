package pl.bzieja.pandemicmodel.model.cell;

import java.awt.*;
import java.util.Arrays;

public enum Building {

    SPAWN   (0, new Color(Integer.parseInt("793a13", 16))),
    B5      (5, new Color(Integer.parseInt("80473e", 16)));

    private final int numberOfWorkers;
    private final Color color;

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

}
