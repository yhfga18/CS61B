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
    long parent;

    public Node(String id, String lon, String lat) {
        this.id = Long.parseLong(id);
        this.lon = Double.parseDouble(lon);
        this.lat = Double.parseDouble(lat);
        distFromSource = 99999.9;
//        parent = this.id;
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
        return distFromSource + heuristic;
    }

    public void setF() {
        f = distFromSource + heuristic;
    }

    public void setParent(long p) {
        parent = p;
    }
    public long getParent() {
        return parent;
    }
}
