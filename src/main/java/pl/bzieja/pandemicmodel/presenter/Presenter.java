package pl.bzieja.pandemicmodel.presenter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


@Component
public class Presenter implements Initializable {
    public Button loadImageButton;
    public Canvas canvasID;
    public Button stopButton;
    public Button startButton;
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

    public synchronized void loadImage(ActionEvent actionEvent) {
        modelInitializer.createModelFromImage();
    }

    public synchronized void moveUp(ActionEvent actionEvent) {
        canvasID.getGraphicsContext2D().clearRect(0, 0, canvasID.getWidth(), canvasID.getHeight());
        view.moveUp();
        view.generateView();
    }

    public synchronized void moveLeft(ActionEvent actionEvent) {
        canvasID.getGraphicsContext2D().clearRect(0, 0, canvasID.getWidth(), canvasID.getHeight());
        view.moveLeft();
        view.generateView();
    }

    public synchronized void moveRight(ActionEvent actionEvent) {
        canvasID.getGraphicsContext2D().clearRect(0, 0, canvasID.getWidth(), canvasID.getHeight());
        view.moveRight();
        view.generateView();
    }

    public synchronized void moveDown(ActionEvent actionEvent) {
        canvasID.getGraphicsContext2D().clearRect(0, 0, canvasID.getWidth(), canvasID.getHeight());
        view.moveDown();
        view.generateView();
    }

    public synchronized void zoomIn(ActionEvent actionEvent) {
        view.zoomIn();
        view.generateView();
    }

    public synchronized void zoomOut(ActionEvent actionEvent) {
        view.zoomOut();
        view.generateView();
    }

    public synchronized void startSimulation(ActionEvent actionEvent) {

//        Disposable disposable = Observable
//              .interval(1, 500, TimeUnit.MILLISECONDS)
//              .forEach(t -> {
//                  model.moveWorkers();
//                  Platform.runLater(view::generateView);
//              });

                final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
                ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
                    model.moveWorkers();
                    Platform.runLater(view::generateView);
                }, 1, 500, TimeUnit.MILLISECONDS);

                stopButton.setOnAction(e -> future.cancel(true));

    }


    public synchronized void stopSimulation(ActionEvent actionEvent) {
        startButton.setOnAction(this::startSimulation);
    }
}
