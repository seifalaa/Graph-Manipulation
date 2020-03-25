package sample;

import java.security.PublicKey;
import java.util.ArrayList;

public class Graph {
    private ArrayList<Vertex> graphVertices;
    private ArrayList<Edge> graphEdges;

    public Graph() {
        graphVertices = new ArrayList<>();
        graphEdges = new ArrayList<>();
    }

    public Graph(ArrayList<Vertex> graphVertices, ArrayList<Edge> graphEdges) {
        this.graphEdges = graphEdges;
        this.graphVertices = graphVertices;
    }

    public Vertex getVertexByName(String vertexName) {
        for (int i = 0; i < graphVertices.size(); i++) {
            if (graphVertices.get(i).getVertexName().equals(vertexName))
                return graphVertices.get(i);
        }
        return null;
    }
    public Edge getEdgeByName(String edgeName) {
        for (int i = 0; i < graphEdges.size(); i++) {
            if (graphEdges.get(i).getEdgeName().equals(edgeName))
                return graphEdges.get(i);
        }
        return null;
    }

    public boolean addVertex(String vertexName) {
        if (getVertexByName(vertexName) != null)
            return false;
        graphVertices.add(new Vertex(vertexName));
        return true;
    }
    public void addEdge(String startVertexName , String endVertexName , String edgeName){
        int startVertexIndex = findVertex(startVertexName);
        int endVertexIndex = findVertex(endVertexName);
        Vertex startVertex = graphVertices.get(startVertexIndex);
        Vertex endVertex  = graphVertices.get(endVertexIndex);
        startVertex.addAdjacentVertex(endVertex);
        endVertex.addAdjacentVertex(startVertex);
        graphEdges.add(new Edge(edgeName , startVertex , endVertex));
    }

    public boolean addDirectedEdge(String edgeName, String startVertexName, String endVertexName) {
        Vertex startVertex = getVertexByName(startVertexName);
        Vertex endVertex = getVertexByName(endVertexName);
        if (startVertex == null || endVertex == null)
            return false;
        if (getEdgeByName(edgeName) != null)
            return false;
        graphEdges.add(new Edge(edgeName, startVertex, endVertex));
        startVertex.addAdjacentVertex(endVertex);
        return true;
    }

    public boolean addUndirectedEdge(String edgeName, String startVertexName, String endVertexName) {
        Vertex startVertex = getVertexByName(startVertexName);
        Vertex endVertex = getVertexByName(endVertexName);
        if (startVertex == null || endVertex == null)
            return false;
        if (getEdgeByName(edgeName) != null)
            return false;
        graphEdges.add(new Edge(edgeName, startVertex, endVertex));
        startVertex.addAdjacentVertex(endVertex);
        if (startVertex != endVertex)
            endVertex.addAdjacentVertex(startVertex);
        return true;
    }

    public ArrayList<Vertex> createAdjacencyList() {
        return graphVertices;
    }

    public ArrayList<ArrayList<Vertex>> createDirectedAdjacencyList() {
        ArrayList<ArrayList<Vertex>> adjacencyList = new ArrayList<>();
        for (int i = 0; i < graphVertices.size(); i++) {
            Vertex vertex = graphVertices.get(i);
            adjacencyList.add(new ArrayList<>());
            for (int j = 0; j < graphEdges.size(); j++) {
                Vertex edgeStart = graphEdges.get(j).getStart();
                Vertex edgeEnd = graphEdges.get(j).getTermination();
                if (vertex.equals(edgeStart))
                    adjacencyList.get(i).add(edgeEnd);
            }
        }
        return adjacencyList;
    }


    public int[][] createDiAdjacencyMatrix() {
        int[][] diAdjacencyMatrix = new int[graphVertices.size()][graphVertices.size()];
        ArrayList<ArrayList<String>> diAdjacencyList = createDiAdjacencyList();
        for (int i = 0; i < graphVertices.size(); i++) {
            ArrayList<String> adjacentVertices = diAdjacencyList.get(i);
            for (int j = 1; j < adjacentVertices.size(); j++) {
                diAdjacencyMatrix[i][findVertex(adjacentVertices.get(j))] = 1;
            }
        }
        return diAdjacencyMatrix;
    }

    public int[][] createAdjacencyMatrix() {
        int[][] adjacencyMatrix = new int[graphVertices.size()][graphVertices.size()];
        for (int i = 0; i < graphVertices.size(); i++) {
            Vertex vertex = graphVertices.get(i);
            ArrayList<Vertex> adjacentVertices = vertex.getAdjacentVertices();
            for (Vertex adjacentVertex : adjacentVertices) {
                adjacencyMatrix[i][graphVertices.indexOf(adjacentVertex)] = 1;
            }
        }
        return adjacencyMatrix;
    }

    public int[][] createRepresentationMatrix() {
        int[][] representationMatrix = new int[graphVertices.size()][graphVertices.size()];
        for (int i = 0; i < graphVertices.size(); i++) {
            Vertex vertex = graphVertices.get(i);
            ArrayList<Vertex> adjacentVertices = vertex.getAdjacentVertices();
            ArrayList<Integer> numberOfEdgesBetweenEachVertices = vertex.getNumberOfEdgesWithEachAdjacentVertex();
            for (int j = 0; j < adjacentVertices.size(); j++) {
                representationMatrix[i][graphVertices.indexOf(adjacentVertices.get(j))] = numberOfEdgesBetweenEachVertices.get(j);
            }
        }
        return representationMatrix;
    }

