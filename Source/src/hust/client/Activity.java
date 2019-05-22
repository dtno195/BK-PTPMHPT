package hust.client;

import hust.constants.ServerConfig;
import hust.model.RemoteItem;
import hust.service.FileStreamingService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Activity extends Application implements Initializable {

    @FXML
    public TableView<RemoteItem> table;
    public Label directory;

    public void initialize(URL location, ResourceBundle resources) {
        TableColumn<RemoteItem, Object> type = new TableColumn<RemoteItem, Object>("Type");
        type.setCellValueFactory(new PropertyValueFactory<RemoteItem, Object>("type"));
        TableColumn<RemoteItem, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.setMinWidth(300);

        try {
            refresh();
        } catch (Exception e) {
            System.out.println(e);
        }

        table.getColumns().addAll(type, name);
    }

    private static Stage stage;
    private static FileStreamingService service;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/hust/gui/main_activity.fxml"));
        primaryStage.setTitle("File Streaming RMI");
        primaryStage.setScene(new Scene(root, root.prefWidth(0), root.prefHeight(0)));
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {
        // Search the registry in the specific Host, Port.
        String HOST = args[0];
        Registry registry = LocateRegistry.getRegistry(HOST, ServerConfig.PORT);

        // Lookup WeatherService in the Registry.
        service = (FileStreamingService) registry.lookup(FileStreamingService.class.getSimpleName());

        stage = new Stage();
        new Activity().start(stage);
    }

    public void delete() throws Exception {
        RemoteItem r = table.getSelectionModel().getSelectedItem();
        if (r.getType().equals("FILE"))
            System.out.println(service.delete(r.getName()));
        refresh();
    }

    public void createFolder() throws Exception {
        String folderName = JOptionPane.showInputDialog(null, "Enter new folder name");
        System.out.println(service.createFolder(folderName));
        refresh();
    }

    public void upload() throws Exception {
        FileChooser ff = new FileChooser();
        ff.setTitle("Select");
        ff.setInitialDirectory(new File("./"));
        File f = ff.showOpenDialog(stage);

        if (f != null) {
            try {
                FileInputStream fin = new FileInputStream(f);
                byte[] bytes = new byte[(int) f.length()];
                fin.read(bytes);
                fin.close();

                String name = f.toString();
                System.out.println(service.upload(bytes, name.substring(name.lastIndexOf("\\"))));
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        refresh();
    }

    public void goToRoot() throws Exception {
        service.goToRoot();
        refresh();
    }

    public void download() throws RemoteException {
        RemoteItem r = table.getSelectionModel().getSelectedItem();

        if (r.getType().equals("FILE")) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("SAVE");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("download", "*" + r.getName().substring(r.getName().lastIndexOf("."))));
            fileChooser.setInitialDirectory(new File("./"));

            File f = fileChooser.showSaveDialog(stage);

            byte[] bytes = service.download(r.getName());

            try {
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bytes);
                fos.close();

                System.out.println(bytes.length);

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void update() throws Exception {
        RemoteItem r = table.getSelectionModel().getSelectedItem();

        if (r.getType().equals("FILE")) {
            byte[] bytes = service.download(r.getName());

            FileOutputStream fos = new FileOutputStream(r.getName());
            fos.write(bytes);
            fos.close();

            Desktop d = Desktop.getDesktop();
            d.open(new File(r.getName()));

            new Action().requestConfirm(r.getName());
        }
    }

    public void forward() throws Exception {
        RemoteItem r = table.getSelectionModel().getSelectedItem();
        if (r.getType().equals("DIRECTORY")) {
            service.forward(r.getName());
            refresh();
        }
    }

    public void back() throws Exception {
        service.back();
        refresh();
    }

    public void yes(String name) {
        try {
            File f = new File(name);
            byte[] bytes;
            FileInputStream fis = new FileInputStream(f);
            bytes = new byte[(int) f.length()];
            fis.read(bytes);
            fis.close();

            System.out.println(service.upload(bytes, name));
            while (!f.delete()) ;
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    void refresh() throws Exception {
        ObservableList<RemoteItem> list = FXCollections.observableArrayList();
        ArrayList<String[]> list2 = service.list();
        directory.setText(list2.get(0)[0]);

        for (int i = 1; i < list2.size(); i++) {
            list.add(new RemoteItem(list2.get(i)[0], list2.get(i)[1]));
        }

        table.getItems().clear();
        table.setItems(list);
    }

}
