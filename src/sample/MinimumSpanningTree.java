package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;


public class MinimumSpanningTree {

    @FXML
    private ImageView inputImage = new ImageView();

    @FXML
    private ImageView outputImage = new ImageView();

    @FXML
    private TableView<EdgeForTabel> inputTbl = new TableView<>();

    @FXML
    private TableColumn<EdgeForTabel, String> inputEdgeNameCol = new TableColumn<>();

    @FXML
    private TableColumn<EdgeForTabel, String> inputEdgeWeightCol = new TableColumn<>();

    @FXML
    private TableColumn<EdgeForTabel, String> inputStartVertexCol = new TableColumn<>();

    @FXML
    private TableColumn<EdgeForTabel, String> inputEndVertexCol = new TableColumn<>();

    @FXML
    private TableView<EdgeForTabel> outputTbl = new TableView<>();

    @FXML
    private TableColumn<EdgeForTabel, String> outputEdgeNameCol = new TableColumn<>();

    @FXML
    private TableColumn<EdgeForTabel, String> outputEdgeWeightCol = new TableColumn<>();

    @FXML
    private TableColumn<EdgeForTabel, String> outputStartVertexCol = new TableColumn<>();

    @FXML
    private TableColumn<EdgeForTabel, String> outputEndVertexCol = new TableColumn<>();

    @FXML
    private Button showGraphBtn = new Button();

    @FXML
    private ProgressBar progressPar = new ProgressBar();
    private int numberOfEdges;

    public void showGraphs(ActionEvent actionEvent) {
        try {
            inputImage.setImage(new Image(new File("graph.png").toURL().toString()));
            outputImage.setImage(new Image(new File("MST.png").toURL().toString()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        inputTbl.setOpacity(1);
        outputTbl.setOpacity(1);
    }

    public void getGraph(Graph graph, int numberOfEdges, String graphType) throws IOException {
        this.numberOfEdges = numberOfEdges;
        if(graphType.equals("Undirected"))
        {
            ArrayList<Edge> edges = graph.Kruskal();
            minimumSpanningTree(edges, graphType);
            fillTables(edges, graph.getGraphEdges());
            executeDrawingProgram();
            setProgressPar();
            showGraphBtn.setDisable(false);
        }
        else if(graphType.equals("Directed"))
        {
            ArrayList<Edge> edges = graph.directedMinimumSpanningTree();
            minimumSpanningTree(edges, graphType);
            fillTables(edges, graph.getGraphEdges());
            executeDrawingProgram();
            setProgressPar();
            showGraphBtn.setDisable(false);
        }


    }

    public void minimumSpanningTree(ArrayList<Edge> edges, String graphType) throws IOException {
        FileWriter writer = new FileWriter("MST.txt");
        writer.write(edges.size() + "\n");
        writer.write(graphType + "\n");
        for (Edge edge : edges) {
            writer.write(edge.getEdgeName() + "," + edge.getWeight() + "," + edge.getStart().getVertexName() + "," + edge.getTermination().getVertexName() + "\n");
        }
        writer.close();
    }

    public void setProgressPar() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressPar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(6), e -> {
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

    public void executeDrawingProgram() {
        try {
            Runtime.getRuntime().exec("main.exe");
            Runtime.getRuntime().exec("minimumSpanningTree.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fillTables(ArrayList<Edge> outputEdges, ArrayList<Edge> inputEdges) {
        ArrayList<EdgeForTabel> edgeForTable = new ArrayList<>();
        ArrayList<EdgeForTabel> edgeForTable2 = new ArrayList<>();
        for (int i = 0; i < numberOfEdges; i++) {
            edgeForTable.add(new EdgeForTabel(inputEdges.get(i).getEdgeName(), String.valueOf(inputEdges.get(i).getWeight()), inputEdges.get(i).getStart().getVertexName(), inputEdges.get(i).getTermination().getVertexName()));
        }
        ObservableList<EdgeForTabel> observableList = FXCollections.observableArrayList(edgeForTable);
        for (Edge outputEdge : outputEdges) {
            edgeForTable2.add(new EdgeForTabel(outputEdge.getEdgeName(), String.valueOf(outputEdge.getWeight()), outputEdge.getStart().getVertexName(), outputEdge.getTermination().getVertexName()));
        }
        ObservableList<EdgeForTabel> observableList2 = FXCollections.observableArrayList(edgeForTable2);

        inputTbl.setItems(observableList);
        inputEdgeNameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        inputEdgeWeightCol.setCellValueFactory(new PropertyValueFactory<>("Weight"));
        inputStartVertexCol.setCellValueFactory(new PropertyValueFactory<>("From"));
        inputEndVertexCol.setCellValueFactory(new PropertyValueFactory<>("To"));

        outputTbl.setItems(observableList2);
        outputEdgeNameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        outputEdgeWeightCol.setCellValueFactory(new PropertyValueFactory<>("Weight"));
        outputStartVertexCol.setCellValueFactory(new PropertyValueFactory<>("From"));
        outputEndVertexCol.setCellValueFactory(new PropertyValueFactory<>("To"));


    }
}


