package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DijkstraController implements Runnable {
    public Label generatingLbl;
    public ImageView inputImage;
    public ImageView outputImage;
    public Label inputGraphLbl;
    public Label outputGraphLbl;
    public Button previousBtn;
    public Button nextBtn;
    public TableView<DijkstraTableData> table;
    public TableColumn<DijkstraTableData,String> pathCol;
    public TableColumn<DijkstraTableData,String> costCol;
    private Graph graph;
    private String startVertex;
    private int counter = 1;
    private int numberOfEdges;
    private ArrayList<String>tableData;
    void getGraph(Graph graph, String startVertex) {
        this.graph = graph;
        this.startVertex = startVertex;
        dijkstra();
    }

    private void dijkstra() {
        Pair<Graph, ArrayList<String>> pair = graph.dijkstra(startVertex) ;
        Graph g = pair.getKey();
        tableData = pair.getValue();
        writeOutputGraphData(g);
    }

    void callDrawingProgram() {
        try {
            Process process = Runtime.getRuntime().exec("main2.exe");
            process.waitFor();
            Process process1 = Runtime.getRuntime().exec("drawGraph.exe");
            process1.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    void showGraph() {
        inputImage.setImage(new Image(new File("graph.png").toURI().toString()));
    }

    @Override
    public void run() {
        callDrawingProgram();
        showGraph();
        showOutputFirstImage();
        fillTable();
        generatingLbl.setOpacity(0);
        table.setOpacity(1);
        //table.setOpacity(1);
        inputGraphLbl.setOpacity(1);
        outputGraphLbl.setOpacity(1);
        //nextBtn.setOpacity(1);
        previousBtn.setDisable(true);
        if(counter >= numberOfEdges){

            nextBtn.setDisable(true);
        }
        previousBtn.setVisible(true);
        nextBtn.setVisible(true);
    }

    public void showOutputFirstImage() {
        outputImage.setImage(new Image(new File("graph1.png").toURI().toString()));
    }

    public void showPrevious(ActionEvent actionEvent) {
        nextBtn.setDisable(false);
        if (counter > 1) {
            counter--;
            outputImage.setImage(new Image(new File("graph" + counter + ".png").toURI().toString()));
            if (counter == 1) {
                previousBtn.setDisable(true);
            }

        }
    }

    public void showNext(ActionEvent actionEvent) {
        previousBtn.setDisable(false);

        if (counter <= numberOfEdges) {
            counter++;
            outputImage.setImage(new Image(new File("graph" + counter + ".png").toURI().toString()));
            if (counter == numberOfEdges) {
                nextBtn.setDisable(true);
            }
        }
    }

    private void writeOutputGraphData(Graph graph) {
        ArrayList<Vertex> vertices = graph.getGraphVertices();
        ArrayList<Edge> edges = graph.getGraphEdges();
        numberOfEdges = edges.size();
        try {
            FileWriter fileWriter = new FileWriter("dijkstraGraph.txt");
            fileWriter.write(vertices.size() + "\n");
            fileWriter.write(edges.size() + "\n");
            if(graph.directed)
            {
                fileWriter.write("Directed\n");
            }
           else
            {
                fileWriter.write("Undirected\n");
            }
            for (Vertex vertex : vertices) {
                fileWriter.write(vertex.getVertexName() + ",");
            }
            fileWriter.write("\n");
            for (Edge edge : edges) {
                fileWriter.write(edge.getWeight() + "," + edge.getStart().getVertexName() + "," + edge.getTermination().getVertexName() + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void fillTable()
    {
        ArrayList<DijkstraTableData> dijkstraTableData = new ArrayList<>();
        for(String str : tableData)
        {
            String[] data = str.split("/");
            dijkstraTableData.add(new DijkstraTableData(data[0],data[1]));
        }
        ObservableList<DijkstraTableData> observableList = FXCollections.observableArrayList(dijkstraTableData);
        table.setItems(observableList);
        pathCol.setCellValueFactory(new PropertyValueFactory<>("Path"));
        costCol.setCellValueFactory(new PropertyValueFactory<>("Cost"));
    }
}
