package pl.bzieja.pandemicmodel.core.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bzieja.pandemicmodel.core.cell.*;
import pl.bzieja.pandemicmodel.core.person.Person;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

@Component
public class ModelInitializer {

    Model model;
    Logger logger = LoggerFactory.getLogger(ModelInitializer.class);

    @Autowired
    public ModelInitializer(Model model) {
        this.model = model;
    }

    public void initialize() {

        createModelFromImage();
        createRouteTraces();
        createWorkers();
        assignCellsToBuildings();
    }

    private void createModelFromImage() {
        logger.info("Creating model from image!");
        try {
            logger.info("Start reading image!");
            final BufferedImage bmp = ImageIO.read(new File("src/main/resources/aghMap.bmp"));
            int height = bmp.getHeight();
            int width = bmp.getWidth();
            Cell[][] map = new Cell[height][width];

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    Color currentColor = new Color(bmp.getRGB(j, i));

                    if (Building.walkable.stream().anyMatch(b -> b.getColor().equals(currentColor))) {
                        map[i][j] = new Cell(i, j, true, currentColor);
                    } else {
                        map[i][j] = new Cell(i, j, false, currentColor);
                    }
                }
            }
            logger.info("Setting map!");
            model.setWorldMap(map);
            logger.info("End of reading image from file!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void assignCellsToBuildings() {
        Building.buildings.forEach(b -> b.setCellsWhichBelongsToGivenBuilding(model.getAllCellsCoordinatesByColor(b.getColor())));
    }

    private void createWorkers() {
        logger.info("Start creating workers");
        List<Person> workers = new ArrayList<>();

        Building.buildings.stream()
                .filter(building -> building.getNumberOfWorkers() > 0)
                .forEach(building -> IntStream.range(0, building.getNumberOfWorkers())
                .forEach(i -> workers.add(new Person(model.getRandomCellCoordinateByColor(Building.SPAWN.getColor()), building, model.getAllCellsCoordinatesByColor(building.getColor())))));
        logger.info("Setting workers to model");
        model.setWorkers(workers);
    }


    /**
     * Create routes only for this buildings which could be destination place for workers
     */
    private void createRouteTraces() {
        logger.info("Create route traces");
        Building.buildings.forEach(b -> b.setRouteMap(findTheShortestPathToGivenPlaces(model.getAllCellsCoordinatesByColor(b.getColor()))));
    }

    /**
     * Computes the route for given places. There are cases:
     * Integer.MAX_VALUE if Cell is not walkable for human
     * 0 for Cells which are walkable and hasn't been visited by algorithm for some reason (e.g. Cell couldn't be reached)
     * >0 distance to the source where 1 means that we are at the destination point
     * @param places places to which we want compute trace
     * @return
     */
    private int[][] findTheShortestPathToGivenPlaces(List<Cell> places) {
        logger.info("Finding the shortest path for " + places);
        final int distanceAtTheDestinationPoint = 1;
        final int valueForNonWalkableFields = Integer.MAX_VALUE;
        final int valueForNonVisitedFields = 0;
        final int xDimension = model.getMapVerticalDimension();
        final int yDimension = model.getMapHorizontalDimension();


        int[][] route = new int[xDimension][yDimension];
        Queue<Cell> queue = new LinkedList<>(places);

        IntStream.range(0, xDimension * yDimension).
                forEach(n -> route[n / yDimension][n % yDimension] = model.isCellWalkable(n / yDimension, n % yDimension) ? 0 : valueForNonWalkableFields);
        places.forEach(c -> route[c.getX()][c.getY()] = distanceAtTheDestinationPoint);


        while (!queue.isEmpty()) {

            Cell currentCell = queue.remove();
            int x = currentCell.getX();
            int y = currentCell.getY();
            int currentCost = route[x][y];

            //up
            if (x - 1 > 0 && route[x - 1][y] == valueForNonVisitedFields) {
                route[x-1][y]= currentCost + 1;
                queue.add(model.getCellByCoordinates(x -1, y));
            }

            //up-right corner
            if (x - 1 > 0 && y + 1 < yDimension && route[x-1][y+1] == valueForNonVisitedFields) {
                route[x - 1][y + 1]= currentCost + 1;
                queue.add(model.getCellByCoordinates(x - 1, y + 1));
            }

            //right
            if (y + 1 < yDimension && route[x][y+1] == valueForNonVisitedFields) {
                route[x][y + 1]= currentCost + 1;
                queue.add(model.getCellByCoordinates(x, y + 1));
            }

            //down-right corner
            if (x + 1 < xDimension && y + 1 < yDimension && route[x + 1][y + 1] == valueForNonVisitedFields) {
                route[x + 1][y + 1]= currentCost + 1;
                queue.add(model.getCellByCoordinates(x + 1, y + 1));
            }

            //down
            if (x + 1 < xDimension && route[x + 1][y] == valueForNonVisitedFields) {
                route[x + 1][y]= currentCost + 1;
                queue.add(model.getCellByCoordinates(x + 1, y));
            }

            //down-left corner
            if (x + 1 < xDimension && y - 1 > 0 && route[x + 1][y - 1] == valueForNonVisitedFields) {
                route[x + 1][y - 1]= currentCost + 1;
                queue.add(model.getCellByCoordinates(x + 1, y - 1));
            }

            //left corner
            if (y - 1 > 0 && route[x][y - 1 ] == valueForNonVisitedFields) {
                route[x][y - 1 ]= currentCost + 1;
                queue.add(model.getCellByCoordinates(x,y - 1 ));
            }

            //up-left corner
            if (x - 1 > 0 && y - 1 > 0 && route[x-1][y - 1] == valueForNonVisitedFields) {
                route[x-1][y - 1]= currentCost + 1;
                queue.add(model.getCellByCoordinates(x -1, y - 1));
            }

        }

        return route;
    }

    /**
     * Method for debugging purposes.
     * @param route
     */
    private void saveMatrixToTxt(int[][] route) {
        try {
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < route.length; i++)//for each row
            {
                for(int j = 0; j < route.length; j++)//for each column
                {
                    builder.append(route[i][j]+ "\t");//append to the output string
                    if(j < route.length - 1)//if this is not the last row element
                        builder.append("");//then add comma (if you don't like commas you can use spaces)
                }
                builder.append("\n");//append new line at the end of the row
            }
            BufferedWriter writer = null;
            writer = new BufferedWriter(new FileWriter("src/main/resources/" + LocalDate.now().toString() + ".txt"));
            writer.write(builder.toString());//save the string representation of the board
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
