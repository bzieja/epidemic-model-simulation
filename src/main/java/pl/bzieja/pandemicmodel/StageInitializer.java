package pl.bzieja.pandemicmodel;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StageInitializer implements ApplicationListener<JavaFXApplication.StageReadyEvent> {
    @Value("classpath:/panel.fxml")
    private Resource panelResource;
    private String applicationTitle;
    private ApplicationContext applicationContext;

    public StageInitializer(@Value("${spring.application.ui.title}") String applicationTitle, ApplicationContext applicationContext) {
        this.applicationTitle = applicationTitle;
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(JavaFXApplication.StageReadyEvent event) {
        try {
            FXMLLoader fxmlLoader  =new FXMLLoader(panelResource.getURL());
            fxmlLoader.setControllerFactory(aClass -> applicationContext.getBean(aClass));
            Parent parent = fxmlLoader.load();

            Stage primaryStage = event.getStage();
            primaryStage.setScene(new Scene(parent, 766, 440));
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
