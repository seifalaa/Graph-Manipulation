package sample;

import java.util.ArrayList;

public class AdjListTableContent {
    private String vertex;
    private ArrayList<String>adjacent;

    public AdjListTableContent(String vertex, ArrayList<String> adjacent) {
        this.vertex = vertex;
        this.adjacent = adjacent;
    }

    public String getVertex() {
        return vertex;
    }

    public void setVertex(String vertex) {
        this.vertex = vertex;
    }

    public ArrayList<String> getAdjacent() {
        return adjacent;
    }

    public void setAdjacent(ArrayList<String> adjacent) {
        this.adjacent = adjacent;
    }
}
