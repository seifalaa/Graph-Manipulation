package sample;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;

public class MaxFlowController implements Runnable {

    public ImageView graphImage;
    public Label generatingGraphLbl;
    public Label outputLbl;
    private Graph graph;
    private String startVertex;
    private String endVertex;

    void getGraph(Graph graph, String startVertex, String endVertex) {
        this.graph = graph;
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        maximumFlow();
    }

    void callDrawingProgram() {
        try {
            Process process = Runtime.getRuntime().exec("main2.exe");
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    void showGraph() {
        graphImage.setImage(new Image(new File("graph.png").toURI().toString()));
    }

    void maximumFlow() {
        int result = graph.maximumFlow(startVertex,endVertex);
        outputLbl.setText(outputLbl.getText()+" "+result);
    }

    @Override
    public void run() {
        callDrawingProgram();
        showGraph();
        generatingGraphLbl.setOpacity(0);
        outputLbl.setOpacity(1);
    }
}
