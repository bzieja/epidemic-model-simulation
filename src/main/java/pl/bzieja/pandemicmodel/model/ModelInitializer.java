package pl.bzieja.pandemicmodel.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bzieja.pandemicmodel.model.cell.*;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class ModelInitializer {

    Model model;

    @Autowired
    public ModelInitializer(Model model) {
        this.model = model;
    }

    public void createModelFromImage() {

        try {
            final BufferedImage bmp = ImageIO.read(new File("src/main/resources/aghMap.bmp"));
            int height = bmp.getHeight();
            int width = bmp.getWidth();
            Cell[][] map = new Cell[height][width];

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    Color currentColor = new Color(bmp.getRGB(j, i));

                    if (Building.PATH.getColor().equals(currentColor) || Building.PARKING.getColor().equals(currentColor)) {
                        map[i][j] = new Path(currentColor, i, j);
                    } else if (Building.SPAWN.getColor().equals(currentColor)) {
                        map[i][j] = new Spawn(currentColor, i, j);
                    } else {
                        map[i][j] = new Background(currentColor, i, j);
                    }
                }
            }
            model.setMap(map);
        } catch (IOException e) {
            e.printStackTrace();
        }

        createWorkers();
        createRouteTraces();
    }

    private void createWorkers() {
        List<Person> workers = new ArrayList<>();

        for (Building building : Building.values()) {
            for (int i = 0; i < building.getNumberOfWorkers(); i++) {
                workers.add(new Person(model.getCellCoordinateByColor(Building.SPAWN.getColor()), building, model.getCellCoordinateByColor(building.getColor())));
            }
        }
        model.setWorkers(workers);
    }

    private void createRouteTraces() {
        for (Building building : Building.values()) {
            if (building.equals(Building.CROWD) || building.equals(Building.WORKER) || building.equals(Building.PATH) || building.equals(Building.PARKING)) {
                continue;
            }  else {

                int[][] route = new int[model.getMapVerticalDimension()][model.getMapHorizontalDimension()];
                List<Cell> destinationCells = model.getAllCellsCoordinatesByColor(building.getColor());

                for (int i = 0; i < model.getMapVerticalDimension(); i++) {
                    for (int j = 0; j < model.getMapHorizontalDimension(); j++) {
                        if (model.isCellWalkable(i, j)) {
                            int x = i;
                            int y = j;
                            route[i][j] = destinationCells.stream().mapToInt(c -> calculateDistanceFromPoint(x, y, c.getX(), c.getY())).min().getAsInt();
                            //route[i][j] = calculateDistanceFromPoint(i, j, toX, toY);
                        } else {
                            route[i][j] = Integer.MAX_VALUE;
                        }
                    }
                }
                saveMatrixToTxt(route);
                building.setRouteMap(route);
            }
        }
    }

    private int calculateDistanceFromPoint(int x1, int y1, int x2, int y2) {
        return (int) Math.round(Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1)));
    }

    private void saveMatrixToTxt(int[][] route) {
        try {
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < route.length; i++)//for each row
            {
                for(int j = 0; j < route.length; j++)//for each column
                {
                    builder.append(route[i][j]+"");//append to the output string
                    if(j < route.length - 1)//if this is not the last row element
                        builder.append(",");//then add comma (if you don't like commas you can use spaces)
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
