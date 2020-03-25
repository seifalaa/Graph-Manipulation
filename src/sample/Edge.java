
package sample;

public class Edge {
    private Vertex start;
    private Vertex termination;
    private String edgeName;

    public Edge(String edgeName, Vertex start,Vertex termination)
    {
        this.edgeName=edgeName;
        this.start=start;
        this.termination=termination;
    }
    public Vertex getStart() {
        return start;
    }

    public Vertex getTermination() {
        return termination;
    }

    public String getEdgeName() {
        return edgeName;
    }

}