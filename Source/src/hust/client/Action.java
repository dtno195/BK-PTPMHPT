package hust.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;

public class Action extends Application {

    public void start(Stage primaryStage) {
    }

    private static Stage stage;
    private static String name;

    public void requestConfirm(String name) throws Exception {
        this.name = name;

        Pane p = FXMLLoader.load(getClass().getResource("/hust/gui/confirm.fxml"));
        stage = new Stage();
        stage.setScene(new Scene(p, 200, 100));
        stage.show();
    }

    public void yes() {
        new Activity().yes(name);
        stage.close();
    }

    public void no() {
        while (!new File(name).delete()) ;
        stage.close();
    }

}
