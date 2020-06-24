package sample;

public class DijkstraTableData {
    private String path;
    private String cost;

    public DijkstraTableData(String path , String cost)
    {
        this.path = path;
        this.cost = cost;
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
