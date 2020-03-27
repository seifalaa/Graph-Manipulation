package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class MinimumSpanningTree implements Runnable{

    public Label inputGraphLabel;
    public Label outputGraphLabel;
    public Label genrationGraphLabel;
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


    private int numberOfEdges;

    public void showGraphs() {
        inputImage.setImage(new Image(new File("graph.png").toURI().toString()));
        outputImage.setImage(new Image(new File("MST.png").toURI().toString()));
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
        }
        else if(graphType.equals("Directed"))
        {
            ArrayList<Edge> edges = graph.directedMinimumSpanningTree();
            minimumSpanningTree(edges, graphType);
            fillTables(edges, graph.getGraphEdges());
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


    public void executeDrawingProgram() {
        try {
            Runtime.getRuntime().exec("main.exe");
            Process process = Runtime.getRuntime().exec("minimumSpanningTree.exe");
            process.waitFor();
        } catch (IOException | InterruptedException e) {
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

    @Override
    public void run() {
        executeDrawingProgram();
        genrationGraphLabel.setOpacity(0);
        inputGraphLabel.setOpacity(1);
        outputGraphLabel.setOpacity(1);
        showGraphs();


    }
}


