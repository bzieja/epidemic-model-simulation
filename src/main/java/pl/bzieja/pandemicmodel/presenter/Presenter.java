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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bzieja.pandemicmodel.model.AppConfig;
import pl.bzieja.pandemicmodel.model.Model;
import pl.bzieja.pandemicmodel.model.ModelInitializer;
import pl.bzieja.pandemicmodel.model.events.Event;
import pl.bzieja.pandemicmodel.model.events.EventContainer;
import pl.bzieja.pandemicmodel.model.person.InfectionManager;
import pl.bzieja.pandemicmodel.view.View;

import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;


@Component
public class Presenter implements Initializable {

    private final Logger logger = LoggerFactory.getLogger(Presenter.class);
    public Canvas canvasID;
    public Button stopButton;
    public Button startButton;
    @FXML public Label timerLabelID;
    public Button startSimulationButton;
    public Label dayLabelID;
    public Label healthyLabelID;
    public Label asymptomaticallyIllLabelID;
    public Label quarantinedLabelID;
    public Label convalescentLabelID;
    public Label symptomaticallyIllLabelID;
    EventHandler<MouseEvent> loadImageEvent;
    Model model;
    ModelInitializer modelInitializer;
    View view;
    EventContainer eventContainer;
    InfectionManager infectionManager;

    @Autowired
    public Presenter(Model model, View view, ModelInitializer modelInitializer, EventContainer eventContainer, InfectionManager infectionManager) {
        this.model = model;
        this.view = view;
        this.modelInitializer = modelInitializer;
        this.eventContainer = eventContainer;
        this.infectionManager = infectionManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        canvasID.setVisible(true);
        modelInitializer.createModelFromImage();
        view.setCanvas(canvasID);
        view.generateNewView();
        logger.info("End of initialization!");
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

        setMorningOnTheClock();
        model.sendPartOfWorkersToWorkFromHome();
        Disposable disposable = Observable
                .interval(1, AppConfig.ONE_ITERATION_TIME_IN_MS, TimeUnit.MILLISECONDS)
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
                .interval(1, AppConfig.ONE_ITERATION_TIME_IN_MS, TimeUnit.MILLISECONDS)
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
                .interval(1, AppConfig.ONE_ITERATION_TIME_IN_MS, TimeUnit.MILLISECONDS)
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
        //bindCounters();
        addHealthStateCounterListeners();
        infectionManager.infectPatientsZero();


        Disposable disposable = Observable
                .interval(1, AppConfig.ONE_ITERATION_TIME_IN_MS, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.computation())
                .doOnNext(tick -> {
                    model.moveWorkers();
                    model.workersGoAroundBuildingIfAreAtDestinationPoint();
                    infectionManager.doTickRoutine();
                })
                .observeOn(JavaFxScheduler.platform())
                .subscribe(t -> {
                    //view.generateViewForWorkersOnly();
                    clockTick();
                });

        stopButton.setOnAction(e -> disposable.dispose());

    }

    private void addHealthStateCounterListeners() {
        logger.info("Add counter listeners");
        infectionManager.numberOfHealthWorkersProperty().addListener((observableValue, number, t1) -> Platform.runLater(() -> healthyLabelID.setText(String.valueOf(t1))));
        infectionManager.numberOfSymptomaticallyIllProperty().addListener((observableValue, number, t1) -> Platform.runLater(() -> symptomaticallyIllLabelID.setText(String.valueOf(t1))));
        infectionManager.numberOfAsymptomaticallyIllProperty().addListener((observableValue, number, t1) -> Platform.runLater(() -> asymptomaticallyIllLabelID.setText(String.valueOf(t1))));
        infectionManager.numberOfQuarantinedProperty().addListener((observableValue, number, t1) -> Platform.runLater(() -> quarantinedLabelID.setText(String.valueOf(t1))));
        infectionManager.numberOfConvalescentProperty().addListener((observableValue, number, t1) -> Platform.runLater(() -> convalescentLabelID.setText(String.valueOf(t1))));
    }


//    private void bindCounters() {
//        healthyLabelID.textProperty().bind(infectionManager.numberOfHealthWorkersProperty().asString());
//        symptomaticallyIllLabelID.textProperty().bind(infectionManager.numberOfSymptomaticallyIllProperty().asString());
//        asymptomaticallyIllLabelID.textProperty().bind(infectionManager.numberOfAsymptomaticallyIllProperty().asString());
//        quarantinedLabelID.textProperty().bind(infectionManager.numberOfQuarantinedProperty().asString());
//        convalescentLabelID.textProperty().bind(infectionManager.numberOfConvalescentProperty().asString());
//    }

    private void addEventListenersToClock() {

        timerLabelID.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                Event event = eventContainer.getHoursToEventsMap().get(newValue);
                if (event != null) {
                    event.start();
                    logger.info("{} time: {}", event.getClass(), timerLabelID.getText());
//                    System.out.println(event.getClass() + " time: " + timerLabelID.getText());
                    Platform.runLater(view::generateRawViewMap);
                }
            }
        });
    }

    public void setMorningOnTheClock() {
        logger.info("Setting morning in the clock!");

        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
        var lt = LocalTime.parse(AppConfig.THE_START_OF_THE_DAY,format);
        timerLabelID.setText(lt.format(format));
    }

    private void clockTick() {
        logger.debug("Clock tick!");

        String dataAsString = timerLabelID.getText();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
        var localTime = LocalTime.parse(dataAsString, format);
        timerLabelID.setText(localTime.plusSeconds(AppConfig.TIME_STEP_IN_SIMULATION_WORLD).format(format));

        if (localTime.getMinute() % 30 == 0 && localTime.getSecond() == 0) {   //save every 30m to file
            infectionManager.writeDataToCSV(dayLabelID.getText(), localTime);
        }

        if (localTime.equals(LocalTime.parse(AppConfig.THE_END_OF_THE_DAY, format))) { //"18:00:00"
            infectionManager.doEndOfDayInfectionSummary();
            increaseDayNumber();
            setMorningOnTheClock();
        }
    }

    private void increaseDayNumber() {
        var dayNumber = Integer.parseInt(dayLabelID.getText());
        logger.info("Increase day number. Current day: " + dayNumber + 1);
        if (dayNumber == 31) {
            System.exit(0);
        }
        dayLabelID.setText(String.valueOf(dayNumber + 1));
    }
}
