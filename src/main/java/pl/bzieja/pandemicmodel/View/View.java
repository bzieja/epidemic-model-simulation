package pl.bzieja.pandemicmodel.View;

import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bzieja.pandemicmodel.Model.Model;

import java.awt.*;

@Component
public class View {
    Model model;
    private int scaleFactor;
    private int xOfTheFirstCellToGenerate;
    private int yOfTheFirstCellToGenerate;

    @Autowired
    public View(Model model) {
        this.model = model;
        scaleFactor = 1;
        xOfTheFirstCellToGenerate = 0;
        yOfTheFirstCellToGenerate = 0;
    }

    public void generateView(Canvas canvasID) {
        int cellsToGenerateAtX = (int) Math.round(canvasID.getWidth() / scaleFactor);
        int cellsToGenerateAtY = (int) Math.round(canvasID.getHeight() / scaleFactor);

        double cellXDimension = canvasID.getWidth() /  cellsToGenerateAtX;
        double cellYDimension = canvasID.getHeight() /  cellsToGenerateAtY;

        GraphicsContext graphicsContext = canvasID.getGraphicsContext2D();

        for (int i = 0; i < cellsToGenerateAtY && i < model.getMap().length; i++) {
            if (i + yOfTheFirstCellToGenerate >= model.getMap().length) {
                continue;
            }

            for (int j = 0; j < cellsToGenerateAtX && j < model.getMap()[0].length; j++) {
                if( j + xOfTheFirstCellToGenerate >= model.getMap()[0].length) {
                    continue;
                }

                graphicsContext.beginPath();
                Color color = model.getMap()[i + yOfTheFirstCellToGenerate][j + xOfTheFirstCellToGenerate].getColor();
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

    public void moveLeft() {
        if (xOfTheFirstCellToGenerate - 10 * scaleFactor < 0) {
            xOfTheFirstCellToGenerate = 0;
        } else {
            xOfTheFirstCellToGenerate -= 10 * scaleFactor;
        }
    }

    public void moveRight() {
//        if (xOfTheFirstCellToGenerate + 10 * scaleFactor >= model.getMap().length) {
//            xOfTheFirstCellToGenerate = model.getMap().length - 10 * scaleFactor;
//        } else {
            xOfTheFirstCellToGenerate += 10 * scaleFactor;
//        }
    }

    public void moveUp() {
        if (yOfTheFirstCellToGenerate - 10 * scaleFactor < 0) {
            yOfTheFirstCellToGenerate = 0;
        } else {
            yOfTheFirstCellToGenerate -= 10 * scaleFactor;
        }
    }

    public void moveDown() {
//        if (yOfTheFirstCellToGenerate + 10 * scaleFactor >= model.getMap()[0].length) {
//            yOfTheFirstCellToGenerate = model.getMap()[0].length - 10 * scaleFactor;
//        } else {
            yOfTheFirstCellToGenerate += 10 * scaleFactor;
//        }
    }

    public void zoomIn() {
        scaleFactor += 1;
    }

    public void zoomOut() {
        if (scaleFactor - 1 == 1) {
            scaleFactor = 1;
            xOfTheFirstCellToGenerate = 0;
            yOfTheFirstCellToGenerate = 0;
        } else if (scaleFactor > 2) {
            scaleFactor -= 1;
        }
    }
}
