package pl.bzieja.pandemicmodel.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bzieja.pandemicmodel.model.Model;

import java.awt.*;
import java.util.*;
import java.util.List;

@Component
public class View {
    Model model;
    static Canvas canvasID;
    private double scaleFactor = 1;
    private int xOfTheFirstCellToGenerate;
    private int yOfTheFirstCellToGenerate;
    private List<CanvasCell> bindedCells;

    @Autowired
    public View(Model model) {
        this.model = model;
        xOfTheFirstCellToGenerate = 0;
        yOfTheFirstCellToGenerate = 0;
        bindedCells = new ArrayList<>();
    }

    public synchronized void moveLeft() {
        unbindCells();
        if (xOfTheFirstCellToGenerate - 10 * scaleFactor < 0) {
            xOfTheFirstCellToGenerate = 0;
        } else {
            xOfTheFirstCellToGenerate -= 10 * scaleFactor;
        }
        generateNewView();
    }

    public synchronized void moveRight() {
        unbindCells();
        xOfTheFirstCellToGenerate += 10 * scaleFactor;
        generateNewView();
    }

    public synchronized void moveUp() {
        unbindCells();
        if (yOfTheFirstCellToGenerate - 7 * scaleFactor < 0) {
            yOfTheFirstCellToGenerate = 0;
        } else {
            yOfTheFirstCellToGenerate -= 7 * scaleFactor;
        }
        generateNewView();
    }

    public synchronized void moveDown() {
        unbindCells();
        yOfTheFirstCellToGenerate += 7 * scaleFactor;
        generateNewView();
    }

    public synchronized void zoomIn() {
        unbindCells();
        scaleFactor += 0.75;
        generateNewView();
    }

    public synchronized void zoomOut() {
        unbindCells();
        if (scaleFactor - 0.75 <= 1) {
            scaleFactor = 1;
            xOfTheFirstCellToGenerate = 0;
            yOfTheFirstCellToGenerate = 0;
        } else if (scaleFactor > 2) {
            scaleFactor -= 0.75;
        }
        generateNewView();
    }

    public synchronized void generateNewView() {
        int cellsToGenerateAtX = (int) Math.round(canvasID.getWidth() / scaleFactor);
        int cellsToGenerateAtY = (int) Math.round(canvasID.getHeight() / scaleFactor);

        double cellXDimension = canvasID.getWidth() / cellsToGenerateAtX;
        double cellYDimension = canvasID.getHeight() / cellsToGenerateAtY;

        GraphicsContext graphicsContext = canvasID.getGraphicsContext2D();

        for (int i = 0; i < cellsToGenerateAtY && i < model.getMapVerticalDimension(); i++) {
            if (i + yOfTheFirstCellToGenerate >= model.getMapVerticalDimension()) {
                continue;
            }

            for (int j = 0; j < cellsToGenerateAtX && j < model.getMapHorizontalDimension(); j++) {
                if (j + xOfTheFirstCellToGenerate >= model.getMapHorizontalDimension()) {
                    continue;
                }

                graphicsContext.beginPath();
                Color color = model.getCellColor(i + yOfTheFirstCellToGenerate, j + xOfTheFirstCellToGenerate);
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                int a = color.getAlpha();
                double opacity = a / 255.0;
                graphicsContext.setFill(javafx.scene.paint.Color.rgb(r, g, b, opacity));

                graphicsContext.rect(j * cellYDimension, i * cellXDimension, cellYDimension, cellXDimension);
                graphicsContext.fill();

                bindedCells.add(new CanvasCell(i, j, cellXDimension, cellYDimension, model.getCellByCoordinates(i + yOfTheFirstCellToGenerate, j + xOfTheFirstCellToGenerate), canvasID));
            }
        }

    }

    public synchronized void setCanvas(Canvas canvas) {
        this.canvasID = canvas;
    }

    private synchronized void unbindCells() {
        bindedCells.forEach(c -> {
            c.removeListener();
            c.color.unbind();
        });
        bindedCells.clear();
    }

    //javafx.scene.paint.Color color, int i, int j, double cellXDimension, double cellYDimension
    public void drawCells() {
        bindedCells.stream().filter(c -> c.toRedraw).forEach(this::draw);
    }

    private synchronized void draw(CanvasCell c) {
        GraphicsContext graphicsContext = canvasID.getGraphicsContext2D();
        graphicsContext.beginPath();
        graphicsContext.setFill(javafx.scene.paint.Color.rgb(c.color.getValue().getRed(), c.color.getValue().getGreen(), c.color.getValue().getBlue(), c.color.getValue().getAlpha() / 255.0));

        graphicsContext.rect(c.getJ() * c.getCellYDimension(), c.getI() * c.getCellXDimension(), c.getCellYDimension(), c.getCellXDimension());
        graphicsContext.fill();
        c.toRedraw = false;
    }

    public static Canvas getCanvasID() {
        return canvasID;
    }
}
