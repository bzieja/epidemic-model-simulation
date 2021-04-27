package pl.bzieja.pandemicmodel.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bzieja.pandemicmodel.model.cell.Background;
import pl.bzieja.pandemicmodel.model.cell.Building;
import pl.bzieja.pandemicmodel.model.cell.Cell;
import pl.bzieja.pandemicmodel.model.cell.Path;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.util.ArrayList;
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

            Color pathColor = new Color(Integer.parseInt("8b4513", 16));
            Color buildingColor = new Color(141, 90, 153, 255);

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    Color currentColor = new Color(bmp.getRGB(j, i));
                    //System.out.println(bmp.getRGB(j, i));
                    //System.out.println(currentColor);
                    //System.out.println(currentColor.getRGB());


                    if (pathColor.equals(currentColor)) {
                        map[i][j] = new Path(currentColor, i, j);
                        //System.out.println("Sciezka");
                        //} else if (buildingColor.equals(currentColor)) {
                        //    map[i][j] = new Building(currentColor);
                    } else {
                        map[i][j] = new Background(currentColor, i, j);
                        //
                    }
                }
            }
            model.setMap(map);
        } catch (IOException e) {
            e.printStackTrace();
        }

        createRouteMap();
        createWorkers();
    }

    private void createRouteMap() {
        int[][] routeMap = new int[model.getMapVerticalDimension()][model.getMapHorizontalDimension()];
        for (int i = 0; i < model.getMapVerticalDimension(); i++) {
            for (int j = 0; j < model.getMapHorizontalDimension(); j++) {
                if (model.isCellWalkable(i, j)) {
                    routeMap[i][j] = 1;
                } else {
                    routeMap[i][j] = 0;
                }
            }
        }
        model.setRouteMap(routeMap);
    }

    private void createWorkers() {
        List<Person> workers = new ArrayList<>();

        for (Building building : Building.values()) {
            for (int i = 0; i < building.getNumberOfWorkers(); i++) {
                workers.add(new Person(model.getCellCoordinateByColor(Building.SPAWN.getColor()), model.getCellCoordinateByColor(building.getColor())));
            }

        }
    }












}
