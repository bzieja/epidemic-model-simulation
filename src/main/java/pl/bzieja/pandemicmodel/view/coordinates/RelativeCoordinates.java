package pl.bzieja.pandemicmodel.view.coordinates;

import java.awt.*;
import java.util.Objects;

/**
 * @author Bartłomiej Zieja
 */
public class RelativeCoordinates {
    private double relativeX;
    private double relativeY;

    public RelativeCoordinates(double relativeX, double relativeY) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RelativeCoordinates)) return false;
        RelativeCoordinates that = (RelativeCoordinates) o;
        return Double.compare(that.relativeX, relativeX) == 0 && Double.compare(that.relativeY, relativeY) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(relativeX, relativeY);
    }

    public double[] getCoordinates () {
        return new double[] {relativeX, relativeY};
    }

}
