package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import jnr.ffi.annotations.In;

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
    private Graph graph = new Graph();
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
        if(gR.isSelected())
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("graphRepresentation.fxml"));
            Parent root = loader.load();
            GraphRepresentationController obj = loader.getController();
            obj.setGraph(graph,numberOfVertexes,numberOfEdges,graphType);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Graph Representations");
            stage.show();
        }
        else if(gC.isSelected())
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("graphColoring.fxml"));
            Parent root = loader.load();
            GraphColoring obj = loader.getController();
            obj.getGraph(graph,numberOfVertexes,numberOfEdges,graphType);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Graph Coloring");
            stage.show();
        }
        else if(flury.isSelected())
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fluery'sAlgorithm.fxml"));
            Parent root = loader.load();
            FlueryAlgorithm obj = loader.getController();
            obj.getGraph(graph,graphType);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Fluery's Algorithm");
            stage.show();
        }
        else if(MST.isSelected())
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("minimumSpanningTree.fxml"));
            Parent root = loader.load();
            MinimumSpanningTree obj = loader.getController();
            obj.getGraph(graph,numberOfEdges,graphType);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Minimum Spanning Tree");
            stage.show();
        }


    }
    public void readGraph() throws FileNotFoundException {
        Scanner input = new Scanner(new File("graph.txt"));
        ArrayList<String>content = new ArrayList<>();
        while(input.hasNext())
        {
            String line  = input.nextLine();
            content.add(line);
        }
        input.close();
        numberOfVertexes = Integer.parseInt(content.get(0));
        numberOfEdges = Integer.parseInt(content.get(1));
        graphType = content.get(2);
        String[] vertexes = content.get(3).split(",");
        for(int i=0;i<numberOfVertexes;i++)
        {
            graph.addVertex(vertexes[i]);
        }
        if(graphType.equals("Directed"))
        {
            for(int i=0;i<numberOfEdges;i++)
            {
                String[] edge = content.get(i+4).split(",");
                graph.addDirectedEdge(edge[0],Integer.parseInt(edge[1]),edge[2],edge[3]);
            }

        }
        else if(graphType.equals("Undirected"))
        {
            for(int i=0;i<numberOfEdges;i++)
            {
                String[] edge = content.get(i+4).split(",");
                graph.addUndirectedEdge(edge[0],Integer.parseInt(edge[1]),edge[2],edge[3]);
            }
        }
    }

}