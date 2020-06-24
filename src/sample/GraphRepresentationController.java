package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GraphRepresentationController implements  Runnable {
    public ImageView graphImage = new ImageView();
    public TableView<EdgeForTabel> table = new TableView<>();
    public TableColumn<EdgeForTabel, String> edgesNamesTbl = new TableColumn<>();
    public TableColumn<EdgeForTabel, String> edgesWeightTbl = new TableColumn<>();
    public TableColumn<EdgeForTabel, String> edgesStartVertex = new TableColumn<>();
    public TableColumn<EdgeForTabel, String> edgesEndVertex = new TableColumn<>();
    public Button showGraphBtn = new Button();
    public ProgressBar progressPar = new ProgressBar();
    public Graph graph = new Graph();
    public int numberOfVertexes;
    public int numberOfEdges;
    public String graphType;
    public ArrayList<EdgeForTabel> edges = new ArrayList<>();
    public TableView<AdjListTableContent> adjListTbl = new TableView<>();
    public TableColumn<AdjListTableContent, String> vertexCol = new TableColumn<>();
    public TableColumn<AdjListTableContent, ArrayList<String>> adjCol = new TableColumn<>();
    public GridPane adjGrid = new GridPane();
    public GridPane repMatrixGrid = new GridPane();
    public GridPane incGrid = new GridPane();
    public Label genrationGraphLabel;

    public GraphRepresentationController()  {
    }

    public void showGraph() {
        graphImage.setImage(new Image(new File("graph.png").toURI().toString()));
        table.setOpacity(1);
    }
    public void setGraph(Graph graph, int numberOfVertexes, int numberOfEdges, String graphType) throws FileNotFoundException {
        this.graph = graph;
        this.numberOfEdges = numberOfEdges;
        this.numberOfVertexes = numberOfVertexes;
        this.graphType = graphType;

        fillTable();
        showAdjList();
        showAdjMatrix();
        showIncidenceMatrix();
        showRepresentationMatrix();

    }

    public void fillTable() throws FileNotFoundException {
        Scanner input = new Scanner(new File("graph.txt"));
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


    public void showAdjList() {
        if (graphType.equals("Directed")) {
            ArrayList<ArrayList<Vertex>> adjacencyList = graph.createDirectedAdjacencyList();
            ArrayList<Vertex> vertices = graph.getGraphVertices();
            ArrayList<AdjListTableContent> tableContents = new ArrayList<>();
            for (int i = 0; i < numberOfVertexes; i++) {
                String vertex = vertices.get(i).getVertexName();
                ArrayList<Vertex> adjacent = adjacencyList.get(i);
                ArrayList<String> temp = new ArrayList<>();
                for (Vertex value : adjacent) {
                    temp.add(value.getVertexName());
                }
                tableContents.add(new AdjListTableContent(vertex, temp));
            }
            ObservableList<AdjListTableContent> observableList = FXCollections.observableArrayList(tableContents);
            adjListTbl.setItems(observableList);
            vertexCol.setCellValueFactory(new PropertyValueFactory<>("Vertex"));
            adjCol.setCellValueFactory(new PropertyValueFactory<>("Adjacent"));
        } else if (graphType.equals("Undirected")) {
            ArrayList<Vertex> adjacencyList = graph.createAdjacencyList();
            ArrayList<AdjListTableContent> tableContents = new ArrayList<>();
            for (int i = 0; i < numberOfVertexes; i++) {
                String vertex = adjacencyList.get(i).getVertexName();
                ArrayList<String> adjacent = new ArrayList<>();
                ArrayList<Vertex> temp = adjacencyList.get(i).getAdjacentVertices();
                for (Vertex value : temp) {
                    adjacent.add(value.getVertexName());
                }
                AdjListTableContent obj = new AdjListTableContent(vertex, adjacent);
                tableContents.add(obj);
            }
            ObservableList<AdjListTableContent> observableList = FXCollections.observableArrayList(tableContents);
            adjListTbl.setItems(observableList);
            vertexCol.setCellValueFactory(new PropertyValueFactory<>("Vertex"));
            adjCol.setCellValueFactory(new PropertyValueFactory<>("Adjacent"));
        }
    }

    public void showAdjMatrix() {
        if (graphType.equals("Directed")) {
            int[][] adjMatrix = graph.createDiAdjacencyMatrix();

            ArrayList<String> vertexes = new ArrayList<>();
            ArrayList<Vertex> temp = graph.getGraphVertices();
            for (int i = 0; i < numberOfVertexes; i++) {
                vertexes.add(temp.get(i).getVertexName());
            }
            double width = 950.0 / numberOfVertexes;
            double height = 400.0 / numberOfVertexes;
            Label lbl2 = new Label("Vertex");
            lbl2.setAlignment(Pos.CENTER);
            lbl2.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
            lbl2.setPrefWidth(width);
            lbl2.setPrefHeight(height);
            lbl2.setFont(Font.font("", FontWeight.BOLD, 15));
            adjGrid.add(lbl2, 0, 0);
            for (int i = 0; i < numberOfVertexes; i++) {
                Label lbl = new Label(vertexes.get(i));
                lbl.setAlignment(Pos.CENTER);
                lbl.setPrefWidth(width);
                lbl.setPrefHeight(height);
                lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                GridPane.setColumnIndex(lbl, i + 1);
                GridPane.setRowIndex(lbl, 0);
                lbl.setFont(Font.font("", FontWeight.BOLD, 15));
                adjGrid.getChildren().add(lbl);
            }
            for (int i = 0; i < numberOfVertexes; i++) {
                Label lbl = new Label(vertexes.get(i));
                lbl.setPrefWidth(width);
                lbl.setPrefHeight(height);
                lbl.setAlignment(Pos.CENTER);
                lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                GridPane.setColumnIndex(lbl, 0);
                GridPane.setRowIndex(lbl, i + 1);
                lbl.setFont(Font.font("", FontWeight.BOLD, 15));
                adjGrid.getChildren().add(lbl);
            }
            for (int i = 0; i < numberOfVertexes; i++) {
                for (int j = 0; j < numberOfVertexes; j++) {
                    Label lbl = new Label(String.valueOf(adjMatrix[i][j]));
                    lbl.setAlignment(Pos.CENTER);
                    lbl.setPrefWidth(width);
                    lbl.setPrefHeight(height);
                    lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                    GridPane.setColumnIndex(lbl, i + 1);
                    GridPane.setRowIndex(lbl, j + 1);
                    adjGrid.getChildren().add(lbl);
                }
            }

        } else if (graphType.equals("Undirected")) {
            int[][] adjMatrix = graph.createAdjacencyMatrix();
            ArrayList<String> vertexes = new ArrayList<>();
            ArrayList<Vertex> temp = graph.getGraphVertices();
            for (int i = 0; i < numberOfVertexes; i++) {
                vertexes.add(temp.get(i).getVertexName());
            }
            double width = 950.0 / numberOfVertexes;
            double height = 400.0 / numberOfVertexes;
            Label lbl2 = new Label("Vertex");
            lbl2.setAlignment(Pos.CENTER);
            lbl2.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
            lbl2.setPrefWidth(width);
            lbl2.setPrefHeight(height);
            lbl2.setFont(Font.font("", FontWeight.BOLD, 15));
            adjGrid.add(lbl2, 0, 0);
            for (int i = 0; i < numberOfVertexes; i++) {
                Label lbl = new Label(vertexes.get(i));
                lbl.setAlignment(Pos.CENTER);
                lbl.setPrefWidth(width);
                lbl.setPrefHeight(height);
                lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                GridPane.setColumnIndex(lbl, i + 1);
                GridPane.setRowIndex(lbl, 0);
                lbl.setFont(Font.font("", FontWeight.BOLD, 15));
                adjGrid.getChildren().add(lbl);
            }
            for (int i = 0; i < numberOfVertexes; i++) {
                Label lbl = new Label(vertexes.get(i));
                lbl.setPrefWidth(width);
                lbl.setPrefHeight(height);
                lbl.setAlignment(Pos.CENTER);
                lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                GridPane.setColumnIndex(lbl, 0);
                GridPane.setRowIndex(lbl, i + 1);
                lbl.setFont(Font.font("", FontWeight.BOLD, 15));
                adjGrid.getChildren().add(lbl);
            }
            for (int i = 0; i < numberOfVertexes; i++) {
                for (int j = 0; j < numberOfVertexes; j++) {
                    Label lbl = new Label(String.valueOf(adjMatrix[i][j]));
                    lbl.setAlignment(Pos.CENTER);
                    lbl.setPrefWidth(width);
                    lbl.setPrefHeight(height);
                    lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                    GridPane.setColumnIndex(lbl, i + 1);
                    GridPane.setRowIndex(lbl, j + 1);
                    adjGrid.getChildren().add(lbl);
                }
            }

        }

    }

    void showIncidenceMatrix() {
        if (graphType.equals("Directed")) {
            int[][] matrix = graph.createIncidenceMatrix(true);
            ArrayList<String> vertexes = new ArrayList<>();
            ArrayList<Vertex> temp = graph.getGraphVertices();
            for (int i = 0; i < numberOfVertexes; i++) {
                vertexes.add(temp.get(i).getVertexName());
            }
            double width = 950.0 / numberOfVertexes;
            double height = 400.0 / numberOfVertexes;
            Label lbl2 = new Label("Vertex/Edge");
            lbl2.setAlignment(Pos.CENTER);
            lbl2.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
            lbl2.setPrefWidth(width);
            lbl2.setPrefHeight(height);
            lbl2.setFont(Font.font("", FontWeight.BOLD, 15));
            incGrid.add(lbl2, 0, 0);
            for (int i = 0; i < numberOfVertexes; i++) {
                Label lbl = new Label(vertexes.get(i));
                lbl.setAlignment(Pos.CENTER);
                lbl.setPrefWidth(width);
                lbl.setPrefHeight(height);
                lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                GridPane.setColumnIndex(lbl, 0);
                GridPane.setRowIndex(lbl, i + 1);
                lbl.setFont(Font.font("", FontWeight.BOLD, 15));
                incGrid.getChildren().add(lbl);
            }
            ArrayList<Edge> edges = graph.getGraphEdges();
            for (int i = 0; i < numberOfEdges; i++) {
                Label lbl = new Label(edges.get(i).getEdgeName());
                lbl.setPrefWidth(width);
                lbl.setPrefHeight(height);
                lbl.setAlignment(Pos.CENTER);
                lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                GridPane.setColumnIndex(lbl, i + 1);
                GridPane.setRowIndex(lbl, 0);
                lbl.setFont(Font.font("", FontWeight.BOLD, 15));
                incGrid.getChildren().add(lbl);
            }
            for (int i = 0; i < numberOfVertexes; i++) {
                for (int j = 0; j < numberOfEdges; j++) {
                    Label lbl = new Label(String.valueOf(matrix[i][j]));
                    lbl.setAlignment(Pos.CENTER);
                    lbl.setPrefWidth(width);
                    lbl.setPrefHeight(height);
                    lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                    GridPane.setColumnIndex(lbl, j + 1);
                    GridPane.setRowIndex(lbl, i + 1);
                    incGrid.getChildren().add(lbl);
                }
            }

        } else if (graphType.equals("Undirected")) {
            int[][] matrix = graph.createIncidenceMatrix(false);
            ArrayList<String> vertexes = new ArrayList<>();
            ArrayList<Vertex> temp = graph.getGraphVertices();
            for (int i = 0; i < numberOfVertexes; i++) {
                vertexes.add(temp.get(i).getVertexName());
            }
            double width = 950.0 / numberOfVertexes;
            double height = 400.0 / numberOfVertexes;
            Label lbl2 = new Label("Vertex/Edge");
            lbl2.setAlignment(Pos.CENTER);
            lbl2.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
            lbl2.setPrefWidth(width);
            lbl2.setPrefHeight(height);
            lbl2.setFont(Font.font("", FontWeight.BOLD, 15));
            incGrid.add(lbl2, 0, 0);
            ///****
            for (int i = 0; i < numberOfVertexes; i++) {
                Label lbl = new Label(vertexes.get(i));
                lbl.setAlignment(Pos.CENTER);
                lbl.setPrefWidth(width);
                lbl.setPrefHeight(height);
                lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                GridPane.setColumnIndex(lbl, 0);
                GridPane.setRowIndex(lbl, i + 1);
                lbl.setFont(Font.font("", FontWeight.BOLD, 15));
                incGrid.getChildren().add(lbl);
            }
            ArrayList<Edge> edges = graph.getGraphEdges();
            for (int i = 0; i < numberOfEdges; i++) {
                Label lbl = new Label(edges.get(i).getEdgeName());
                lbl.setPrefWidth(width);
                lbl.setPrefHeight(height);
                lbl.setAlignment(Pos.CENTER);
                lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                GridPane.setColumnIndex(lbl, i + 1);
                GridPane.setRowIndex(lbl, 0);
                lbl.setFont(Font.font("", FontWeight.BOLD, 15));
                incGrid.getChildren().add(lbl);
            }
            for (int i = 0; i < numberOfVertexes; i++) {
                for (int j = 0; j < numberOfEdges; j++) {
                    Label lbl = new Label(String.valueOf(matrix[i][j]));
                    lbl.setAlignment(Pos.CENTER);
                    lbl.setPrefWidth(width);
                    lbl.setPrefHeight(height);
                    lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                    GridPane.setColumnIndex(lbl, j + 1);
                    GridPane.setRowIndex(lbl, i + 1);
                    incGrid.getChildren().add(lbl);
                }
            }

        }
    }

    void showRepresentationMatrix() {
        if (graphType.equals("Directed")) {
            int[][] repMatrix = graph.createDiRepresentationMatrix();
            ArrayList<String> vertexes = new ArrayList<>();
            ArrayList<Vertex> temp = graph.getGraphVertices();
            for (int i = 0; i < numberOfVertexes; i++) {
                vertexes.add(temp.get(i).getVertexName());
            }
            double width = 950.0 / numberOfVertexes;
            double height = 400.0 / numberOfVertexes;
            Label lbl2 = new Label("Vertex");
            lbl2.setAlignment(Pos.CENTER);
            lbl2.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
            lbl2.setPrefWidth(width);
            lbl2.setPrefHeight(height);
            lbl2.setFont(Font.font("", FontWeight.BOLD, 15));
            repMatrixGrid.add(lbl2, 0, 0);
            for (int i = 0; i < numberOfVertexes; i++) {
                Label lbl = new Label(vertexes.get(i));
                lbl.setAlignment(Pos.CENTER);
                lbl.setPrefWidth(width);
                lbl.setPrefHeight(height);
                lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                GridPane.setColumnIndex(lbl, i + 1);
                GridPane.setRowIndex(lbl, 0);
                lbl.setFont(Font.font("", FontWeight.BOLD, 15));
                repMatrixGrid.getChildren().add(lbl);
            }
            for (int i = 0; i < numberOfVertexes; i++) {
                Label lbl = new Label(vertexes.get(i));
                lbl.setPrefWidth(width);
                lbl.setPrefHeight(height);
                lbl.setAlignment(Pos.CENTER);
                lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                GridPane.setColumnIndex(lbl, 0);
                GridPane.setRowIndex(lbl, i + 1);
                lbl.setFont(Font.font("", FontWeight.BOLD, 15));
                repMatrixGrid.getChildren().add(lbl);
            }
            for (int i = 0; i < numberOfVertexes; i++) {
                for (int j = 0; j < numberOfVertexes; j++) {
                    Label lbl = new Label(String.valueOf(repMatrix[i][j]));
                    lbl.setAlignment(Pos.CENTER);
                    lbl.setPrefWidth(width);
                    lbl.setPrefHeight(height);
                    lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                    GridPane.setColumnIndex(lbl, j + 1);
                    GridPane.setRowIndex(lbl, i + 1);
                    repMatrixGrid.getChildren().add(lbl);
                }
            }

        } else if (graphType.equals("Undirected")) {
            int[][] repMatrix = graph.createRepresentationMatrix();
            ArrayList<String> vertexes = new ArrayList<>();
            ArrayList<Vertex> temp = graph.getGraphVertices();
            for (int i = 0; i < numberOfVertexes; i++) {
                vertexes.add(temp.get(i).getVertexName());
            }
            double width = 950.0 / numberOfVertexes;
            double height = 400.0 / numberOfVertexes;
            Label lbl2 = new Label("Vertex");
            lbl2.setAlignment(Pos.CENTER);
            lbl2.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
            lbl2.setPrefWidth(width);
            lbl2.setPrefHeight(height);
            lbl2.setFont(Font.font("", FontWeight.BOLD, 15));
            repMatrixGrid.add(lbl2, 0, 0);
            for (int i = 0; i < numberOfVertexes; i++) {
                Label lbl = new Label(vertexes.get(i));
                lbl.setAlignment(Pos.CENTER);
                lbl.setPrefWidth(width);
                lbl.setPrefHeight(height);
                lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                GridPane.setColumnIndex(lbl, i + 1);
                GridPane.setRowIndex(lbl, 0);
                lbl.setFont(Font.font("", FontWeight.BOLD, 15));
                repMatrixGrid.getChildren().add(lbl);
            }
            for (int i = 0; i < numberOfVertexes; i++) {
                Label lbl = new Label(vertexes.get(i));
                lbl.setPrefWidth(width);
                lbl.setPrefHeight(height);
                lbl.setAlignment(Pos.CENTER);
                lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                GridPane.setColumnIndex(lbl, 0);
                GridPane.setRowIndex(lbl, i + 1);
                lbl.setFont(Font.font("", FontWeight.BOLD, 15));
                repMatrixGrid.getChildren().add(lbl);
            }
            for (int i = 0; i < numberOfVertexes; i++) {
                for (int j = 0; j < numberOfVertexes; j++) {
                    Label lbl = new Label(String.valueOf(repMatrix[i][j]));
                    lbl.setAlignment(Pos.CENTER);
                    lbl.setPrefWidth(width);
                    lbl.setPrefHeight(height);
                    lbl.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                    GridPane.setColumnIndex(lbl, j + 1);
                    GridPane.setRowIndex(lbl, i + 1);
                    repMatrixGrid.getChildren().add(lbl);
                }
            }

        }

    }

    @Override
    public void run() {
        try {
            Process process = Runtime.getRuntime().exec("main.exe");
            process.waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        genrationGraphLabel.setOpacity(0);
        showGraph();
    }
}
