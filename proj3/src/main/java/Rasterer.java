import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.List;
import java.util.LinkedList;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    // Recommended: QuadTree instance variable. You'll need to make
    //              your own QuadTree since there is no built-in quadtree in Java.

    /** imgRoot is the name of the directory containing the images.
     *  You may not actually need this for your class. */

    QuadTree quadTree;
    double raster_ul_lon;
    double raster_ul_lat;
    double raster_lr_lon;
    double raster_lr_lat;
    double depth;
    boolean query_success = true;
    List<QuadTree.Node> fileList;


    public Rasterer(String imgRoot) { // linear
        // YOUR CODE HERE
        Map<String, Double> rootScale = new HashMap<>();
        fileList = new LinkedList<QuadTree.Node>();
        rootScale.put("ullat", MapServer.ROOT_ULLAT);
        rootScale.put("ullon", MapServer.ROOT_ULLON);
        rootScale.put("lrlat", MapServer.ROOT_LRLAT);
        rootScale.put("lrlon", MapServer.ROOT_LRLON);
        rootScale.put("w", 256.0);
        quadTree = new QuadTree(rootScale);
    }


    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>Has dimensions of at least w by h, where w and h are the user viewport width
     *         and height.</li>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     * </p>
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified:
     * "render_grid"   -> String[][], the files to display
     * "raster_ul_lon" -> Number, the bounding upper left longitude of the rastered image <br>
     * "raster_ul_lat" -> Number, the bounding upper left latitude of the rastered image <br>
     * "raster_lr_lon" -> Number, the bounding lower right longitude of the rastered image <br>
     * "raster_lr_lat" -> Number, the bounding lower right latitude of the rastered image <br>
     * "depth"         -> Number, the 1-indexed quadtree depth of the nodes of the rastered image.
     *                    Can also be interpreted as the length of the numbers in the image
     *                    string. <br> // recursion の数
     * "query_success" -> Boolean, whether the query was able to successfully complete. Don't
     *                    forget to set this to true! <br>
     * //@see #REQUIRED_RASTER_REQUEST_PARAMS
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        Map<String, Object> results = new HashMap<>();
        QuadTree.Node root = quadTree.getRoot();
        search(params, root, 1);
        String[][] imageFiles = listToArray(fileList);
        results.put("render_grid", imageFiles);
        results.put("raster_ul_lon", raster_ul_lon);
        results.put("raster_ul_lat", raster_ul_lat);
        results.put("raster_lr_lon", raster_lr_lon);
        results.put("raster_lr_lat", raster_lr_lat);
        results.put("depth", depth);
        results.put("query_success", query_success);
        return results;
    }

    private void search(Map<String, Double> params, QuadTree.Node node, int d) {
        if (node == null) {
            return;
        }
        if (!(P1(params, node))) {
            if (d == 1) {
                query_success = false;
            }
            return;
        }
        if ((P1(params, node)) && !(P2(params, node))) {
            if (node.getSub1() == null) {return;}
            search(params, node.getSub1(), d + 1);
            search(params, node.getSub2(), d + 1);
            search(params, node.getSub3(), d + 1);
            search(params, node.getSub4(), d + 1);
            return;
        }
        if ((P1(params, node)) & P2(params, node)) {
            fileList.add(node);
            depth = d;
            //depth = node.getDepth();
            Map<String, Double> m = node.getPicScale();
            Double ullon = m.get("ullon");
            Double ullat = m.get("ullat");
            Double lrlon = m.get("lrlon");
            Double lrlat = m.get("lrlat");
            if (raster_ul_lon < ullon) {
                raster_ul_lon = ullon;
            }
            if (raster_ul_lat > ullat) {
                raster_ul_lat = ullat;
            }
            if (raster_lr_lon < lrlon) {
                raster_lr_lon = lrlon;
            }
            if (raster_lr_lat > lrlat) {
                raster_lr_lat = lrlat;
            }

        }
        return;
    }

    private boolean P1(Map<String, Double> params, QuadTree.Node node) {
        Map<String, Double> picScale = node.getPicScale();
        boolean cond1 = (picScale.get("ullat") > params.get("lrlat")); // lat 1
        boolean cond2 = (picScale.get("lrlat") < params.get("ullat")); // lat 2
        boolean cond3 = (picScale.get("lrlon") > params.get("ullon")); // lon 1
        boolean cond4 = (picScale.get("ullon") < params.get("lrlon")); // lon 2
        return cond1 || cond2 || cond3 || cond4;
    }

    private boolean P2(Map<String, Double> params, QuadTree.Node node) {
        Map<String, Double> picScale = node.getPicScale();
        double nodeLonDPP = lonDPP(picScale);
        double rasterLonDPP = lonDPP(params);
        return rasterLonDPP >= nodeLonDPP;
        }

    private double lonDPP(Map<String, Double> params) {
        //double q_upperLeftlat = params.get("ullat");
        double q_upperLeftlon = params.get("ullon");
        //double q_lowerRightlat = params.get("lrlon");
        double q_lowerRightlon = params.get("lrlat");
        double q_width = params.get("w");
        return (q_lowerRightlon - q_upperLeftlon) / q_width;
    }

    private String[][] listToArray(List<QuadTree.Node> fileList){
        Map<Double, LinkedList<QuadTree.Node>> map = new HashMap<>();
        for (QuadTree.Node node : fileList) {
            Double ullat = node.getPicScale().get("ullat");
            if (!(map.containsKey(ullat))) {
                map.put(ullat, new LinkedList<QuadTree.Node>());
            }
        }
        for (QuadTree.Node node : fileList) {
            Double ullat = node.getPicScale().get("ullat");
            LinkedList<QuadTree.Node> list = map.get(ullat);
            list.add(node);
        }
        /*
        for (Double key : map.keySet()) {
            LinkedList<QuadTree.Node> list = map.get(key);
            list.sort();
        }
        */
        Double[] latitudeSet = map.keySet().toArray(new Double[1]);
        java.util.Arrays.sort(latitudeSet);
        String[][] returnArray = new String[latitudeSet.length][];
        for (int i = 0; i < latitudeSet.length; i++) {
            LinkedList<QuadTree.Node> list = map.get(latitudeSet[i]);
            LinkedList<String> latList = new LinkedList<>();
            for (QuadTree.Node node : list) {
                latList.add(Double.toString(node.getPicScale().get("ullat")));
            }
            returnArray[i] = latList.toArray(new String[0]);
        }
        return returnArray;
    }

    public static void main(String[] args) {

    }

    /*
    class NodeComparator implements Comparator<QuadTree.Node> {

    }
    */
}



