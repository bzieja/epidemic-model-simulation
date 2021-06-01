package pl.bzieja.pandemicmodel.presenter;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
//import io.reactivex.rxjava3.annotations.NonNull;
//import io.reactivex.rxjava3.core.*;
//import io.reactivex.rxjava3.disposables.Disposable;
//import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
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
        view.setCanvas(canvasID);
        view.generateNewView();
        System.out.println("End of initialization!");
    }

    public synchronized void moveUp(ActionEvent actionEvent) {
        canvasID.getGraphicsContext2D().clearRect(0, 0, canvasID.getWidth(), canvasID.getHeight());
        view.moveUp();
        view.generateNewView();
    }

    public synchronized void moveLeft(ActionEvent actionEvent) {
        canvasID.getGraphicsContext2D().clearRect(0, 0, canvasID.getWidth(), canvasID.getHeight());
        view.moveLeft();
        view.generateNewView();
    }

    public synchronized void moveRight(ActionEvent actionEvent) {
        canvasID.getGraphicsContext2D().clearRect(0, 0, canvasID.getWidth(), canvasID.getHeight());
        view.moveRight();
        view.generateNewView();
    }

    public synchronized void moveDown(ActionEvent actionEvent) {
        canvasID.getGraphicsContext2D().clearRect(0, 0, canvasID.getWidth(), canvasID.getHeight());
        view.moveDown();
        view.generateNewView();
    }

    public synchronized void zoomIn(ActionEvent actionEvent) {
        view.zoomIn();
        view.generateNewView();
    }

    public synchronized void zoomOut(ActionEvent actionEvent) {
        view.zoomOut();
        view.generateNewView();
    }

    public synchronized void goWork(ActionEvent actionEvent) {

        model.workersToWork();
//        Disposable disposable = Observable
//                .interval(1, 700, TimeUnit.MILLISECONDS)
//                //.observeOn(Schedulers.computation())
//                .doOnNext(tick -> {
//                    model.moveWorkers();
//                    model.workersGoAroundBuildingIfAreAtDestinationPoint();
//                    model.actualizeColorOfCells();
//                    Platform.runLater(() -> view.drawCells());
//                });
//                //.observeOn(JavaFxScheduler.platform())//javafx scheduler
                //.sample(500, TimeUnit.MILLISECONDS)
                //.subscribe(Platform.runLater(() -> view.drawCells()););

              Disposable disposable = Observable
              .interval(1, 1000, TimeUnit.MILLISECONDS)
              .forEach(t -> {
                  model.moveWorkers();
                  model.workersGoAroundBuildingIfAreAtDestinationPoint();
                  model.actualizeColorOfCells();
                  Platform.runLater(view::drawCells);
              });

        stopButton.setOnAction(e -> disposable.dispose());
//        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
//            Platform.runLater(view::generateView);
//            model.moveWorkers();
//        }, 1, 2000, TimeUnit.MILLISECONDS);

      //  stopButton.setOnAction(e -> future.cancel(true));


    }

    public void goLunch(ActionEvent actionEvent) {

        model.workersToLunch();
        Disposable disposable = Observable
                .interval(1, 1000, TimeUnit.MILLISECONDS)
                .forEach(t -> {
                    model.moveWorkers();
                    model.workersGoAroundBuildingIfAreAtDestinationPoint();
                    Platform.runLater(view::generateNewView);
                });
        stopButton.setOnAction(e -> disposable.dispose());
    }

    public void goBackHome(ActionEvent actionEvent) {
        model.workersGoBackHome();
        Disposable disposable = Observable
                .interval(1, 600, TimeUnit.MILLISECONDS)
                .forEach(t -> {
                    model.moveWorkers();
                    Platform.runLater(view::generateNewView);
                });
        stopButton.setOnAction(e -> disposable.dispose());
    }

    public void stopSimulation(ActionEvent actionEvent) {
    }
}
