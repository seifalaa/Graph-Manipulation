package sample;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;

public class EnterGraphController {

    public RadioButton directed = new RadioButton();
    public RadioButton undirected = new RadioButton();
    public TextField edgeName;
    public TextField edgeFrom;
    public TextField edgeTo;
    public Label edgeAdded;
    public TextField numberOfVertexesTxtField;
    public TextField numberOfEdgesTxtField;
    public Button addEdgeBtn;
    public TextField vertexName;
    public Button addVertexBtn;
    public Label vertexAdded;
    public TextField edgeWeight;
    public Label dataEnteredLbl;
    public Button enterDataBtn;
    public ToggleGroup toggleGroup;
    public Button chooseOperationBtn;
    public Label graphEntered;
    public Button EnterGraphBtn;
    private int numberOfVertexes;
    private int numberOfEdges;
    private ArrayList<EdgeForTabel> edges = new ArrayList<>();
    private String graphType;
    private int edgeCounter = 0;
    private int vertexCounter = 0;
    private ArrayList<String> vertexes = new ArrayList<>();

    private void getData() {


    }

    public void addEdge(ActionEvent actionEvent) {
        edges.add(new EdgeForTabel(edgeName.getText(), edgeWeight.getText(), edgeFrom.getText(), edgeTo.getText()));
        edgeName.setText("");
        edgeFrom.setText("");
        edgeTo.setText("");
        edgeWeight.setText("");
        edgeAdded.setOpacity(1);
        FadeTransition fadeout = new FadeTransition(Duration.millis(1500));
        fadeout.setNode(edgeAdded);
        fadeout.setFromValue(1);
        fadeout.setToValue(0);
        fadeout.setCycleCount(1);
        fadeout.setAutoReverse(false);
        fadeout.playFromStart();
        edgeCounter++;

        if (edgeCounter == numberOfEdges) {
            addEdgeBtn.setDisable(true);
        }

    }

    public void drawGraph(ActionEvent actionEvent) throws InterruptedException {
        try {
            FileWriter writer = new FileWriter("graph.txt");
            writer.write(numberOfVertexes + "\n");
            writer.write(numberOfEdges + "\n");
            writer.write(graphType + "\n");
            for (int i = 0; i < numberOfVertexes; i++) {
                writer.write(vertexes.get(i) + ",");
            }
            writer.write("\n");
            for (int i = 0; i < numberOfEdges; i++) {
                EdgeForTabel edge = edges.get(i);
                writer.write(edge.getName() + "," + edge.getWeight() + "," + edge.getFrom() + "," + edge.getTo() + "\n");
            }
            writer.close();
            graphEntered.setOpacity(1);
            FadeTransition fadeout = new FadeTransition(Duration.millis(1500));
            fadeout.setNode(graphEntered);
            fadeout.setFromValue(1);
            fadeout.setToValue(0);
            fadeout.setCycleCount(1);
            fadeout.setAutoReverse(false);
            fadeout.playFromStart();
            EnterGraphBtn.setDisable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNumberOfVertexes() {
        return numberOfVertexes;
    }


    public int getNumberOfEdges() {
        return numberOfEdges;
    }


    public ArrayList<EdgeForTabel> getEdges() {
        return edges;
    }

    public String getGraphType() {
        return graphType;
    }

    public void addVertex(ActionEvent actionEvent) {
        vertexes.add(vertexName.getText());
        vertexName.setText("");
        vertexAdded.setOpacity(1);
        FadeTransition fadeout = new FadeTransition(Duration.millis(1500));
        fadeout.setNode(vertexAdded);
        fadeout.setFromValue(1);
        fadeout.setToValue(0);
        fadeout.setCycleCount(1);
        fadeout.setAutoReverse(false);
        fadeout.playFromStart();
        vertexCounter++;

        if (vertexCounter == numberOfVertexes) {
            addVertexBtn.setDisable(true);
        }
    }

    public void EnterData(ActionEvent actionEvent) {
        ToggleGroup toggleGroup = new ToggleGroup();
        directed.setToggleGroup(toggleGroup);
        undirected.setToggleGroup(toggleGroup);
        numberOfVertexes = Integer.parseInt(numberOfVertexesTxtField.getText());
        numberOfEdges = Integer.parseInt(numberOfEdgesTxtField.getText());
        if (directed.isSelected()) {
            graphType = "Directed";
        } else if (undirected.isSelected()) {
            graphType = "Undirected";
        }
        if (numberOfEdges == 0) {
            addEdgeBtn.setDisable(true);
        }
        dataEnteredLbl.setOpacity(1);
        FadeTransition fadeout = new FadeTransition(Duration.millis(1500));
        fadeout.setNode(dataEnteredLbl);
        fadeout.setFromValue(1);
        fadeout.setToValue(0);
        fadeout.setCycleCount(1);
        fadeout.setAutoReverse(false);
        fadeout.playFromStart();
        enterDataBtn.setDisable(true);
        chooseOperationBtn.setDisable(false);
    }

    public void chooseOperation(ActionEvent actionEvent) throws IOException {
        Parent nextScene = FXMLLoader.load(getClass().getResource("chooseOperation.fxml"));
        Scene scene = new Scene(nextScene);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setTitle("Choose Operation");
        window.setScene(scene);
        window.show();

    }
}
