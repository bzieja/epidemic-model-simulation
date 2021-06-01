package pl.bzieja.pandemicmodel.view;

import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import pl.bzieja.pandemicmodel.model.cell.Cell;

import java.awt.*;

/**
 * @author Bartłomiej Zieja
 */
public class CanvasCell {

    private final int i;
    private final int j;
    private final double cellXDimension;
    private final double cellYDimension;
    Property<Color> color = new SimpleObjectProperty<>();
    Cell cellToFollow;
    private Canvas canvasID;
    boolean toRedraw = false;

    public CanvasCell(int i, int j, double cellXDimension, double cellYDimension, Cell cellToFollow, Canvas canvasID) {
        this.i = i;
        this.j = j;
        this.cellXDimension = cellXDimension;
        this.cellYDimension = cellYDimension;
        color.bind(cellToFollow.getColorProperty());
        this.canvasID = canvasID;

        this.cellToFollow = cellToFollow;
        this.cellToFollow.getColorProperty().addListener(changeColorListener);
    }


    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public double getCellXDimension() {
        return cellXDimension;
    }

    public double getCellYDimension() {
        return cellYDimension;
    }

    public void removeListener(){
        cellToFollow.getColorProperty().removeListener(changeColorListener);
    }

    private ChangeListener<Color> changeColorListener = new ChangeListener<Color>() {
        @Override
        public void changed(ObservableValue<? extends Color> observableValue, Color color, Color t1) {
            toRedraw = true;
            //hasBeenChanged = true;
            //View.draw(javafx.scene.paint.Color.rgb(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 255.0), i, j, cellXDimension, cellYDimension);
            //Platform.runLater(() -> draw());
        }
    };
//
//    protected void draw() {
//        //System.out.println("Static draw color: " + color + " for cell i: " + i + " j: " + j);
//        GraphicsContext graphicsContext = View.getCanvasID().getGraphicsContext2D();
//        graphicsContext.beginPath();
//        graphicsContext.setFill(javafx.scene.paint.Color.rgb(this.color.getValue().getRed(), this.color.getValue().getGreen(), this.color.getValue().getBlue(), this.color.getValue().getAlpha() / 255.0));
//        graphicsContext.rect(j * cellYDimension, i * cellXDimension, cellYDimension, cellXDimension);
//        graphicsContext.fill();
//    }

}