    public int[][] createDiRepresentationMatrix() {
        int[][] representationMatrix = new int[graphVertices.size()][graphVertices.size()];
        ArrayList<ArrayList<String>> diAdjacencyList = createDiAdjacencyList();
        ArrayList<Edge> tempEdges = deepCopyForEdges();
        for (int i = 0; i < graphVertices.size(); i++) {
            ArrayList<String> adjacentVertices = diAdjacencyList.get(i);
            String start = adjacentVertices.get(0);
            for (int j = 1; j < adjacentVertices.size(); j++) {
                int numberOfEdgesWithEachAdjacentVertex = 0;
                String end = adjacentVertices.get(j);
                for (int s = 0; s < tempEdges.size(); s++) {
                    Edge edge = tempEdges.get(s);
                    if (edge.getStart().getVertexName().equals(start) && edge.getTermination().getVertexName().equals(end)) {
                        numberOfEdgesWithEachAdjacentVertex++;
                        tempEdges.remove(s);
                        s--;
                    }
                }
                representationMatrix[i][findVertex(end)] = numberOfEdgesWithEachAdjacentVertex;
            }
        }
        return representationMatrix;
    }

    public int findVertex(String vertexName) {
        for (int i = 0; i < graphVertices.size(); i++) {
            if (graphVertices.get(i).getVertexName().equals(vertexName)) {
                return i;
            }
        }
        return -1;
    }

    public int[][] createIncidenceMatrix(boolean directed) {
        int[][] incidenceMatrix = new int[graphVertices.size()][graphEdges.size()];
        for (int i = 0; i < graphVertices.size(); i++) {
            Vertex vertex = graphVertices.get(i);
            for (int j = 0; j < graphEdges.size(); j++) {
                Vertex edgeStart = graphEdges.get(j).getStart();
                Vertex edgeEnd = graphEdges.get(j).getTermination();
                if (vertex.equals(edgeStart))
                    incidenceMatrix[i][j] = 1;
                if (!directed) {
                    if (vertex.equals(edgeEnd))
                        incidenceMatrix[i][j] = 1;
                } else {
                    if (vertex.equals(edgeEnd))
                        incidenceMatrix[i][j] = -1;
                }
            }
        }
        return incidenceMatrix;
    }

    private ArrayList<Edge> deepCopyForEdges() {
        return new ArrayList<>(graphEdges);
    }

    private ArrayList<ArrayList<String>> createDiAdjacencyList() {
        ArrayList<Edge> temporaryEdges = deepCopyForEdges();
        ArrayList<ArrayList<String>> diAdjacencyList = new ArrayList<>();
        for (int i = 0; i < graphVertices.size(); i++) {
            Vertex vertex = graphVertices.get(i);
            ArrayList<String> adjacentVertices = new ArrayList<>();
            adjacentVertices.add(vertex.getVertexName());
            for (int j = 0; j < temporaryEdges.size(); j++) {
                Edge edge = temporaryEdges.get(j);
                if (vertex.getVertexName().equals(edge.getStart().getVertexName())) {
                    temporaryEdges.remove(j);
                    j--;
                    int index = adjacentVertices.lastIndexOf(edge.getTermination().getVertexName());
                    if (-1 == index || (index == 0)) {
                        adjacentVertices.add(edge.getTermination().getVertexName());
                    }
                }
            }
            diAdjacencyList.add(adjacentVertices);
        }
        return diAdjacencyList;
    }

    public ArrayList<Edge> getGraphEdges() {
        return graphEdges;
    }

    public ArrayList<Vertex> getGraphVertices() {
        return graphVertices;
    }

    public void coloringTheVertices() {
        int[][] adjacencyMatrix = createAdjacencyMatrix();
        int numberOfColoredVertices = 0;
        ArrayList<Integer> colored = new ArrayList<>();
        String[] colors = new String[]{"Blue", "Yellow", "Red", "Green"};
        int indexForColors = 0;
        int indexForRows = 0;
        while (numberOfColoredVertices != graphVertices.size()) {
            graphVertices.get(indexForRows).setVertexColor(colors[indexForColors]);
            numberOfColoredVertices++;
            for (int i = 0; i < adjacencyMatrix[0].length; i++) {
                int isAdjacent = adjacencyMatrix[indexForRows][i];
                if (isAdjacent == 0 && i != indexForRows && graphVertices.get(i).getVertexColor() == null && adjacencyMatrix[i][indexForRows] !=1) {
                    if (colored.isEmpty()) {
                        graphVertices.get(i).setVertexColor(colors[indexForColors]);
                        numberOfColoredVertices++;
                        colored.add(i);
                    } else {
                        boolean canColor = true;
                        for (Integer integer : colored) {
                            if (adjacencyMatrix[i][integer] == 1) {
                                canColor = false;
                                break;
                            }
                        }
                        if (canColor) {
                            graphVertices.get(i).setVertexColor(colors[indexForColors]);
                            numberOfColoredVertices++;
                        }
                    }
                }
                if(isAdjacent==1)
                {
                    adjacencyMatrix[i][indexForRows]=1;
                }
            }
            indexForColors++;
            indexForRows++;
            colored.clear();

        }
        for(int i=0;i<graphVertices.size();i++)
        {
            System.out.println(graphVertices.get(i).getVertexColor());
        }
    }
}