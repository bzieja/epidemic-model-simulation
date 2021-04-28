package pl.bzieja.pandemicmodel.presenter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bzieja.pandemicmodel.model.Model;
import pl.bzieja.pandemicmodel.model.ModelInitializer;
import pl.bzieja.pandemicmodel.view.View;

import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;

@Component
public class Presenter implements Initializable {
    public Button loadImageButton;
    public Canvas canvasID;
    EventHandler<MouseEvent> loadImageEvent;
    Model model;
    ModelInitializer modelInitializer;
    View view;

    @Autowired
    public Presenter(Model model, View view, ModelInitializer modelInitializer) {
        this.model = model;
        this.view = view;
        this.modelInitializer = modelInitializer;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        canvasID.setVisible(true);
        modelInitializer.createModelFromImage();
        //view.generateView(canvasID);
        view.setCanvas(canvasID);
        view.generateView();
        System.out.println("End of initialization!");
    }

    public void loadImage(ActionEvent actionEvent) {
        modelInitializer.createModelFromImage();
    }

    public void moveUp(ActionEvent actionEvent) {
        canvasID.getGraphicsContext2D().clearRect(0, 0, canvasID.getWidth(), canvasID.getHeight());
        view.moveUp();
        view.generateView();
    }

    public void moveLeft(ActionEvent actionEvent) {
        canvasID.getGraphicsContext2D().clearRect(0, 0, canvasID.getWidth(), canvasID.getHeight());
        view.moveLeft();
        view.generateView();
    }

    public void moveRight(ActionEvent actionEvent) {
        canvasID.getGraphicsContext2D().clearRect(0, 0, canvasID.getWidth(), canvasID.getHeight());
        view.moveRight();
        view.generateView();
    }

    public void moveDown(ActionEvent actionEvent) {
        canvasID.getGraphicsContext2D().clearRect(0, 0, canvasID.getWidth(), canvasID.getHeight());
        view.moveDown();
        view.generateView();
    }

    public void zoomIn(ActionEvent actionEvent) {
        view.zoomIn();
        view.generateView();
    }

    public void zoomOut(ActionEvent actionEvent) {
        view.zoomOut();
        view.generateView();
    }

    public void startSimulation(ActionEvent actionEvent) {


//        Executors.newFixedThreadPool(1).execute(() ->{
//            while (grainMap.hasEmptyCells()) {
//                grainMap.nextStep();
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e)  {
//                    e.printStackTrace();
//                }
//                Platform.runLater(canvasPrinter::generateView);
//            }
//        });

        Executors.newFixedThreadPool(1).execute(() -> {
            while(!model.areAllWorkersAtTheirDestinationPoints()) {
                model.moveWorkers();
//
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                Platform.runLater(view::generateView);


            }
        });

    }


    public void stopSimulation(ActionEvent actionEvent) {
    }
}
