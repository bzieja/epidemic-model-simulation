package pl.bzieja.pandemicmodel;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PandemicModelApplication {

    public static void main(String[] args) {
        //SpringApplication.run(PandemicModelApplication.class, args);
        Application.launch(JavaFXApplication.class, args);
    }

}
