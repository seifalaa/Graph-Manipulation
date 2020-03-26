package sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


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
        for (Vertex graphVertex : graphVertices) {
            if (graphVertex.getVertexName().equals(vertexName))
                return graphVertex;
        }
        return null;
    }

    public Edge getEdgeByName(String edgeName) {
        for (Edge graphEdge : graphEdges) {
            if (graphEdge.getEdgeName().equals(edgeName))
                return graphEdge;
        }
        return null;
    }

    public void addVertex(String vertexName) {
        if (getVertexByName(vertexName) != null)
            return;
        graphVertices.add(new Vertex(vertexName, graphVertices.size()));
    }

    public void addDirectedEdge(String edgeName, int edgeWeight, String startVertexName, String endVertexName) {
        Vertex startVertex = getVertexByName(startVertexName);
        Vertex endVertex = getVertexByName(endVertexName);
        if (startVertex == null || endVertex == null)
            return;
        if (getEdgeByName(edgeName) != null)
            return;
        graphEdges.add(new Edge(edgeName, edgeWeight, startVertex, endVertex));
        startVertex.addAdjacentVertex(endVertex);
    }

    public void addUndirectedEdge(String edgeName, int edgeWeight, String startVertexName, String endVertexName) {
        Vertex startVertex = getVertexByName(startVertexName);
        Vertex endVertex = getVertexByName(endVertexName);
        if (startVertex == null || endVertex == null)
            return;
        if (getEdgeByName(edgeName) != null)
            return;
        graphEdges.add(new Edge(edgeName, edgeWeight, startVertex, endVertex));
        startVertex.addAdjacentVertex(endVertex);
        if (startVertex != endVertex)
            endVertex.addAdjacentVertex(startVertex);
    }

    public ArrayList<Vertex> createAdjacencyList() {
        return graphVertices;
    }

    public ArrayList<ArrayList<Vertex>> createDirectedAdjacencyList() {
        ArrayList<ArrayList<Vertex>> adjacencyList = new ArrayList<>();
        for (int i = 0; i < graphVertices.size(); i++) {
            Vertex vertex = graphVertices.get(i);
            adjacencyList.add(new ArrayList<>());
            for (Edge graphEdge : graphEdges) {
                Vertex edgeStart = graphEdge.getStart();
                Vertex edgeEnd = graphEdge.getTermination();
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
        for (Vertex vertex : graphVertices) {
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
                if (isAdjacent == 0 && i != indexForRows && graphVertices.get(i).getVertexColor() == null && adjacencyMatrix[i][indexForRows] != 1) {
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
                if (isAdjacent == 1) {
                    adjacencyMatrix[i][indexForRows] = 1;
                }
            }
            indexForColors++;
            indexForRows++;
            colored.clear();

        }

    }

    ArrayList<Edge> Kruskal() {
        DisjointSets dis = new DisjointSets();
        ArrayList<Edge> edges = new ArrayList<>(graphEdges);
        Collections.sort(edges);

        ArrayList<Edge> result = new ArrayList<>();
        for (Vertex graphVertex : graphVertices) {
            dis.create_set(graphVertex.getVertexName());

        }

        for (Edge edge : edges) {

            String s1 = dis.find_set(edge.getStart().getVertexName());
            String s2 = dis.find_set(edge.getTermination().getVertexName());

            if (!s1.equals(s2)) {
                result.add(edge);
                dis.union(s1, s2);

            }


        }

        return result;
    }

    public String directedFleuryAlgorithm() {
        ArrayList<Vertex> vertices = deepCopyForVertices();
        int canBeDirectedEuler = canBeDirectedEuler();
        StringBuilder route;
        Vertex startVertex;
        if (canBeDirectedEuler == -1) {
            return "No Euler path and no Euler circuit";
        } else if (canBeDirectedEuler > -1) {
            route = new StringBuilder("Euler path : ");
            startVertex = vertices.get(canBeDirectedEuler);
        } else {
            route = new StringBuilder("Euler circuit : ");
            startVertex = vertices.get(0);
        }
        startMoving(startVertex, route, vertices, true);
        return route.toString();
    }

    public String fleuryAlgorithm() {
        ArrayList<Vertex> vertices = deepCopyForVertices();
        int canBeEuler = canBeEuler();
        StringBuilder route;
        Vertex startVertex;
        if (canBeEuler == -1) {
            return "No Euler path and no Euler circuit";
        } else if (canBeEuler == 1) {
            route = new StringBuilder("Euler path : ");
            startVertex = vertices.get(getOddVertex());
        } else {
            route = new StringBuilder("Euler circuit : ");
            startVertex = vertices.get(0);
        }
        startMoving(startVertex, route, vertices, false);
        return route.toString();
    }

    private void startMoving(Vertex vertex, StringBuilder route, ArrayList<Vertex> vertices, boolean isDirected) {
        if (vertex.getAdjacentVertices().size() == 0) {
            route.append(vertex.getVertexName());
            return;
        }
        ArrayList<Vertex> adjacentVertices = vertex.getAdjacentVertices();
        for (int i = 0; i < adjacentVertices.size(); i++) {
            Vertex adjacentVertex = adjacentVertices.get(i);
            if (isValidNextEdge(vertex, adjacentVertex, i, vertices, isDirected)) {
                route.append(vertex.getVertexName()).append(" -> ");
                removeEdge(vertex, adjacentVertex, isDirected);
                startMoving(adjacentVertex, route, vertices, isDirected);
            }
        }
    }

    private boolean isValidNextEdge(Vertex startVertex, Vertex endVertex, int indexOfEnd, ArrayList<Vertex> vertices, boolean isDirected) {
        if (startVertex.getAdjacentVertices().size() == 1) {
            return true;
        }
        boolean[] isVisited = new boolean[vertices.size()];
        int numberOfEdgesThatCanBeVisited = dfsCount(startVertex, isVisited);
        removeEdge(startVertex, endVertex, isDirected);
        isVisited = new boolean[vertices.size()];
        int numberOfEdgesThatCanBeVisitedAfterRemoving = dfsCount(endVertex, isVisited);
        addEdge(startVertex, endVertex, indexOfEnd, isDirected);
        return !(numberOfEdgesThatCanBeVisited > numberOfEdgesThatCanBeVisitedAfterRemoving);
    }

    private int dfsCount(Vertex vertex, boolean[] isVisited) {
        isVisited[vertex.getIndex()] = true;
        int counter = 1;
        for (Vertex adjacentVertex : vertex.getAdjacentVertices()) {
            if (!isVisited[adjacentVertex.getIndex()]) {
                counter = counter + dfsCount(adjacentVertex, isVisited);
            }
        }
        return counter;
    }

    private int canBeDirectedEuler() {
        int outGoDegree;
        int inGoDegree;
        int numberOfVerticesWhereOutBiggerThanIn = 0;
        int numberOfVerticesWhereInBiggerThanOut = 0;
        int indexOfStartVertex = -1;
        for (int i = 0; i < graphVertices.size(); i++) {
            Vertex vertex = graphVertices.get(i);
            outGoDegree = vertex.getAdjacentVertices().size();
            inGoDegree = 0;
            for (Edge graphEdge : graphEdges) {
                if (graphEdge.getTermination().getVertexName().equals(vertex.getVertexName())) {
                    inGoDegree++;
                }
            }
            if (outGoDegree != inGoDegree) {
                int difference = outGoDegree - inGoDegree;
                if (difference == 1 && numberOfVerticesWhereOutBiggerThanIn == 0) {
                    numberOfVerticesWhereOutBiggerThanIn++;
                    indexOfStartVertex = i;
                } else if (difference == -1 && numberOfVerticesWhereInBiggerThanOut == 0) {
                    numberOfVerticesWhereInBiggerThanOut++;
                } else {
                    return -1;
                }
            }
        }
        if (indexOfStartVertex > -1) {
            return indexOfStartVertex;
        }
        indexOfStartVertex = -2;
        return indexOfStartVertex;
    }

    private ArrayList<Vertex> deepCopyForVertices() {
        ArrayList<Vertex> tempVertices = new ArrayList<>();
        for (Vertex vertex : graphVertices) {
            tempVertices.add(new Vertex(vertex.getVertexName(), vertex.getIndex()));
        }
        for (int i = 0; i < graphVertices.size(); i++) {
            Vertex vertex = graphVertices.get(i);
            ArrayList<Vertex> adjacentVertices = vertex.getAdjacentVertices();
            for (Vertex adjacentVertex : adjacentVertices) {
                tempVertices.get(i).addAdjacentVertex(tempVertices.get(adjacentVertex.getIndex()));
            }
        }
        return tempVertices;
    }

    private void removeEdge(Vertex startVertex, Vertex endVertex, boolean isDirected) {

        startVertex.getAdjacentVertices().remove(endVertex);
        if (!isDirected) {
            endVertex.getAdjacentVertices().remove(startVertex);
        }
    }

    private void addEdge(Vertex startVertex, Vertex endVertex, int index, boolean isDirected) {
        if (isDirected) {
            startVertex.getAdjacentVertices().add(index, endVertex);
        } else {
            startVertex.getAdjacentVertices().add(endVertex);
            endVertex.getAdjacentVertices().add(startVertex);
        }
    }

    private int canBeEuler() {
        int numberOfOdd = 0;
        for (Vertex graphVertex : graphVertices) {
            if ((graphVertex.getAdjacentVertices().size()) % 2 == 1) {
                numberOfOdd++;
            }
        }
        if (numberOfOdd > 2 || numberOfOdd == 1) {
            return -1;//no euler path or circle
        } else if (numberOfOdd == 2) {
            return 1; // euler path
        } else {
            return 0; // euler circle
        }
    }

    private int getOddVertex() {
        for (int i = 0; i < graphVertices.size(); i++) {
            if ((graphVertices.get(i).getAdjacentVertices().size()) % 2 == 1) {
                return i;
            }
        }
        return -1;
    }
    private int[][] createAdjacencyMatrixSMP() {
        int[][] adjacencyMatrix = new int[graphVertices.size()][graphVertices.size()];
        for (int i = 0; i < graphEdges.size(); i++) {
            Edge e = graphEdges.get(i);
            Vertex startVertex = e.getStart();
            Vertex termVertex = e.getTermination();
            int node1=0,node2=0;
            for (int j = 0; j < graphVertices.size(); j++) {
                if (startVertex.equals(graphVertices.get(j))) {
                    node1 = j;
                }
                if (termVertex.equals(graphVertices.get(j))) {
                    node2 = j;
                }
            }
            adjacencyMatrix[node1][node2] = e.getWeight();
            adjacencyMatrix[node2][node1] = e.getWeight();
        }
        return adjacencyMatrix;
    }

    public  ArrayList<Edge> runSalesManProblem() {
        ArrayList<Edge> result = new ArrayList<>();
        int [][]adjMatrix = createAdjacencyMatrixSMP();
        int numberOfVertices = graphVertices.size();
        ArrayList<Integer> nodes = getMinimumRoute(adjMatrix,numberOfVertices);
        for(int i=0;i<nodes.size();i++)
        {
            System.out.println(nodes.get(i));
        }
        for (int i = 0; i < nodes.size()-2 ; i++) {
            Vertex startV = graphVertices.get(i);

            Vertex endV = graphVertices.get(i+1);

            //System.out.println("Start "+startV.getVertexName()+" "+"End "+endV.getVertexName());
            for (int j=0;j<graphEdges.size();j++) {
                Edge e = graphEdges.get(j);
                if (startV.equals(e.getStart()) && endV.equals(e.getTermination())) {
                    result.add(e);
                }
                if (startV.equals(e.getTermination()) && endV.equals(e.getStart())) {
                    result.add(e);
                }


            }
        }


        return result;

    }

    private   ArrayList<Integer> getMinimumRoute(int [][]adjMatrix,int nVertices) {
        ArrayList<Integer> minRout = new ArrayList<>();
        ArrayList<Integer> visited = new ArrayList<>();
        int n = nVertices;
        int startNode = 0;
        int node = startNode;
        int minCost , minCostIndex ;
        visited.add(node);
        while (visited.size() != n) {
            minCost = Integer.MAX_VALUE;
            minCostIndex = 0;

            for (int i = 0; i < n; i++) {
                if( !visited.contains(i) && adjMatrix[node][i] < minCost  ) {
                    //System.out.println(node + " ** " + i);
                    minCostIndex = i;
                    minCost = adjMatrix[node][i];
                }
            }
            System.out.println(minCostIndex);
            //visited.add(minCostIndex);
            minRout.add(minCost);
            node = minCostIndex;
            visited.add(node);
        }
        minRout.add(adjMatrix[startNode][node]);
        visited.add(startNode);
        //System.out.println("minrout"+minRout.size());


        System.out.println("visSIze "+visited.size());
        return visited;
    }
    public boolean checkSafe(int v, int graph[][], ArrayList<Integer> path, int pos){
        if (graph[path.get(pos - 1)][v] == 0)
            return false;
        for (int i = 0; i < pos; i++)
            if (path.get(i) == v)
                return false;
        return true;
    }
    public boolean createHamiltonCycle(int graph[][], ArrayList<Integer> path, int pos){
        if (pos == graphVertices.size()) {
            if (graph[path.get(pos - 1)][path.get(0)] == 1)
                return true;
            else
                return false;
        }
        for (int v = 1; v < graphVertices.size(); v++){
            if (checkSafe(v, graph, path, pos)) {
                path.set(pos,v);
                if (createHamiltonCycle(graph, path,pos + 1))
                    return true;
                path.set(pos,-1);
            }
        }
        return false;
    }
    public boolean createHamiltonPath(int graph[][], ArrayList<Integer> path, int pos){
        if (pos == graphVertices.size())
            return true;
        for (int v = 1; v < graphVertices.size(); v++){
            if (checkSafe(v, graph, path, pos)) {
                path.set(pos,v);
                if (createHamiltonPath(graph, path,pos + 1))
                    return true;
                path.set(pos,-1);
            }
        }
        return false;
    }
    public ArrayList<Integer> hamilton(String cycleOrPath){
        ArrayList<Integer> path = new ArrayList<>(graphVertices.size());
        int[][] graph = createAdjacencyMatrix();
        for (int i = 0; i < graphVertices.size(); i++)
            path.add(-1);
        path.set(0,0);
        if(cycleOrPath.equals("cycle")) {
            if (createHamiltonCycle(graph, path, 1)) {
                path.add(0);
                return path;
            }
        }
        else if(cycleOrPath.equals("path")) {
            if (createHamiltonPath(graph, path, 1))
                return path;
        }
        return null;
    }
    public ArrayList<Integer> DiHamilton(String cycleOrPath){
        ArrayList<Integer> path = new ArrayList<>(graphVertices.size());
        int[][] graph = createDiAdjacencyMatrix();
        for (int i = 0; i < graphVertices.size(); i++)
            path.add(-1);
        path.set(0,0);
        if(cycleOrPath.equals("cycle")) {
            if (createHamiltonCycle(graph, path, 1)) {
                path.add(0);
                return path;
            }
        }
        else if(cycleOrPath.equals("path")) {
            if (createHamiltonPath(graph, path, 1))
                return path;
        }
        return null;
    }

    public ArrayList<String> fullHamilton(){
        String cycle = "Hamilton Circuit: ", path = "Hamilton Path: ";
        ArrayList<String> result = new ArrayList<>();
        ArrayList<Integer> listCycle = hamilton("cycle");
        if(listCycle != null) {
            for (int i = 0; i < listCycle.size(); i++) {
                cycle += "" + graphVertices.get(listCycle.get(i)).getVertexName();
                if (i + 1 != listCycle.size())
                    cycle += " -> ";
            }
        }
        else cycle = "No Hamilton Circuit\n";
        ArrayList<Integer> listPath = hamilton("path");
        if(listPath != null) {
            for (int i = 0; i < listPath.size(); i++) {
                path += "" + graphVertices.get(listPath.get(i)).getVertexName();
                if (i + 1 != listPath.size())
                    path += " -> ";
            }
        }
        else path = "No Hamilton Path\n";
        result.add(cycle);
        result.add(path);
        return result;
    }
    public ArrayList<String> diFullHamilton(){
        String cycle = "Hamilton Circuit: ", path = "Hamilton Path: ";
        ArrayList<String> result = new ArrayList<>();
        ArrayList<Integer> listCycle = DiHamilton("cycle");
        if(listCycle != null) {
            for (int i = 0; i < listCycle.size(); i++) {
                cycle += "" + graphVertices.get(listCycle.get(i)).getVertexName();
                if (i + 1 != listCycle.size())
                    cycle += " -> ";
            }
        }
        else cycle = "No Hamilton Circuit\n";
        ArrayList<Integer> listPath = DiHamilton("path");
        if(listPath != null) {
            for (int i = 0; i < listPath.size(); i++) {
                path += "" + graphVertices.get(listPath.get(i)).getVertexName();
                if (i + 1 != listPath.size())
                    path += " -> ";
            }
        }
        else path = "No Hamilton Path\n";
        result.add(cycle);
        result.add(path);
        return result;
    }
    public ArrayList<Edge> directedMinimumSpanningTree()
    {
        ArrayList<Edge> result = new ArrayList<>();
        ArrayList<Vertex> tempVertices = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>(graphEdges);
        edges.sort(new SortEdge());
        for(int i = 0;i < graphVertices.size();i++)
        {
            tempVertices.add(new Vertex(graphVertices.get(i).getVertexName()));
        }
        for(int i = 0;i < edges.size();i++)
        {
            ArrayList<Vertex> visited = new ArrayList<>();
            Edge e = edges.get(i);
            System.out.println(e.getEdgeName() + " " + e.getStart().getVertexName() + " " + e.getTermination().getVertexName());
            Vertex Start = findInTemp(e.getStart(),tempVertices);
            Vertex End = findInTemp(e.getTermination(),tempVertices);
            dfs(Start,visited);
            if(!visited.contains(End))
            {
                Start.addAdjacentVertex(End);
                result.add(e);
            }
        }
        return result;
    }

    private Vertex findInTemp(Vertex vertex, ArrayList<Vertex> tempVertices) {
        for(int i = 0;i < tempVertices.size();i++)
        {
            if(vertex.getVertexName().equals(tempVertices.get(i).getVertexName()))
                return tempVertices.get(i);
        }
        return null;
    }

    void dfs(Vertex temp, ArrayList<Vertex> visited)
    {
        visited.add(temp);
        for(int i = 0;i < temp.getAdjacentVertices().size();i++)
            if(!visited.contains(temp.getAdjacentVertices().get(i)))
                dfs(temp.getAdjacentVertices().get(i),visited);
    }
    static class SortEdge implements Comparator<Edge> {
        public int compare(Edge e1, Edge e2) {
            return Integer.compare(e1.getWeight(), e2.getWeight());
        }
    }

}