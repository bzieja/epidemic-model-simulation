package pl.bzieja.pandemicmodel.Presenter;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bzieja.pandemicmodel.Model.Model;
import pl.bzieja.pandemicmodel.View.View;

import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class Presenter implements Initializable {
    public Button loadImageButton;
    public Canvas canvasID;
    EventHandler<MouseEvent> loadImageEvent;
    Model model;
    View view;

    @Autowired
    public Presenter(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        canvasID.setVisible(true);
        model.createModelFromImage();
        view.generateView(canvasID);
    }

    public void loadImage(ActionEvent actionEvent) {
        model.createModelFromImage();
    }

    public void moveUp(ActionEvent actionEvent) {
        canvasID.getGraphicsContext2D().clearRect(0, 0, canvasID.getWidth(), canvasID.getHeight());
        view.moveUp();
        view.generateView(canvasID);
    }

    public void moveLeft(ActionEvent actionEvent) {
        canvasID.getGraphicsContext2D().clearRect(0, 0, canvasID.getWidth(), canvasID.getHeight());
        view.moveLeft();
        view.generateView(canvasID);
    }

    public void moveRight(ActionEvent actionEvent) {
        canvasID.getGraphicsContext2D().clearRect(0, 0, canvasID.getWidth(), canvasID.getHeight());
        view.moveRight();
        view.generateView(canvasID);
    }

    public void moveDown(ActionEvent actionEvent) {
        canvasID.getGraphicsContext2D().clearRect(0, 0, canvasID.getWidth(), canvasID.getHeight());
        view.moveDown();
        view.generateView(canvasID);
    }

    public void zoomIn(ActionEvent actionEvent) {
        view.zoomIn();
        view.generateView(canvasID);
    }

    public void zoomOut(ActionEvent actionEvent) {
        view.zoomOut();
        view.generateView(canvasID);
    }

    public Canvas getCanvasID() {
        return canvasID;
    }
}
