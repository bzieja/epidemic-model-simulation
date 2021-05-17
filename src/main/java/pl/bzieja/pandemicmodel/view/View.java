package pl.bzieja.pandemicmodel.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bzieja.pandemicmodel.model.Model;

import java.awt.*;

@Component
public class View {
    Model model;
    Canvas canvasID;
    private double scaleFactor = 1;
    private int xOfTheFirstCellToGenerate;
    private int yOfTheFirstCellToGenerate;

    @Autowired
    public View(Model model) {
        this.model = model;
        xOfTheFirstCellToGenerate = 0;
        yOfTheFirstCellToGenerate = 0;
    }

    public synchronized void moveLeft() {
        if (xOfTheFirstCellToGenerate - 10 * scaleFactor < 0) {
            xOfTheFirstCellToGenerate = 0;
        } else {
            xOfTheFirstCellToGenerate -= 10 * scaleFactor;
        }
    }

    public synchronized void moveRight() {
            xOfTheFirstCellToGenerate += 10 * scaleFactor;
    }

    public synchronized void moveUp() {
        if (yOfTheFirstCellToGenerate - 7 * scaleFactor < 0) {
            yOfTheFirstCellToGenerate = 0;
        } else {
            yOfTheFirstCellToGenerate -= 7 * scaleFactor;
        }
    }

    public synchronized void moveDown() {
            yOfTheFirstCellToGenerate += 7 * scaleFactor;
    }

    public synchronized void zoomIn() {
        scaleFactor += 0.75;
    }

    public synchronized void zoomOut() {
        if (scaleFactor - 0.75 <= 1) {
            scaleFactor = 1;
            xOfTheFirstCellToGenerate = 0;
            yOfTheFirstCellToGenerate = 0;
        } else if (scaleFactor > 2) {
            scaleFactor -= 0.75;
        }
    }

    public synchronized void generateView() {
        int cellsToGenerateAtX = (int) Math.round(canvasID.getWidth() / scaleFactor);
        int cellsToGenerateAtY = (int) Math.round(canvasID.getHeight() / scaleFactor);

        double cellXDimension = canvasID.getWidth() /  cellsToGenerateAtX;
        double cellYDimension = canvasID.getHeight() /  cellsToGenerateAtY;

        GraphicsContext graphicsContext = canvasID.getGraphicsContext2D();

        for (int i = 0; i < cellsToGenerateAtY && i < model.getMapVerticalDimension(); i++) {
            if (i + yOfTheFirstCellToGenerate >= model.getMapVerticalDimension()) {
                continue;
            }

            for (int j = 0; j < cellsToGenerateAtX && j < model.getMapHorizontalDimension(); j++) {
                if( j + xOfTheFirstCellToGenerate >= model.getMapHorizontalDimension()) {
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
            }
        }

    }

    public synchronized void setCanvas(Canvas canvas) {
        this.canvasID = canvas;
    }

}
