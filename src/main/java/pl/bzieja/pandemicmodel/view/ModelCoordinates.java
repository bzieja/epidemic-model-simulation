package pl.bzieja.pandemicmodel.view;

import java.util.Objects;

/**
 * @author Bartłomiej Zieja
 */
public class ModelCoordinates {
    private int x;
    private int y;

    public ModelCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModelCoordinates)) return false;
        ModelCoordinates that = (ModelCoordinates) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
