package sample;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class GraphRepresentationController implements Initializable {
    public ImageView graphImage = new ImageView();
    public TableView<EdgeForTabel> table = new TableView<>();
    public TableColumn<EdgeForTabel,String> edgesNamesTbl = new TableColumn<>();
    public TableColumn<EdgeForTabel,String> edgesWeightTbl = new TableColumn<>();
    public TableColumn<EdgeForTabel,String> edgesStartVertex = new TableColumn<>();
    public TableColumn<EdgeForTabel,String> edgesEndVertex = new TableColumn<>();
    public Button showGraphBtn = new Button();
    public ProgressBar progressPar = new ProgressBar();
    public Graph graph = new Graph();
    public int numberOfVertexes;
    public int numberOfEdges;
    public String graphType;
    public ArrayList<EdgeForTabel>edges = new ArrayList<>();
    public TableView<AdjListTableContent> adjListTbl = new TableView<>();
    public TableColumn<AdjListTableContent,String>vertexCol = new TableColumn<>();
    public TableColumn<AdjListTableContent, ArrayList<String>>adjCol = new TableColumn<>();
    public GridPane adjGrid = new GridPane();
    public GridPane repMatrixGrid = new GridPane();
    public GridPane incGrid = new GridPane();

    public GraphRepresentationController(){
        try {
            Runtime.getRuntime().exec("E:\\books\\6th semester\\algorithms\\graph-drawing\\main.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        showGraphBtn.setDisable(false);
                    }
                },
                6000
        );

    }

    public void showGraph(ActionEvent actionEvent) {
        try {
            graphImage.setImage(new Image(new File("E:\\books\\6th semester\\algorithms\\graph-drawing\\graph.png").toURL().toString()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        table.setOpacity(1);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setGraph(Graph graph , int numberOfVertexes , int numberOfEdges , String graphType) throws FileNotFoundException {
        this.graph = graph;
        this.numberOfEdges = numberOfEdges;
        this.numberOfVertexes = numberOfVertexes;
        this.graphType = graphType;

        fillTable();
        setProgressPar();
        showAdjList();
        showAdjMatrix();
        showIncidenceMatrix();
        showRepresentationMatrix();

    }
    public void fillTable() throws FileNotFoundException {
        Scanner input = new Scanner(new File("E:\\books\\6th semester\\algorithms\\graph-drawing\\graph.txt"));
        ArrayList<String> content = new ArrayList<>();

        while (input.hasNext()) {
            String line = input.nextLine();
            content.add(line);
        }
        int numberOfEdges = Integer.parseInt(content.get(1));
        for (int i = 0; i < numberOfEdges; i++) {
            String[] edgeString = content.get(i + 4).split(",");
            EdgeForTabel edge = new EdgeForTabel(edgeString[0], edgeString[1], edgeString[2], edgeString[3]);
            edges.add(edge);
        }
        input.close();
        ObservableList<EdgeForTabel> observableList = FXCollections.observableArrayList(edges);
        table.setItems(observableList);
        edgesNamesTbl.setCellValueFactory(new PropertyValueFactory<>("Name"));
        edgesWeightTbl.setCellValueFactory(new PropertyValueFactory<>("Weight"));
        edgesStartVertex.setCellValueFactory(new PropertyValueFactory<>("From"));
        edgesEndVertex.setCellValueFactory(new PropertyValueFactory<>("To"));
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
    public void showAdjList()
    {
        if(graphType.equals("Directed"))
        {
            ArrayList<ArrayList<Vertex>> adjacencyList = graph.createDirectedAdjacencyList();
            ArrayList<Vertex>vertices = graph.getGraphVertices();
            ArrayList<AdjListTableContent>tableContents = new ArrayList<>();
            for(int i=0;i<numberOfVertexes;i++)
            {
                String vertex = vertices.get(i).getVertexName();
                ArrayList<Vertex>adjacent = adjacencyList.get(i);
                ArrayList<String>temp = new ArrayList<>();
                for (Vertex value : adjacent) {
                    temp.add(value.getVertexName());
                }
                tableContents.add(new AdjListTableContent(vertex,temp));
            }
            ObservableList<AdjListTableContent> observableList = FXCollections.observableArrayList(tableContents);
            adjListTbl.setItems(observableList);
            vertexCol.setCellValueFactory(new PropertyValueFactory<>("Vertex"));
            adjCol.setCellValueFactory(new PropertyValueFactory<>("Adjacent"));
        }
        else if(graphType.equals("Undirected"))
        {
            ArrayList<Vertex> adjacencyList = graph.createAdjacencyList();
            ArrayList<AdjListTableContent>tableContents = new ArrayList<>();
            for(int i=0;i<numberOfVertexes;i++)
            {
                String vertex = adjacencyList.get(i).getVertexName();
                ArrayList<String>adjacent = new ArrayList<>();
                ArrayList<Vertex> temp = adjacencyList.get(i).getAdjacentVertices();
                for (Vertex value : temp) {
                    adjacent.add(value.getVertexName());
                }
                AdjListTableContent obj = new AdjListTableContent(vertex,adjacent);
                tableContents.add(obj);
            }
            ObservableList<AdjListTableContent>observableList = FXCollections.observableArrayList(tableContents);
            adjListTbl.setItems(observableList);
            vertexCol.setCellValueFactory(new PropertyValueFactory<>("Vertex"));
            adjCol.setCellValueFactory(new PropertyValueFactory<>("Adjacent"));
        }
    }
    public void showAdjMatrix()
    {
        if(graphType.equals("Directed"))
        {
            int [][] adjMatrix = graph.createAdjacencyMatrix();
            ArrayList<String> vertexes = new ArrayList<>();
            ArrayList<Vertex>temp = graph.getGraphVertices();
            for(int i=0;i<numberOfVertexes;i++)
            {
                vertexes.add(temp.get(i).getVertexName());
            }
            double width = 950.0/numberOfVertexes;
            double height = 400.0/numberOfVertexes;
            Label lbl2 = new Label("Vertex");
            lbl2.setAlignment(Pos.CENTER);
            lbl2.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
            lbl2.setPrefWidth(width);
            lbl2.setPrefHeight(height);
            lbl2.setFont(Font.font("", FontWeight.BOLD, 15));
            adjGrid.add(lbl2,0,0);
            for(int  i=0 ;i< numberOfVertexes;i++)
            {
                Label lbl = new Label(vertexes.get(i));
                lbl.setAlignment(Pos.CENTER);
                lbl.setPrefWidth(width);
                lbl.setPrefHeight(height);
                lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                GridPane.setColumnIndex(lbl,i+1);
                GridPane.setRowIndex(lbl,0);
                lbl.setFont(Font.font("", FontWeight.BOLD, 15));
                adjGrid.getChildren().add(lbl);
            }
            for(int i=0;i<numberOfVertexes;i++)
            {
                Label lbl = new Label(vertexes.get(i));
                lbl.setPrefWidth(width);
                lbl.setPrefHeight(height);
                lbl.setAlignment(Pos.CENTER);
                lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                GridPane.setColumnIndex(lbl,0);
                GridPane.setRowIndex(lbl,i+1);
                lbl.setFont(Font.font("", FontWeight.BOLD, 15));
                adjGrid.getChildren().add(lbl);
            }
            for(int i=0;i<numberOfVertexes;i++)
            {
                for(int j=0;j<numberOfVertexes;j++)
                {
                    Label lbl = new Label(String.valueOf(adjMatrix[i][j]));
                    lbl.setAlignment(Pos.CENTER);
                    lbl.setPrefWidth(width);
                    lbl.setPrefHeight(height);
                    lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                    GridPane.setColumnIndex(lbl,i+1);
                    GridPane.setRowIndex(lbl,j+1);
                    adjGrid.getChildren().add(lbl);
                }
            }

        }
        else if(graphType.equals("Undirected"))
        {
            int [][] adjMatrix = graph.createDiAdjacencyMatrix();
            ArrayList<String> vertexes = new ArrayList<>();
            ArrayList<Vertex>temp = graph.getGraphVertices();
            for(int i=0;i<numberOfVertexes;i++)
            {
                vertexes.add(temp.get(i).getVertexName());
            }
            double width = 950.0/numberOfVertexes;
            double height = 400.0/numberOfVertexes;
            Label lbl2 = new Label("Vertex");
            lbl2.setAlignment(Pos.CENTER);
            lbl2.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
            lbl2.setPrefWidth(width);
            lbl2.setPrefHeight(height);
            lbl2.setFont(Font.font("", FontWeight.BOLD, 15));
            adjGrid.add(lbl2,0,0);
            for(int  i=0 ;i< numberOfVertexes;i++)
            {
                Label lbl = new Label(vertexes.get(i));
                lbl.setAlignment(Pos.CENTER);
                lbl.setPrefWidth(width);
                lbl.setPrefHeight(height);
                lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                GridPane.setColumnIndex(lbl,i+1);
                GridPane.setRowIndex(lbl,0);
                lbl.setFont(Font.font("", FontWeight.BOLD, 15));
                adjGrid.getChildren().add(lbl);
            }
            for(int i=0;i<numberOfVertexes;i++)
            {
                Label lbl = new Label(vertexes.get(i));
                lbl.setPrefWidth(width);
                lbl.setPrefHeight(height);
                lbl.setAlignment(Pos.CENTER);
                lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                GridPane.setColumnIndex(lbl,0);
                GridPane.setRowIndex(lbl,i+1);
                lbl.setFont(Font.font("", FontWeight.BOLD, 15));
                adjGrid.getChildren().add(lbl);
            }
            for(int i=0;i<numberOfVertexes;i++)
            {
                for(int j=0;j<numberOfVertexes;j++)
                {
                    Label lbl = new Label(String.valueOf(adjMatrix[i][j]));
                    lbl.setAlignment(Pos.CENTER);
                    lbl.setPrefWidth(width);
                    lbl.setPrefHeight(height);
                    lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                    GridPane.setColumnIndex(lbl,i+1);
                    GridPane.setRowIndex(lbl,j+1);
                    adjGrid.getChildren().add(lbl);
                }
            }

        }

    }
    void showIncidenceMatrix()
    {
        if(graphType.equals("Directed"))
        {
            int[][]matrix =  graph.createIncidenceMatrix(true);
            ArrayList<String> vertexes = new ArrayList<>();
            ArrayList<Vertex>temp = graph.getGraphVertices();
            for(int i=0;i<numberOfVertexes;i++)
            {
                vertexes.add(temp.get(i).getVertexName());
            }
            double width = 950.0/numberOfVertexes;
            double height = 400.0/numberOfVertexes;
            Label lbl2 = new Label("Vertex");
            lbl2.setAlignment(Pos.CENTER);
            lbl2.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
            lbl2.setPrefWidth(width);
            lbl2.setPrefHeight(height);
            lbl2.setFont(Font.font("", FontWeight.BOLD, 15));
            incGrid.add(lbl2,0,0);
            for(int  i=0 ;i< numberOfVertexes;i++)
            {
                Label lbl = new Label(vertexes.get(i));
                lbl.setAlignment(Pos.CENTER);
                lbl.setPrefWidth(width);
                lbl.setPrefHeight(height);
                lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                GridPane.setColumnIndex(lbl,i+1);
                GridPane.setRowIndex(lbl,0);
                lbl.setFont(Font.font("", FontWeight.BOLD, 15));
                incGrid.getChildren().add(lbl);
            }
            for(int i=0;i<numberOfVertexes;i++)
            {
                Label lbl = new Label(vertexes.get(i));
                lbl.setPrefWidth(width);
                lbl.setPrefHeight(height);
                lbl.setAlignment(Pos.CENTER);
                lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                GridPane.setColumnIndex(lbl,0);
                GridPane.setRowIndex(lbl,i+1);
                lbl.setFont(Font.font("", FontWeight.BOLD, 15));
                incGrid.getChildren().add(lbl);
            }
            for(int i=0;i<numberOfVertexes;i++)
            {
                for(int j=0;j<numberOfVertexes;j++)
                {
                    Label lbl = new Label(String.valueOf(matrix[i][j]));
                    lbl.setAlignment(Pos.CENTER);
                    lbl.setPrefWidth(width);
                    lbl.setPrefHeight(height);
                    lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                    GridPane.setColumnIndex(lbl,i+1);
                    GridPane.setRowIndex(lbl,j+1);
                    incGrid.getChildren().add(lbl);
                }
            }

        }
        else if(graphType.equals("Undirected"))
        {
            int [][] matrix = graph.createIncidenceMatrix(false);
            ArrayList<String> vertexes = new ArrayList<>();
            ArrayList<Vertex>temp = graph.getGraphVertices();
            for(int i=0;i<numberOfVertexes;i++)
            {
                vertexes.add(temp.get(i).getVertexName());
            }
            double width = 950.0/numberOfVertexes;
            double height = 400.0/numberOfVertexes;
            Label lbl2 = new Label("Vertex");
            lbl2.setAlignment(Pos.CENTER);
            lbl2.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
            lbl2.setPrefWidth(width);
            lbl2.setPrefHeight(height);
            lbl2.setFont(Font.font("", FontWeight.BOLD, 15));
            incGrid.add(lbl2,0,0);
            for(int  i=0 ;i< numberOfVertexes;i++)
            {
                Label lbl = new Label(vertexes.get(i));
                lbl.setAlignment(Pos.CENTER);
                lbl.setPrefWidth(width);
                lbl.setPrefHeight(height);
                lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                GridPane.setColumnIndex(lbl,i+1);
                GridPane.setRowIndex(lbl,0);
                lbl.setFont(Font.font("", FontWeight.BOLD, 15));
                adjGrid.getChildren().add(lbl);
            }
            for(int i=0;i<numberOfVertexes;i++)
            {
                Label lbl = new Label(vertexes.get(i));
                lbl.setPrefWidth(width);
                lbl.setPrefHeight(height);
                lbl.setAlignment(Pos.CENTER);
                lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                GridPane.setColumnIndex(lbl,0);
                GridPane.setRowIndex(lbl,i+1);
                lbl.setFont(Font.font("", FontWeight.BOLD, 15));
                incGrid.getChildren().add(lbl);
            }
            for(int i=0;i<numberOfVertexes;i++)
            {
                for(int j=0;j<numberOfVertexes;j++)
                {
                    Label lbl = new Label(String.valueOf(matrix[i][j]));
                    lbl.setAlignment(Pos.CENTER);
                    lbl.setPrefWidth(width);
                    lbl.setPrefHeight(height);
                    lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                    GridPane.setColumnIndex(lbl,i+1);
                    GridPane.setRowIndex(lbl,j+1);
                    incGrid.getChildren().add(lbl);
                }
            }

        }
    }
    void showRepresentationMatrix()
    {
        if(graphType.equals("Directed"))
        {
            int[][]repMatrix = graph.createDiRepresentationMatrix();

            ArrayList<String> vertexes = new ArrayList<>();
            ArrayList<Vertex>temp = graph.getGraphVertices();
            for(int i=0;i<numberOfVertexes;i++)
            {
                vertexes.add(temp.get(i).getVertexName());
            }
            double width = 950.0/numberOfVertexes;
            double height = 400.0/numberOfVertexes;
            Label lbl2 = new Label("Vertex");
            lbl2.setAlignment(Pos.CENTER);
            lbl2.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
            lbl2.setPrefWidth(width);
            lbl2.setPrefHeight(height);
            lbl2.setFont(Font.font("", FontWeight.BOLD, 15));
            repMatrixGrid.add(lbl2,0,0);
            for(int  i=0 ;i< numberOfVertexes;i++)
            {
                Label lbl = new Label(vertexes.get(i));
                lbl.setAlignment(Pos.CENTER);
                lbl.setPrefWidth(width);
                lbl.setPrefHeight(height);
                lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                GridPane.setColumnIndex(lbl,i+1);
                GridPane.setRowIndex(lbl,0);
                lbl.setFont(Font.font("", FontWeight.BOLD, 15));
                repMatrixGrid.getChildren().add(lbl);
            }
            for(int i=0;i<numberOfVertexes;i++)
            {
                Label lbl = new Label(vertexes.get(i));
                lbl.setPrefWidth(width);
                lbl.setPrefHeight(height);
                lbl.setAlignment(Pos.CENTER);
                lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                GridPane.setColumnIndex(lbl,0);
                GridPane.setRowIndex(lbl,i+1);
                lbl.setFont(Font.font("", FontWeight.BOLD, 15));
                repMatrixGrid.getChildren().add(lbl);
            }
            for(int i=0;i<numberOfVertexes;i++)
            {
                for(int j=0;j<numberOfVertexes;j++)
                {
                    Label lbl = new Label(String.valueOf(repMatrix[i][j]));
                    lbl.setAlignment(Pos.CENTER);
                    lbl.setPrefWidth(width);
                    lbl.setPrefHeight(height);
                    lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                    GridPane.setColumnIndex(lbl,i+1);
                    GridPane.setRowIndex(lbl,j+1);
                    repMatrixGrid.getChildren().add(lbl);
                }
            }

        }
        else if(graphType.equals("Undirected"))
        {
            int [][] repMatrix = graph.createRepresentationMatrix();
            ArrayList<String> vertexes = new ArrayList<>();
            ArrayList<Vertex>temp = graph.getGraphVertices();
            for(int i=0;i<numberOfVertexes;i++)
            {
                vertexes.add(temp.get(i).getVertexName());
            }
            double width = 950.0/numberOfVertexes;
            double height = 400.0/numberOfVertexes;
            Label lbl2 = new Label("Vertex");
            lbl2.setAlignment(Pos.CENTER);
            lbl2.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
            lbl2.setPrefWidth(width);
            lbl2.setPrefHeight(height);
            lbl2.setFont(Font.font("", FontWeight.BOLD, 15));
            repMatrixGrid.add(lbl2,0,0);
            for(int  i=0 ;i< numberOfVertexes;i++)
            {
                Label lbl = new Label(vertexes.get(i));
                lbl.setAlignment(Pos.CENTER);
                lbl.setPrefWidth(width);
                lbl.setPrefHeight(height);
                lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                GridPane.setColumnIndex(lbl,i+1);
                GridPane.setRowIndex(lbl,0);
                lbl.setFont(Font.font("", FontWeight.BOLD, 15));
                repMatrixGrid.getChildren().add(lbl);
            }
            for(int i=0;i<numberOfVertexes;i++)
            {
                Label lbl = new Label(vertexes.get(i));
                lbl.setPrefWidth(width);
                lbl.setPrefHeight(height);
                lbl.setAlignment(Pos.CENTER);
                lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                GridPane.setColumnIndex(lbl,0);
                GridPane.setRowIndex(lbl,i+1);
                lbl.setFont(Font.font("", FontWeight.BOLD, 15));
                repMatrixGrid.getChildren().add(lbl);
            }
            for(int i=0;i<numberOfVertexes;i++)
            {
                for(int j=0;j<numberOfVertexes;j++)
                {
                    Label lbl = new Label(String.valueOf(repMatrix[i][j]));
                    lbl.setAlignment(Pos.CENTER);
                    lbl.setPrefWidth(width);
                    lbl.setPrefHeight(height);
                    lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                    GridPane.setColumnIndex(lbl,i+1);
                    GridPane.setRowIndex(lbl,j+1);
                    repMatrixGrid.getChildren().add(lbl);
                }
            }

        }

    }

}
