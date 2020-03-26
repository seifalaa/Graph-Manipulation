package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.python.util.PythonInterpreter;

import java.io.File;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("welcomePage.fxml"));
        primaryStage.setTitle("Graph Manipulation");
        primaryStage.setScene(new Scene(root, 600, 400));
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
        File file = new File("graph.png");
        File file1 = new File("coloredGraph.png");
        File file2 = new File("MST.png");
        boolean delete = file.delete();
        boolean delete1 = file1.delete();
        boolean delete2 = file2.delete();
        if (delete && delete1 && delete2) {
            System.out.println("files deleted successfully");
            System.exit(0);
        }

    }
}
