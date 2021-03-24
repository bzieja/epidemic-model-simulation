package pl.bzieja.pandemicmodel.Model.Cell;

import java.awt.*;

public abstract class Cell {
    boolean walkable;
    Color color;

    public Cell(boolean walkable, Color color) {
        this.walkable = walkable;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