/*

    private List<QuadTree.Node> search(Map<String, Double> params, QuadTree.Node node) {
        List<QuadTree.Node> fileList = new LinkedList<>();
        if (node == null) {
            return fileList;
        }
        if ((P1(params, node))) {
            return fileList;
        } else if (!(P1(params, node)) && !(P2(params, node))) {
            if (node.getSub1() == null) {return fileList;}
            List<QuadTree.Node> fileList1 = search(params, node.getSub1());
            List<QuadTree.Node> fileList2 = search(params, node.getSub2());
            List<QuadTree.Node> fileList3 = search(params, node.getSub3());
            List<QuadTree.Node> fileList4 = search(params, node.getSub4());
            fileList.addAll(fileList1);
            fileList.addAll(fileList2);
            fileList.addAll(fileList3);
            fileList.addAll(fileList4);
        } else if (P1(params, node) & P2(params, node)) {
            fileList.add(node);
            depth = node.getDepth();
            Map<String, Double> m = node.getPicScale();
            Double ullon = m.get("ullon");
            Double ullat = m.get("ullat");
            Double lrlon = m.get("lrlon");
            Double lrlat = m.get("lrlat");
            if (raster_ul_lon < ullon) {
                raster_ul_lon = ullon;
            }
            if (raster_ul_lat > ullat) {
                raster_ul_lat = ullat;
            }
            if (raster_lr_lon < lrlon) {
                raster_lr_lon = lrlon;
            }
            if (raster_lr_lat > lrlat) {
                raster_lr_lat = lrlat;
            }

        }
        return fileList;
    }


*/