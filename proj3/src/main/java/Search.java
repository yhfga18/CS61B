import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by hideyoshitakahashi on 4/20/17.
 */
public class Search {
    public static final double ROOT_ULLAT = 37.892195547244356, ROOT_ULLON = -122.2998046875,
            ROOT_LRLAT = 37.82280243352756, ROOT_LRLON = -122.2119140625;
    QuadTree quadTree;
    private double ULLAT;
    private double ULLON;
    private double LRLAT;
    private double LRLON;
    private double width;
    //    private double Height;
    private double LONDPP;
    double rasterUllon = MapServer.ROOT_ULLON;
    double rasterUllat = MapServer.ROOT_ULLAT;
    double rasterLrlon = MapServer.ROOT_LRLON;
    double rasterLrlat = MapServer.ROOT_LRLAT;
    int rootDepth = 0;
    int ResultingDepth = 0;
    boolean querySuccess = true;
    Map<Double, LinkedList<Integer>> fileMap;
    LinkedList<Double> latitudeList;
    String img;
    int picCounter;
    Map<String, Object> results;

    public Search(Map<String, Double> params, String img) {
        this.ULLAT = params.get("ullat");
        this.ULLON = params.get("ullon");
        this.LRLAT = params.get("lrlat");
        this.LRLON = params.get("lrlon");
        this.width = params.get("w");
//        this.Height = params.get("h");
        this.LONDPP = (LRLON - ULLON) / width;
        latitudeList = new LinkedList<>(); // LinkedList<Double>
        picCounter = 0;
        results = new HashMap<>();

        ////////////////////////////////////////////////////////////////////////////////
        fileMap = new HashMap<>(); // Map<Double, LinkedList<Long>> fileMap;
        ////////////////////////////////////////////////////////////////////////////////

        results = new HashMap<>();

        // call
        search(0, rasterUllat, rasterUllon, rasterLrlat, rasterLrlon, 0, rootDepth);  // id, mapの位置０
        String[][] resultMap = new String[latitudeList.size()][picCounter / latitudeList.size()];

        if (querySuccess) {
            for (int i = 0; i < latitudeList.size(); i++) {
                double keyLat = latitudeList.get(i);
                LinkedList<Integer> l = fileMap.get(keyLat);
                String[] newL = new String[l.size()];
                for (int j = 0; j < l.size(); j++) {
                    newL[j] = img + Long.toString(l.get(j)) + ".png";
                }
                resultMap[i] = newL;
            }
        }

        int l1 = resultMap.length - 1;
        int l2 = resultMap[l1].length - 1;

        results.put("render_grid", resultMap);
        results.put("raster_ul_lon", rasterUllon);
        results.put("raster_ul_lat", rasterUllat);
        results.put("raster_lr_lon", rasterLrlon);
        results.put("raster_lr_lat", rasterLrlat);
        results.put("depth", ResultingDepth);
        results.put("query_success", querySuccess);
    }

    public Map<String, Object> getResult() {
        return results;
    }

    private void search(int current, double parentULLAT, double parentULLON,
                        double parentLRLAT, double parentLRLON, int type, int depth) {

        double ullat = parentULLAT;
        double ullon = parentULLON;
        double lrlat = parentLRLAT;
        double lrlon = parentLRLON;

        // treating self...
        if (type == 1) {
            lrlat = (ullat + lrlat) / 2;
            lrlon = (ullon + lrlon) / 2;
        } else if (type == 2) {
            ullon = (ullon + lrlon) / 2;
            lrlat = (ullat + lrlat) / 2;
        } else if (type == 3) {
            ullat = (ullat + lrlat) / 2;
            lrlon = (ullon + lrlon) / 2;
        } else if (type == 4) {
            ullat = (ullat + lrlat) / 2;
            ullon = (ullon + lrlon) / 2;
        }

        if (P1(ullat, ullon, lrlat, lrlon)) {
                    if (depth == 0) {
                querySuccess = false;
            }
            return;
        }


        if (P2(ullon, lrlon, depth)) {
            search(current * 10 + 1, ullat, ullon,
                    lrlat, lrlon, 1, depth + 1);
            search(current * 10 + 2, ullat, ullon,
                    lrlat, lrlon, 2, depth + 1);
            search(current * 10 + 3, ullat, ullon,
                    lrlat, lrlon, 3, depth + 1);
            search(current * 10 + 4, ullat, ullon,
                    lrlat, lrlon, 4, depth + 1);
            return;
        }

        if (!(fileMap.containsKey(ullat))) {
            if (fileMap.isEmpty()) {
                rasterUllon = ullon;
                rasterUllat = ullat;
            }
            latitudeList.addLast(ullat);
            LinkedList<Integer> list = new LinkedList<>();
            list.addLast(current);
            fileMap.put(ullat, list);
        } else {
            fileMap.get(ullat).addLast(current);
        }

        rasterLrlat = lrlat;
        rasterLrlon = lrlon;
        ResultingDepth = depth;
        picCounter++;
    }

    private boolean P1(double ullat, double ullon, double lrlat, double lrlon) {

        boolean cond1 = (ullat < LRLAT); // lat 1
        boolean cond2 = (lrlon < ULLON); // lon 1
        boolean cond3 = (lrlat > ULLAT); // lat 2
        boolean cond4 = (ullon > LRLON); // lon 2
        return cond1 || cond2 || cond3 || cond4;
    }

    private boolean P2(double ullon, double lrlon, int depth) {
        return ((lrlon - ullon) / 256) > LONDPP && (depth < 7);
    }

}



