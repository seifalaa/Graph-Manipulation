package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ChooseOperationController {

    public RadioButton gC = new RadioButton();
    public RadioButton gR = new RadioButton();
    public RadioButton flury = new RadioButton();
    public RadioButton MST = new RadioButton();
    public RadioButton MHC = new RadioButton();
    public RadioButton MHPC = new RadioButton();
    public RadioButton MaximumFlow;
    public RadioButton Dijkstra;
    private Graph graph = new Graph(true);
    private int numberOfVertexes;
    private String graphType;
    private int numberOfEdges;

    public ChooseOperationController() throws FileNotFoundException {
        readGraph();
    }

    @FXML
    private ToggleGroup g1;

    @FXML
    void back(ActionEvent actionEvent) throws IOException {
        Parent nextScene = FXMLLoader.load(getClass().getResource("enterGraph.fxml"));
        Scene scene = new Scene(nextScene);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setTitle("Enter Graph");
        window.setScene(scene);
        window.show();

    }

    @FXML
    void choose(ActionEvent actionEvent) throws IOException {

        if (gR.isSelected()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("graphRepresentation.fxml"));
            Parent root = loader.load();
            GraphRepresentationController obj = loader.getController();
            obj.setGraph(graph, numberOfVertexes, numberOfEdges, graphType);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Graph Representations");
            stage.show();
            Thread thread = new Thread(obj);
            thread.start();
        } else if (gC.isSelected()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("graphColoring.fxml"));
            Parent root = loader.load();
            GraphColoring obj = loader.getController();
            Stage stage = new Stage();
            obj.getGraph(graph, numberOfVertexes, numberOfEdges, graphType);
            stage.setScene(new Scene(root));
            stage.setTitle("Graph Coloring");
            stage.show();
            Thread thread = new Thread(obj);
            thread.start();
        } else if (flury.isSelected()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fluery'sAlgorithm.fxml"));
            Parent root = loader.load();
            FlueryAlgorithm obj = loader.getController();
            obj.getGraph(graph, graphType);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Fluery's Algorithm");
            stage.show();
            Thread thread = new Thread(obj);
            thread.start();
        } else if (MST.isSelected()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("minimumSpanningTree.fxml"));
            Parent root = loader.load();
            MinimumSpanningTree obj = loader.getController();
            obj.getGraph(graph, numberOfEdges, graphType);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Minimum Spanning Tree");
            stage.show();
            Thread thread = new Thread(obj);
            thread.start();
        }
        else if(MHC.isSelected())
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("minimumHamiltonCircuit.fxml"));
            Parent root = loader.load();
            MinimumHamiltonCircuit obj = loader.getController();
            obj.getGraph(graph, numberOfEdges, graphType);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Minimum Hamilton Circuit");
            stage.show();
            Thread thread = new Thread(obj);
            thread.start();
        }
        else if(MHPC.isSelected())
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hamiltonCircuit.fxml"));
            Parent root = loader.load();
            HamiltonCircuit obj = loader.getController();
            obj.getGraph(graph,graphType);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Hamilton Circuit & Hamilton Path");
            stage.show();
            Thread thread = new Thread(obj);
            thread.start();
        }
        else if(MaximumFlow.isSelected()){
            if(!graph.directed){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Can't perform Maximum Flow on an undirected graph");
                a.show();
            }else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("maximumFLowStart.fxml"));
            Parent root = loader.load();
            MaximumFLowStartController obj = loader.getController();
            obj.setGraph(graph);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Maximum Flow");
            stage.show();}
        }
        else if(Dijkstra.isSelected()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dijkstraStart.fxml"));
            Parent root = loader.load();
            DijkstraStartController obj = loader.getController();
            obj.setGraph(graph);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Dijkstra");
            stage.show();
        }

    }

    public void readGraph() throws FileNotFoundException {
        Scanner input = new Scanner(new File("graph.txt"));
        ArrayList<String> content = new ArrayList<>();
        while (input.hasNext()) {
            String line = input.nextLine();
            content.add(line);
        }
        input.close();
        numberOfVertexes = Integer.parseInt(content.get(0));
        numberOfEdges = Integer.parseInt(content.get(1));
        graphType = content.get(2);
        String[] vertexes = content.get(3).split(",");
        for (int i = 0; i < numberOfVertexes; i++) {
            graph.addVertex(vertexes[i]);
        }
        if (graphType.equals("Directed")) {
            graph.setDirected(true);
            for (int i = 0; i < numberOfEdges; i++) {
                String[] edge = content.get(i + 4).split(",");
                graph.addDirectedEdge(edge[0], Integer.parseInt(edge[1]), edge[2], edge[3]);
            }

        } else if (graphType.equals("Undirected")) {
            graph.setDirected(false);
            for (int i = 0; i < numberOfEdges; i++) {
                String[] edge = content.get(i + 4).split(",");
                graph.addUndirectedEdge(edge[0], Integer.parseInt(edge[1]), edge[2], edge[3]);
            }
        }
    }


}