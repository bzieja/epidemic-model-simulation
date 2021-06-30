package pl.bzieja.pandemicmodel.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bzieja.pandemicmodel.model.Model;
import pl.bzieja.pandemicmodel.model.person.Person;
import pl.bzieja.pandemicmodel.view.coordinates.ModelCoordinates;
import pl.bzieja.pandemicmodel.view.coordinates.RelativeCoordinates;

import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

@Component
public class View {
    Model model;
    static Canvas canvasID;
    private double scaleFactor = 1;
    private int xOfTheFirstModelCellToGenerate;
    private int yOfTheFirstModelCellToGenerate;
    private static final int LEFT_RIGHT_MOVE_FACTOR = 30;
    private static final int UP_DOWN_MOVE_FACTOR = 12;
    private static final int AGH_IMAGE_HEIGHT = 300;
    private static final int AGH_IMAGE_WIDTH = 736;

    private final ConcurrentHashMap<ModelCoordinates, RelativeCoordinates> relativePositions;
    private int numberOfCellsToGenerateByWidth;
    private int numberOfCellsToGenerateByHeight;
    private double cellWidth;
    private double cellHeight;

    @Autowired
    public View(Model model) {
        this.model = model;
        xOfTheFirstModelCellToGenerate = 0;
        yOfTheFirstModelCellToGenerate = 0;
        relativePositions = new ConcurrentHashMap<>();
    }

    public synchronized void moveLeft() {
        if (yOfTheFirstModelCellToGenerate - LEFT_RIGHT_MOVE_FACTOR * scaleFactor < 0) {
            yOfTheFirstModelCellToGenerate = 0;
        } else {
            yOfTheFirstModelCellToGenerate -= LEFT_RIGHT_MOVE_FACTOR * scaleFactor;
        }
        generateNewView();
    }

    public synchronized void moveRight() {
        if (yOfTheFirstModelCellToGenerate + numberOfCellsToGenerateByWidth + LEFT_RIGHT_MOVE_FACTOR * scaleFactor > model.getMapHorizontalDimension()) {
            yOfTheFirstModelCellToGenerate = model.getMapHorizontalDimension() - numberOfCellsToGenerateByWidth;
        } else {
            yOfTheFirstModelCellToGenerate += LEFT_RIGHT_MOVE_FACTOR * scaleFactor;
        }
        generateNewView();
    }

    public synchronized void moveUp() {
        if (xOfTheFirstModelCellToGenerate - UP_DOWN_MOVE_FACTOR * scaleFactor < 0) {
            xOfTheFirstModelCellToGenerate = 0;
        } else {
            xOfTheFirstModelCellToGenerate -= UP_DOWN_MOVE_FACTOR * scaleFactor;
        }
        generateNewView();
    }

    public synchronized void moveDown() {
        if (xOfTheFirstModelCellToGenerate + numberOfCellsToGenerateByHeight + UP_DOWN_MOVE_FACTOR * scaleFactor > model.getMapVerticalDimension()) {
            xOfTheFirstModelCellToGenerate = model.getMapVerticalDimension() - numberOfCellsToGenerateByHeight;
        } else {
            xOfTheFirstModelCellToGenerate += UP_DOWN_MOVE_FACTOR * scaleFactor;
        }
        generateNewView();
    }

    public synchronized void zoomIn() {
        scaleFactor += 0.75;
        generateNewView();
    }

    public synchronized void zoomOut() {
        if (scaleFactor - 0.75 <= 1) {
            scaleFactor = 1;
            xOfTheFirstModelCellToGenerate = 0;
            yOfTheFirstModelCellToGenerate = 0;
        } else if (scaleFactor > 2) {
            scaleFactor -= 0.75;
        }
        generateNewView();
    }

    private void clearViewData(GraphicsContext graphicsContext) {
        relativePositions.clear();

        graphicsContext.beginPath();
        graphicsContext.setFill(javafx.scene.paint.Color.rgb(Color.WHITE.getRed(), Color.WHITE.getGreen(), Color.WHITE.getBlue(), Color.WHITE.getAlpha() / 255.0));
        graphicsContext.fillRect(0,0, canvasID.getWidth(), canvasID.getHeight());
    }

