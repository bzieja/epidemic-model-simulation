package pl.bzieja.pandemicmodel.model.cell;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.awt.Color;
import java.util.Objects;

public class Cell {
    protected boolean walkable;
    protected Color defaultColor;
    //protected Property<Color> color;
    protected int x;
    protected int y;


    public Cell(int x, int y, boolean walkable, Color defaultColor) {
        this.x = x;
        this.y = y;
        this.walkable = walkable;
        this.defaultColor = defaultColor;
        //this.color = new SimpleObjectProperty<>(defaultColor);
    }

    public Color getDefaultColor() {
        return defaultColor;
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



//    public void setColor(Color color) {
//        this.color.setValue(color);
//        System.out.println("Change from Cell");
//    }
//
//    public void resetColorToDefault() {
//        this.color.setValue(defaultColor);
//    }
//
//    public Color getColor() {
//        return color.getValue();
//    }
//
//    public Property<Color> getColorProperty() {
//        return color;
//    }
}
