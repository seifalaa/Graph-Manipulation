package sample;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class EnterGraphController {

    public RadioButton directed = new RadioButton();
    public RadioButton undirected = new RadioButton();
    public TextField edgeName;
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
    public ComboBox endCombo;
    public ComboBox startCombo;
    public Button generateVertices;
    private int numberOfVertexes;
    private int numberOfEdges;
    private ArrayList<EdgeForTabel> edges = new ArrayList<>();
    private String graphType;
    private int edgeCounter = 0;
    private int vertexCounter = 0;
    private ArrayList<String> vertexes = new ArrayList<>();
    public void addEdge(ActionEvent actionEvent) {
        edges.add(new EdgeForTabel(edgeName.getText(), edgeWeight.getText(), (String)startCombo.getSelectionModel().getSelectedItem(),(String)endCombo.getSelectionModel().getSelectedItem()));
        edgeName.setText("");
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
            EnterGraphBtn.setDisable(false);
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
            chooseOperationBtn.setDisable(false);
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
            generateVertices.setDisable(true);
            addEdgeBtn.setDisable(false);
            if(!startCombo.getItems().isEmpty()){
                startCombo.getItems().clear();
                endCombo.getItems().clear();
            }
            startCombo.getItems().addAll(vertexes);
            endCombo.getItems().addAll(vertexes);
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
        addVertexBtn.setDisable(false);
        generateVertices.setDisable(false);
    }

    public void chooseOperation(ActionEvent actionEvent) throws IOException {
        Parent nextScene = FXMLLoader.load(getClass().getResource("chooseOperation.fxml"));
        Scene scene = new Scene(nextScene);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setOnCloseRequest(new ExitListener());
        window.setTitle("Choose Operation");
        window.setScene(scene);
        window.show();

    }
    public void generate(ActionEvent actionEvent) {
        int counter = 1;
        while(vertexCounter != numberOfVertexes){
            vertexes.add(String.valueOf(counter));
            counter++;
            vertexCounter++;
        }
        addVertexBtn.setDisable(true);
        if(!startCombo.getItems().isEmpty()){
            startCombo.getItems().clear();
            endCombo.getItems().clear();
        }
        startCombo.getItems().addAll(vertexes);
        endCombo.getItems().addAll(vertexes);
        generateVertices.setDisable(true);
        addEdgeBtn.setDisable(false);
    }
    public void undoChange(ActionEvent actionEvent) {
        if(!edges.isEmpty()){
            EdgeForTabel edge = edges.remove(edges.size()-1);
            edgeWeight.setText(edge.getWeight());
            edgeName.setText(edge.getName());
            startCombo.getSelectionModel().select(edge.getFrom());
            endCombo.getSelectionModel().select(edge.getTo());
            if(numberOfEdges == edgeCounter){

                addEdgeBtn.setDisable(false);
            }
            edgeCounter--;
        }else if(!vertexes.isEmpty()){
            String vertex = vertexes.remove(vertexes.size()-1);
            vertexName.setText(vertex);
            if(numberOfVertexes == vertexCounter){
                addVertexBtn.setDisable(false);
                generateVertices.setDisable(false);
            }
            vertexCounter--;

        }else if(numberOfEdges != 0){
            enterDataBtn.setDisable(false);
            addVertexBtn.setDisable(true);
            generateVertices.setDisable(true);
            addEdgeBtn.setDisable(true);
            EnterGraphBtn.setDisable(true);
            chooseOperationBtn.setDisable(true);
        }
    }
}
