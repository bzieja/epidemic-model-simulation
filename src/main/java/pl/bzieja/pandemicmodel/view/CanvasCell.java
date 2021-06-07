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

    private int previousI;
    private int previousJ;
    private int i;
    private int j;
//    private final double cellXDimension;
//    private final double cellYDimension;
//    Property<Color> color = new SimpleObjectProperty<>();
//    boolean toRedraw = false;

//    public CanvasCell(int i, int j, double cellXDimension, double cellYDimension, Property<Color> colorToFollow) {
//        this.i = i;
//        this.j = j;
//        this.cellXDimension = cellXDimension;
//        this.cellYDimension = cellYDimension;
//        color.bind(colorToFollow);
//
//        this.color.addListener(changeColorListener);
//    }

    public CanvasCell(int i, int j) {
        this.i = i;
        this.j = j;
        previousI = i;
        previousJ = j;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }


//    public double getCellXDimension() {
//        return cellXDimension;
//    }
//
//    public double getCellYDimension() {
//        return cellYDimension;
//    }
//
//    public void removeListener(){
//        this.color.removeListener(changeColorListener);
//    }

//    private ChangeListener<Color> changeColorListener = new ChangeListener<Color>() {
//        @Override
//        public void changed(ObservableValue<? extends Color> observableValue, Color color, Color t1) {
//            //View.draw(javafx.scene.paint.Color.rgb(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 255.0), i, j, cellXDimension, cellYDimension);
////            GraphicsContext graphicsContext = View.getCanvasID().getGraphicsContext2D();
////            graphicsContext.beginPath();
////            graphicsContext.setFill(javafx.scene.paint.Color.rgb(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 255.0));
////            graphicsContext.rect(j * cellYDimension, i * cellXDimension, cellYDimension, cellXDimension);
////            System.out.println("Change from CanvasCell");
////            graphicsContext.fill();
//            Platform.runLater(() -> View.draw(javafx.scene.paint.Color.rgb(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 255.0), i, j, cellXDimension, cellYDimension));
//        }
//    };
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