    public void generateNewView() {
        numberOfCellsToGenerateByWidth = (int) Math.round(AGH_IMAGE_WIDTH / scaleFactor);
        numberOfCellsToGenerateByHeight = (int) Math.round(AGH_IMAGE_HEIGHT / scaleFactor);

        cellWidth = canvasID.getWidth() / numberOfCellsToGenerateByWidth;
        cellHeight = canvasID.getHeight() / numberOfCellsToGenerateByHeight;

        GraphicsContext graphicsContext = canvasID.getGraphicsContext2D();
        clearViewData(graphicsContext);
        int xOfCurrentModelCell = xOfTheFirstModelCellToGenerate;
        int yOfCurrentModelCell = yOfTheFirstModelCellToGenerate;
        for (int i = 0; i < numberOfCellsToGenerateByHeight; i++, xOfCurrentModelCell++) {
            yOfCurrentModelCell = yOfTheFirstModelCellToGenerate;

            for (int j = 0; j < numberOfCellsToGenerateByWidth; j++, yOfCurrentModelCell++) {

                Color color = model.getCellByCoordinates(xOfCurrentModelCell, yOfCurrentModelCell).getDefaultColor();
                graphicsContext.beginPath();
                graphicsContext.setFill(convertAwtColorToJavaFxColor(color));
                //canvas coordinates needed
                graphicsContext.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);

                relativePositions.put(new ModelCoordinates(xOfCurrentModelCell, yOfCurrentModelCell), new RelativeCoordinates(j * cellWidth, i * cellHeight));
            }
        }
        generateViewForWorkersOnly();
    }

    public void generateRawViewMap() {
        numberOfCellsToGenerateByWidth = (int) Math.round(AGH_IMAGE_WIDTH / scaleFactor);
        numberOfCellsToGenerateByHeight = (int) Math.round(AGH_IMAGE_HEIGHT / scaleFactor);

        cellWidth = canvasID.getWidth() / numberOfCellsToGenerateByWidth;
        cellHeight = canvasID.getHeight() / numberOfCellsToGenerateByHeight;

        GraphicsContext graphicsContext = canvasID.getGraphicsContext2D();
        clearViewData(graphicsContext);
        int xOfCurrentModelCell = xOfTheFirstModelCellToGenerate;
        int yOfCurrentModelCell = yOfTheFirstModelCellToGenerate;
        for (int i = 0; i < numberOfCellsToGenerateByHeight; i++, xOfCurrentModelCell++) {
            yOfCurrentModelCell = yOfTheFirstModelCellToGenerate;

            for (int j = 0; j < numberOfCellsToGenerateByWidth; j++, yOfCurrentModelCell++) {

                Color color = model.getCellByCoordinates(xOfCurrentModelCell, yOfCurrentModelCell).getDefaultColor();
                graphicsContext.beginPath();
                graphicsContext.setFill(convertAwtColorToJavaFxColor(color));
                //canvas coordinates needed
                graphicsContext.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);

                relativePositions.put(new ModelCoordinates(xOfCurrentModelCell, yOfCurrentModelCell), new RelativeCoordinates(j * cellWidth, i * cellHeight));
            }
        }
    }

    public void generateViewForWorkersOnly() {
        GraphicsContext graphicsContext = canvasID.getGraphicsContext2D();

        Predicate<Person> shouldBePersonGenerated = new Predicate<Person>() {
            @Override
            public boolean test(Person person) {
                int x = person.getX();
                int y = person.getY();
                int maxX = xOfTheFirstModelCellToGenerate + numberOfCellsToGenerateByHeight;
                int maxY = yOfTheFirstModelCellToGenerate + numberOfCellsToGenerateByWidth;
                if (((x > xOfTheFirstModelCellToGenerate) && (x < maxX))
                        && ((y > yOfTheFirstModelCellToGenerate) && (y < maxY))) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        model.getWorkers().stream().filter(shouldBePersonGenerated)
                .forEach((w) -> {
                    var color = model.getCellColor(w.getX(), w.getY());
                    //var defaultColor = model.getCellByCoordinates(w.getX(), w.getY()).getDefaultColor();
                    graphicsContext.beginPath();
                    graphicsContext.setFill(convertAwtColorToJavaFxColor(color));
                    double[] coords = relativePositions.getOrDefault(new ModelCoordinates(w.getX(), w.getY()), new RelativeCoordinates(0, 0)).getCoordinates();
                    graphicsContext.fillRect(coords[0], coords[1], cellHeight, cellWidth);
                });
        clearPreviousPositionsOfWorkers();
    }

    private void clearPreviousPositionsOfWorkers() {
        GraphicsContext graphicsContext = canvasID.getGraphicsContext2D();

        Predicate<Person> shouldBeLastPersonCoordinatesRedraw = new Predicate<Person>() {
            @Override
            public boolean test(Person person) {
                int x = person.getPreviousX();
                int y = person.getPreviousY();
                int maxX = xOfTheFirstModelCellToGenerate + numberOfCellsToGenerateByHeight;
                int maxY = yOfTheFirstModelCellToGenerate + numberOfCellsToGenerateByWidth;
                if (((x > xOfTheFirstModelCellToGenerate) && (x < maxX))
                        && ((y > yOfTheFirstModelCellToGenerate) && (y < maxY))) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        model.getWorkers().stream().filter(shouldBeLastPersonCoordinatesRedraw)
                .forEach((w) -> {
                    double[] coords = relativePositions.get(new ModelCoordinates(w.getPreviousX(), w.getPreviousY())).getCoordinates();

                    Color color = model.getCellColor(w.getPreviousX(), w.getPreviousY());
                    graphicsContext.beginPath();
                    graphicsContext.setFill(convertAwtColorToJavaFxColor(Color.WHITE));
                    graphicsContext.fillRect(coords[0], coords[1], cellHeight, cellWidth);

                    graphicsContext.beginPath();
                    graphicsContext.setFill(convertAwtColorToJavaFxColor(color));
                    graphicsContext.fillRect(coords[0], coords[1], cellHeight, cellWidth);

                });
    }

    public synchronized void setCanvas(Canvas canvas) {
        this.canvasID = canvas;
    }

    private javafx.scene.paint.Color convertAwtColorToJavaFxColor(Color awtColor) {
        int r = awtColor.getRed() ;
        int g = awtColor.getGreen();
        int b = awtColor.getBlue();
        int a = awtColor.getAlpha();
        double opacity = a / 255.0;
        return new javafx.scene.paint.Color(r / 255.0, g / 255.0 , b / 255.0, opacity);
    }
}
