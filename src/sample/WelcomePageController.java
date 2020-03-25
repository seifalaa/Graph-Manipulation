package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomePageController {


    public void EnterGraph(ActionEvent actionEvent) throws IOException {

        Parent nextScene = FXMLLoader.load(getClass().getResource("enterGraph.fxml"));
        Scene scene = new Scene(nextScene);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setTitle("Enter Graph");
        window.setScene(scene);

        window.show();
    }
}
