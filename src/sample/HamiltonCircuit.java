package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class HamiltonCircuit implements Runnable {

    public Label generationGraphLabel;
    @FXML
    private ImageView graphImage = new ImageView();

    @FXML
    private Label hamiltonCircuitLbl = new Label();

    @FXML
    private Label hamiltonPathLbl = new Label();



    void showGraph() {
        graphImage.setImage(new Image(new File("graph.png").toURI().toString()));
        hamiltonCircuitLbl.setOpacity(1);
        hamiltonPathLbl.setOpacity(1);

    }
    public void getGraph(Graph graph, String graphType) {

        if (graphType.equals("Directed")) {
            ArrayList<String>results = graph.diFullHamilton();
            hamiltonCircuitLbl.setText(results.get(0));
            hamiltonPathLbl.setText(results.get(1));
        } else if (graphType.equals("Undirected")) {
            ArrayList<String>results = graph.fullHamilton();
            hamiltonCircuitLbl.setText(results.get(0));
            hamiltonPathLbl.setText(results.get(1));
        }

    }

    public void executeDrawingProgram() {
        try {
            Process process = Runtime.getRuntime().exec("main.exe");
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        executeDrawingProgram();
        generationGraphLabel.setOpacity(0);
        showGraph();
    }
}
