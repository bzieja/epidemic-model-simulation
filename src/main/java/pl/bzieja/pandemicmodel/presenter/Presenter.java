package pl.bzieja.pandemicmodel.presenter;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bzieja.pandemicmodel.core.AppConfig;
import pl.bzieja.pandemicmodel.core.model.Model;
import pl.bzieja.pandemicmodel.core.model.ModelInitializer;
import pl.bzieja.pandemicmodel.core.events.Event;
import pl.bzieja.pandemicmodel.core.events.EventContainer;
import pl.bzieja.pandemicmodel.core.person.InfectionManager;
import pl.bzieja.pandemicmodel.view.View;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;


@Component
public class Presenter implements Initializable {

    private final Logger logger = LoggerFactory.getLogger(Presenter.class);
    public Canvas canvasID;
    public Button startButton;
    @FXML public Label timerLabelID;
    public Button startSimulationButton;
    public Label dayLabelID;
    public Label healthyLabelID;
    public Label asymptomaticallyIllLabelID;
    public Label quarantinedLabelID;
    public Label convalescentLabelID;
    public Label symptomaticallyIllLabelID;
    public TextField infectionFactorInsideId;
    public TextField infectionFactorOutsideId;
    public CheckBox areMasksId;
    public CheckBox isVaccineId;
    public TextField vaccineEffectivenessId;
    public Button stopSimulationButton;
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
        logger.info("Setting initial conditions in the AppConfig");
        setInitialConditions();
        canvasID.setVisible(true);
        modelInitializer.initialize();
        view.setCanvas(canvasID);
        view.generateNewView();
        logger.info("Number of generated workers: {}", model.getWorkers().size());
        logger.info("End of initialization!");
    }

    private void setInitialConditions() {
        double insideFactor = Double.parseDouble(infectionFactorInsideId.getText());
        double outsideFactor = Double.parseDouble(infectionFactorOutsideId.getText());
        AppConfig.setProbabilityOfBeingInfectedByOnePersonInTheBuilding(areMasksId.isSelected() ? insideFactor * 0.2 : insideFactor);
        AppConfig.setProbabilityOfBeingInfectedByOnePersonOutside(areMasksId.isSelected() ? outsideFactor * 0.2 : insideFactor);

        AppConfig.setIsVaccination(isVaccineId.isSelected());
        AppConfig.setVaccineEffectiveness(Double.parseDouble(vaccineEffectivenessId.getText()));
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

    public void stopSimulation(ActionEvent actionEvent) {
        logger.info("Stop simulation");
        System.exit(0);
    }

    public void startSimulation(ActionEvent actionEvent) {

        setMorningOnTheClock();
        addEventListenersToClock();
        addHealthStateCounterListeners();
        infectionManager.infectPatientsZero();

        Observable.interval(1, AppConfig.ONE_ITERATION_TIME_IN_MS, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.computation())
                .doOnNext(tick -> {
                    model.moveWorkers();
                    model.workersGoAroundBuildingIfAreAtDestinationPoint();
                    infectionManager.doTickRoutine();
                })
                .observeOn(JavaFxScheduler.platform())
                .subscribe(t -> {
                    if (AppConfig.SHOULD_RENDER_VIEW) {
                        view.generateViewForWorkersOnly();
                    }
                    clockTick();
                });

    }

    private void addHealthStateCounterListeners() {
        logger.info("Add counter listeners");
        infectionManager.numberOfHealthWorkersProperty().addListener((observableValue, number, t1) -> Platform.runLater(() -> healthyLabelID.setText(String.valueOf(t1))));
        infectionManager.numberOfSymptomaticallyIllProperty().addListener((observableValue, number, t1) -> Platform.runLater(() -> symptomaticallyIllLabelID.setText(String.valueOf(t1))));
        infectionManager.numberOfAsymptomaticallyIllProperty().addListener((observableValue, number, t1) -> Platform.runLater(() -> asymptomaticallyIllLabelID.setText(String.valueOf(t1))));
        infectionManager.numberOfQuarantinedProperty().addListener((observableValue, number, t1) -> Platform.runLater(() -> quarantinedLabelID.setText(String.valueOf(t1))));
        infectionManager.numberOfConvalescentProperty().addListener((observableValue, number, t1) -> Platform.runLater(() -> convalescentLabelID.setText(String.valueOf(t1))));
    }

    private void addEventListenersToClock() {

        timerLabelID.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                Event event = eventContainer.getHoursToEventsMap().get(newValue);
                if (event != null) {
                    event.start();
                    logger.info("{} time: {}", event.getClass(), timerLabelID.getText());
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

        if (localTime.getHour() == 7 && localTime.getMinute() == 0 && localTime.getSecond() == 0) {   //save every morning
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
        logger.info("Increase day number. Current day: {}", dayNumber + 1);
        if (dayNumber == AppConfig.DAYS_OF_SIMULATION) {
            System.exit(0);
        }
        dayLabelID.setText(String.valueOf(dayNumber + 1));
    }
}
