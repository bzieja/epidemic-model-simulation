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
        view.moveUp();
    }

    public synchronized void moveLeft(ActionEvent actionEvent) {
        view.moveLeft();
    }

    public synchronized void moveRight(ActionEvent actionEvent) {
        view.moveRight();
    }

    public synchronized void moveDown(ActionEvent actionEvent) {
        view.moveDown();
    }

    public synchronized void zoomIn(ActionEvent actionEvent) {
        view.zoomIn();
    }

    public synchronized void zoomOut(ActionEvent actionEvent) {
        view.zoomOut();
    }

    public void goWork(ActionEvent actionEvent) {
        model.workersToWork();
        Disposable disposable = Observable
                .interval(1, 300, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.computation())
                .doOnNext(tick -> {
                    model.moveWorkers();
                    model.workersGoAroundBuildingIfAreAtDestinationPoint();
                })
                .observeOn(JavaFxScheduler.platform())
                .subscribe(t -> view.generateViewForWorkersOnly());

        stopButton.setOnAction(e -> disposable.dispose());
    }

    public void goLunch(ActionEvent actionEvent) {

        model.workersToLunch();
        Disposable disposable = Observable
                .interval(1, 300, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.computation())
                .doOnNext(tick -> {
                    model.moveWorkers();
                    model.workersGoAroundBuildingIfAreAtDestinationPoint();
                })
                .observeOn(JavaFxScheduler.platform())
                .subscribe(t -> view.generateViewForWorkersOnly());

        stopButton.setOnAction(e -> disposable.dispose());
    }


    public void goBackHome(ActionEvent actionEvent) {
        model.workersGoBackHome();
        Disposable disposable = Observable
                .interval(1, 300, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.computation())
                .doOnNext(tick -> {
                    model.moveWorkers();
                })
                .observeOn(JavaFxScheduler.platform())
                .subscribe(t -> view.generateViewForWorkersOnly());
        stopButton.setOnAction(e -> disposable.dispose());
    }

    public void stopSimulation(ActionEvent actionEvent) {
    }
}
