package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GraphColoring implements Runnable {

    public ImageView inputImage = new ImageView();
    public ImageView outputImage = new ImageView();
    public ProgressBar progressPar = new ProgressBar();
    public Label genrationGraphText;
    public Label outputGraphLabel;
    public Label inputGraphLabel;
    private Graph graph = new Graph();
    private int numberOfVertexes;
    private int numberOfEdges;
    private String graphType;

    public void showGraphs(ActionEvent actionEvent) {
        setImages();
    }

    public void getGraph(Graph graph, int numberOfVertexes, int numberOfEdges, String graphType) throws IOException {
        this.graph = graph;
        this.numberOfVertexes = numberOfVertexes;
        this.numberOfEdges = numberOfEdges;
        this.graphType = graphType;
        colorGraph();

        //setProgressPar();

    }

    public void colorGraph() throws IOException {
        graph.coloringTheVertices();
        FileWriter writer = new FileWriter("coloredGraph.txt");
        writer.write(numberOfVertexes + "\n");
        writer.write(numberOfEdges + "\n");
        writer.write(graphType + "\n");
        ArrayList<Vertex> vertices = graph.getGraphVertices();
        ArrayList<Edge> edges = graph.getGraphEdges();
        for (int i = 0; i < numberOfVertexes; i++) {
            writer.write(vertices.get(i).getVertexName() + ",");
        }
        writer.write("\n");
        for (int i = 0; i < numberOfVertexes; i++) {
            writer.write(vertices.get(i).getVertexColor() + ",");
        }
        writer.write("\n");
        for (int i = 0; i < numberOfEdges; i++) {
            writer.write(edges.get(i).getEdgeName() + "," + edges.get(i).getStart().getVertexName() + "," + edges.get(i).getTermination().getVertexName() + "\n");
        }

        writer.close();

    }
    public void setImages() {
        inputImage.setImage(new Image(new File("graph.png").toURI().toString()));
        outputImage.setImage(new Image(new File("coloredGraph.png").toURI().toString()));
    }

    public void callDrawingPrograms() {
        try {
            Runtime.getRuntime().exec("main.exe");
            Process process = Runtime.getRuntime().exec("coloredGraph.exe");
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        callDrawingPrograms();
        genrationGraphText.setOpacity(0);
        inputGraphLabel.setOpacity(1);
        outputGraphLabel.setOpacity(1);
        setImages();
    }
}
