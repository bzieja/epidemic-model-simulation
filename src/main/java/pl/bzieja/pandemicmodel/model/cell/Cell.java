package pl.bzieja.pandemicmodel.model.cell;

import pl.bzieja.pandemicmodel.model.Person;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Cell {
    protected boolean walkable;
    protected Color color;
    protected int x;
    protected int y;


    public Cell(boolean walkable, Color color, int x, int y) {
        this.walkable = walkable;
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public int[] getCoordinates() {
        return new int[]{getX(), getY()};
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell)) return false;
        Cell cell = (Cell) o;
        return x == cell.x && y == cell.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
