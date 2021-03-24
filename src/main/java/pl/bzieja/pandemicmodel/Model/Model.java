package pl.bzieja.pandemicmodel.Model;

import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bzieja.pandemicmodel.Model.Cell.Background;
import pl.bzieja.pandemicmodel.Model.Cell.Building;
import pl.bzieja.pandemicmodel.Model.Cell.Cell;
import pl.bzieja.pandemicmodel.Model.Cell.Path;
import pl.bzieja.pandemicmodel.Presenter.Presenter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

@Component
public class Model {
    private Cell[][] map;

    public void createModelFromImage() {

        try {
            final BufferedImage bmp = ImageIO.read(new File("src/main/resources/aghMap.bmp"));
            int height = bmp.getHeight();
            int width = bmp.getWidth();
            map = new Cell[height][width];

            Color pathColor = new Color(25, 219, 25, 255);
            Color buildingColor = new Color(141, 90, 153, 255);

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    Color currentColor = new Color(bmp.getRGB(j, i));

                    if (pathColor.equals(currentColor)) {
                        map[i][j] = new Path(currentColor);
                    } else if (buildingColor.equals(currentColor)) {
                        map[i][j] = new Building(currentColor);
                    } else {
                        map[i][j] = new Background(currentColor);
                    }
                }
            }
            System.out.println("Model loaded!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Cell[][] getMap() {
        return map;
    }
}
