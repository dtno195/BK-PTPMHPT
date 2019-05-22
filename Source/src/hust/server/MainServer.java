package hust.server;

import hust.constants.ServerConfig;
import hust.service.FileStreamingService;
import hust.service.impl.FileStreamingServiceImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MainServer extends Application {
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/hust/gui/start_server.fxml"));
        primaryStage.setTitle("Server");
        primaryStage.setScene(new Scene(root, root.prefWidth(0), root.prefHeight(0)));
        primaryStage.show();
        stage = primaryStage;
    }


    public static void main(String[] args) {
        launch(args);
    }

    private static Registry registry;

    private static void startRegistry() throws RemoteException {
        // Tạo một bộ đăng ký (Registry) tại Server.
        registry = LocateRegistry.createRegistry(ServerConfig.PORT);
    }

    private static void registerObject(String name, Remote remoteObj)
            throws RemoteException, AlreadyBoundException {
        // Đăng ký đối tượng vào bộ đăng ký.
        // Nó được gắn với cái tên nào đó.
        // Client sẽ tìm trên bộ đăng ký với tên này để có thể gọi đối tượng.

        registry.bind(name, remoteObj);
        System.out.println("Registered: " + name + " -> "
                + remoteObj.getClass().getName() + "[" + remoteObj + "]");
    }

    public void runServer() throws Exception {
        stage.close();
        try {
            System.out.println("Server starting...");
            startRegistry();
            registerObject(FileStreamingService.class.getSimpleName(), new FileStreamingServiceImpl());

            // Server đã được start, và đang lắng nghe các request từ Client.
            System.out.println("Server started!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
