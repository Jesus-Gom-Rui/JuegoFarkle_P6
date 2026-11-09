package org.UABC.farkle;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {

        HelloController controller = new HelloController();

        Scene scene = new Scene(controller.crearVista(), 650, 550);

        stage.setTitle("Farkle Game");
        stage.setScene(scene);
        stage.show();
    }
}