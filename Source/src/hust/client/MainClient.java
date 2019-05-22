package hust.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.InetAddress;

public class MainClient extends Application {
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/hust/gui/start_client.fxml"));
        primaryStage.setTitle("Client");
        primaryStage.setScene(new Scene(root, root.prefWidth(0), root.prefHeight(0)));
        primaryStage.show();
        stage = primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void runClient() throws Exception {
        try {
            String hostName = JOptionPane.showInputDialog(null, "Enter the host name");
            InetAddress ip = InetAddress.getByName(hostName);

            System.out.println(ip.getHostAddress());
            stage.close();

            Activity.main(new String[]{ip.getHostAddress()});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
