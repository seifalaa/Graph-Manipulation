package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;

public class FlueryAlgorithm implements Runnable {

    public Label generationGraphLabel;
    @FXML
    private ImageView graphImage = new ImageView();




    @FXML
    private Label resultLbl = new Label();

    @FXML
    void showGraph() {
        graphImage.setImage(new Image(new File("graph.png").toURI().toString()));
        resultLbl.setOpacity(1);
    }

    public void getGraph(Graph graph, String graphType) {
        if (graphType.equals("Directed")) {
            String result = graph.directedFleuryAlgorithm();
            resultLbl.setText(result);
        } else if (graphType.equals("Undirected")) {
            String result = graph.fleuryAlgorithm();
            resultLbl.setText(result);
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
