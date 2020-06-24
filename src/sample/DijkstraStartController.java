package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class DijkstraStartController {
    public ComboBox start;
    private Graph graph;
    public void setGraph(Graph graph){
        this.graph = graph;
        ArrayList<String> verticesName = new ArrayList<>();
        for(Vertex vertex : graph.getGraphVertices()){
            verticesName.add(vertex.getVertexName());
        }
        start.getItems().addAll(verticesName);

    }

    public void run(ActionEvent actionEvent) throws IOException {
        String startV = (String) start.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dijkstra.fxml"));
        Parent root = loader.load();
        DijkstraController obj = loader.getController();
        Stage stage = new Stage();
        obj.getGraph(graph, startV);
        stage.setScene(new Scene(root));
        stage.setTitle("Dijkstra");
        stage.show();
        Thread thread = new Thread(obj);
        thread.start();
    }
}
