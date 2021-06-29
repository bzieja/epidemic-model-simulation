package pl.bzieja.pandemicmodel.presenter;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bzieja.pandemicmodel.model.AppConfig;
import pl.bzieja.pandemicmodel.model.Model;
import pl.bzieja.pandemicmodel.model.ModelInitializer;
import pl.bzieja.pandemicmodel.model.events.Event;
import pl.bzieja.pandemicmodel.model.events.EventContainer;
import pl.bzieja.pandemicmodel.view.View;

import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;


@Component
public class Presenter implements Initializable {

    public Canvas canvasID;
    public Button stopButton;
    public Button startButton;
    @FXML public Label timerLabelID;
    public Button startSimulationButton;
    public Label dayLabelID;
    EventHandler<MouseEvent> loadImageEvent;
    Model model;
    ModelInitializer modelInitializer;
    View view;
    EventContainer eventContainer;

    @Autowired
    public Presenter(Model model, View view, ModelInitializer modelInitializer, EventContainer eventContainer) {
        this.model = model;
        this.view = view;
        this.modelInitializer = modelInitializer;
        this.eventContainer = eventContainer;
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

//        Event workEvent = new WorkEvent();
//        workEvent.start();
        setMorningOnTheClock();
        model.sendPartOfWorkersToWorkFromHome();
        Disposable disposable = Observable
                .interval(1, AppConfig.ITERATION_TIME, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.computation())
                .doOnNext(tick -> {
                    model.moveWorkers();
                    model.workersGoAroundBuildingIfAreAtDestinationPoint();

                })
                .observeOn(JavaFxScheduler.platform())
                .subscribe(t -> {
                    view.generateViewForWorkersOnly();
                    clockTick();
                });

        stopButton.setOnAction(e -> disposable.dispose());
    }

    public void goLunch(ActionEvent actionEvent) {

        model.workersToLunch();
        Disposable disposable = Observable
                .interval(1, AppConfig.ITERATION_TIME, TimeUnit.MILLISECONDS)
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
        model.setSpawnAsADestinationPointForEachWorker();
        Disposable disposable = Observable
                .interval(1, AppConfig.ITERATION_TIME, TimeUnit.MILLISECONDS)
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

    public void startSimulation(ActionEvent actionEvent) {

        setMorningOnTheClock();
        addEventListenersToClock();
        Disposable disposable = Observable
                .interval(1, AppConfig.ITERATION_TIME, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.computation())
                .doOnNext(tick -> {
                    model.moveWorkers();
                    model.workersGoAroundBuildingIfAreAtDestinationPoint();
                })
                .observeOn(JavaFxScheduler.platform())
                .subscribe(t -> {
                    view.generateViewForWorkersOnly();
                    clockTick();
                });

        stopButton.setOnAction(e -> disposable.dispose());

    }

    private void addEventListenersToClock() {

        timerLabelID.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                Event event = eventContainer.getHoursToEventsMap().get(newValue);
                if (event != null) {
                    event.start();
                    System.out.println(event.getClass() + " time: " + timerLabelID.getText());
                    Platform.runLater(view::generateRawViewMap);
                }
            }
        });
}

//    private void addMorningRoutineListener() {
//
//        int minuteOfEvent = 50 / AppConfig.NUMBER_OF_GROUPS_GOING_TO_WORK;
//
//        for (int i = 1; i <= AppConfig.NUMBER_OF_GROUPS_GOING_TO_WORK; i++) {
//            String minuteOfEventAsString = (i * minuteOfEvent) < 10 ? "0" + (i * minuteOfEvent) : String.valueOf((i * minuteOfEvent));
//            String eventTime = "07:" + minuteOfEventAsString + ":00";
//            System.out.println("eventTime = " + eventTime);
//
//            timerLabelID.textProperty().addListener(new ChangeListener<String>() {
//                @Override
//                public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
//                    if (newValue.equals(eventTime)) {
//                        model.sendPartOfWorkersToWork();
//                        //Platform.runLater(view::generateNewView);
//                        System.out.println("Part of workers is going to work! Time: " + newValue);
//                    }
//                }
//            });
//        }
//    }

    public void setMorningOnTheClock() {
        var lt = LocalTime.of(7,0,0);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
        timerLabelID.setText(lt.format(format));
    }

    private void clockTick() {
        String dataAsString = timerLabelID.getText();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
        var lt = LocalTime.parse(dataAsString, format);
        if (lt.equals(LocalTime.parse("18:00:00", format))) {
            increaseDayNumber();
        }
        timerLabelID.setText(lt.plusSeconds(AppConfig.TIME_STEP_IN_SIMULATION_CLOCK).format(format));
    }

    private void increaseDayNumber() {
        var dayNumber = Integer.parseInt(dayLabelID.getText());
        dayLabelID.setText(String.valueOf(dayNumber + 1));
    }
}
