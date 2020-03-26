
package sample;
public class Edge implements Comparable<Edge>  {
    private Vertex start;
    private Vertex termination;
    private String edgeName;
    private int weight;
    public Edge(String edgeName, int weight,Vertex start,Vertex termination)
    {
        this.edgeName=edgeName;
        this.start=start;
        this.termination=termination;
        this.weight=weight;
    }


    Edge() {
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
    public int getWeight() {
        return weight;
    }

    @Override
    public int compareTo(Edge t) {

        return this.weight-t.weight;

    }

}