import java.util.LinkedList;

/**
 * Created by hideyoshitakahashi on 4/20/17.
 */
public class Node {
    long id;
    double lon;
    double lat;
    double distFromSource;
    double heuristic;
    double f;

    public Node(String id, String lon, String lat) {
        this.id = Long.parseLong(id);
        this.lon = Double.parseDouble(lon);
        this.lat = Double.parseDouble(lat);
        distFromSource = 9999999999999.9;
    }
    public long getId() {
        return id;
    }
    public double getLon() {
        return lon;
    }
    public double getLat() {
        return lat;
    }


    public double getDistFromSource() {
        return distFromSource;
    }
    public void setDistFromSource(Node prev) {
        distFromSource = distFromSource + prev.getDistFromSource();
    }
    public void setDistFromSource(double d) {
        distFromSource = d;
    }
    public double getHeuristic() {
        return heuristic;
    }
    public void setHeuristic(double h) {
        heuristic = h;
    }
    public double getF() {
        return f;
    }
    public void setF() {
        f = distFromSource + heuristic;
    }
}
