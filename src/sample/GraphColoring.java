package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class GraphColoring {

    public ImageView inputImage = new ImageView();
    public ImageView outputImage = new ImageView();
    public Button showGraphsBtn = new Button();
    public ProgressBar progressPar = new ProgressBar();
    private Graph graph = new Graph();
    private int numberOfVertexes;
    private int numberOfEdges;
    private String graphType;
    public void showGraphs(ActionEvent actionEvent) {
        setImages();
    }
    public void getGraph(Graph graph ,int numberOfVertexes,int numberOfEdges,String graphType ) throws IOException {
        this.graph = graph;
        this.numberOfVertexes = numberOfVertexes;
        this.numberOfEdges = numberOfEdges;
        this.graphType = graphType;
        colorGraph();
        callDrawingPrograms();
        setProgressPar();
        showGraphsBtn.setDisable(false);

    }
    public void colorGraph() throws IOException {
        graph.coloringTheVertices();
        FileWriter writer = new FileWriter("coloredGraph.txt");
        writer.write(numberOfVertexes+"\n");
        writer.write(numberOfEdges+"\n");
        writer.write(graphType+"\n");
        ArrayList<Vertex> vertices = graph.getGraphVertices();
        ArrayList<Edge> edges = graph.getGraphEdges();
        for(int i=0;i<numberOfVertexes;i++)
        {
            writer.write(vertices.get(i).getVertexName()+",");
        }
        writer.write("\n");
        for(int i=0;i<numberOfVertexes;i++)
        {
            writer.write(vertices.get(i).getVertexColor()+",");
        }
        writer.write("\n");
        for(int i=0;i<numberOfEdges;i++ )
        {
            writer.write(edges.get(i).getEdgeName()+","+edges.get(i).getStart().getVertexName()+","+edges.get(i).getTermination().getVertexName()+"\n");
        }

        writer.close();
    }
    public void setProgressPar()
    {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressPar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(6), e-> {
                }, new KeyValue(progressPar.progressProperty(), 1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        timeline.stop();



                    }
                },
                6000
        );
    }
    public void setImages()
    {
        try {
            inputImage.setImage(new Image(new File("graph.png").toURL().toString()));
            outputImage.setImage(new Image(new File("coloredGraph.png").toURL().toString()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    public void callDrawingPrograms()
    {
        try {
            Runtime.getRuntime().exec("main.exe");
            Runtime.getRuntime().exec("coloredGraph.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
