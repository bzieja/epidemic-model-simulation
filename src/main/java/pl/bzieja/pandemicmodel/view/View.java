package pl.bzieja.pandemicmodel.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bzieja.pandemicmodel.model.Model;
import pl.bzieja.pandemicmodel.model.cell.Building;

import java.awt.*;
import java.util.*;

@Component
public class View {
    Model model;
    static Canvas canvasID;
    private double scaleFactor = 1;
    private int xOfTheFirstCellToGenerate;
    private int yOfTheFirstCellToGenerate;
    private HashMap<ModelCoordinates, RelativeCoordinates> relativePositions;

    //private List<CanvasCell> workerCellsToRedraw;

    int cellsToGenerateAtX;
    int cellsToGenerateAtY;
    double cellXDimension;
    double cellYDimension;

    @Autowired
    public View(Model model) {
        this.model = model;
        xOfTheFirstCellToGenerate = 0;
        yOfTheFirstCellToGenerate = 0;
        relativePositions = new HashMap<>();
        //workerCellsToRedraw = new ArrayList<>();
    }

    public synchronized void moveLeft() {
        //resetWorkers();
        if (xOfTheFirstCellToGenerate - 10 * scaleFactor < 0) {
            xOfTheFirstCellToGenerate = 0;
        } else {
            xOfTheFirstCellToGenerate -= 10 * scaleFactor;
        }
        generateNewView();
    }

    public synchronized void moveRight() {
        //resetWorkers();
        xOfTheFirstCellToGenerate += 10 * scaleFactor;
        generateNewView();
    }

    public synchronized void moveUp() {
        //resetWorkers();
        if (yOfTheFirstCellToGenerate - 7 * scaleFactor < 0) {
            yOfTheFirstCellToGenerate = 0;
        } else {
            yOfTheFirstCellToGenerate -= 7 * scaleFactor;
        }
        generateNewView();
    }

    public synchronized void moveDown() {
        //resetWorkers();
        yOfTheFirstCellToGenerate += 7 * scaleFactor;
        generateNewView();
    }

    public synchronized void zoomIn() {
        //resetWorkers();
        scaleFactor += 0.75;
        generateNewView();
    }

    public synchronized void zoomOut() {
        //resetWorkers();
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
        relativePositions.clear();

        cellsToGenerateAtX = (int) Math.round(canvasID.getWidth() / scaleFactor);
        cellsToGenerateAtY = (int) Math.round(canvasID.getHeight() / scaleFactor);

        cellXDimension = canvasID.getWidth() / cellsToGenerateAtX;
        cellYDimension = canvasID.getHeight() / cellsToGenerateAtY;

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

//                if (color.equals(Building.WORKER.getColor())) {
//                    workerCellsToRedraw.add(new CanvasCell(i, j));
//                }

                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                int a = color.getAlpha();
                double opacity = a / 255.0;
                graphicsContext.setFill(javafx.scene.paint.Color.rgb(r, g, b, opacity));

                //if (Building.walkable.stream().anyMatch(q -> q.getColor().equals(color))) {
                    relativePositions.put(new ModelCoordinates(i + yOfTheFirstCellToGenerate, j + xOfTheFirstCellToGenerate), new RelativeCoordinates(j * cellYDimension, i * cellXDimension));
                //}
                graphicsContext.rect(j * cellYDimension, i * cellXDimension, cellYDimension, cellXDimension);
                graphicsContext.fill();

                //bindedCells.add(new CanvasCell(i, j, cellXDimension, cellYDimension, model.getCellByCoordinates(i + yOfTheFirstCellToGenerate, j + xOfTheFirstCellToGenerate).getColorProperty()));
            }
        }
    }

    public synchronized void generateViewForWorkersOnly() {
        GraphicsContext graphicsContext = canvasID.getGraphicsContext2D();

        model.getWorkers().stream().filter(w -> (w.getX() >= xOfTheFirstCellToGenerate && w.getX() < (xOfTheFirstCellToGenerate + cellsToGenerateAtY)
        && (w.getY() >= yOfTheFirstCellToGenerate && w.getY() < (yOfTheFirstCellToGenerate + cellsToGenerateAtX))))
                .forEach((w) -> {
                    Color color = model.getCellColor(w.getX(), w.getY());
                    int r = color.getRed();
                    int g = color.getGreen();
                    int b = color.getBlue();
                    int a = color.getAlpha();
                    double opacity = a / 255.0;

                    graphicsContext.beginPath();
                    graphicsContext.setFill(javafx.scene.paint.Color.rgb(r, g, b, opacity));
                    double[] coords = relativePositions.get(new ModelCoordinates(w.getX(), w.getY())).getCoordinates();
                    graphicsContext.rect(coords[0], coords[1], cellYDimension, cellXDimension);
                    graphicsContext.fill();
                    System.out.println("Worker is moving!");
                });
    }

    public synchronized void setCanvas(Canvas canvas) {
        this.canvasID = canvas;
    }

    private synchronized void resetWorkers() {
//        workerCells.forEach(c -> {
//            c.removeListener();
//            c.color.unbind();
//        });
        //workerCellsToRedraw.clear();
    }

    //javafx.scene.paint.Color color, int i, int j, double cellXDimension, double cellYDimension

    public static void draw(javafx.scene.paint.Color color, int i, int j, double cellXDimension, double cellYDimension) {
        GraphicsContext graphicsContext = canvasID.getGraphicsContext2D();
        graphicsContext.beginPath();
        graphicsContext.setFill(color);

        graphicsContext.rect(j * cellYDimension, i * cellXDimension, cellYDimension, cellXDimension);
        graphicsContext.fill();
    }

    public static Canvas getCanvasID() {
        return canvasID;
    }
}
