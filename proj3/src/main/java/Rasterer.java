import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;

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
    /*
    QuadTree quadTree;
    String raster_ul_lon = "";
    String raster_ul_lat = "";
    String raster_lr_lon = "";
    String raster_lr_lat = "";
    String depth;
    boolean query_success = true;
    List<QuadTree.Node> fileList;
    int numOfPic = 0;
    int dup = 0;
    String img;
    */
    QuadTree quadTree;
    double raster_ul_lon = MapServer.ROOT_ULLON;
    double raster_ul_lat = MapServer.ROOT_ULLAT;
    double raster_lr_lon = MapServer.ROOT_LRLON;
    double raster_lr_lat = MapServer.ROOT_LRLAT;
    int depth;
    boolean query_success = true;
    List<QuadTree.Node> fileList;
    int numOfPic = 0;
    int dup = 0;
    String img;



    public Rasterer(String imgRoot) { // linear
        // YOUR CODE HERE
        img = imgRoot;
        Map<String, Double> rootScale = new HashMap<>();
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
        fileList = new LinkedList<QuadTree.Node>();
        Map<String, Object> results = new HashMap<>();
        QuadTree.Node root = quadTree.getRoot();
        search(params, root, 0);
        String[][] imageFiles = new String[1][];
        if (query_success) {
            imageFiles = listToArray(fileList);
        }
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
        if ((P1(params, node))) {
            if (d == 0) {
                query_success = false;
            }
            return;
        }
        if (!(P2(params, node, d))) {
            search(params, node.getSub1(), d + 1);
            search(params, node.getSub2(), d + 1);
            search(params, node.getSub3(), d + 1);
            search(params, node.getSub4(), d + 1);
            return;
        }
        //if (!(P1(params, node)) && P2(params, node, d)) {
        //if (fileList.contains(node)) {dup++;}
        fileList.add(node);
        numOfPic += 1;
        depth = (int) d;


        //}
        return;
    }

    private boolean P1(Map<String, Double> params, QuadTree.Node node) {
        Map<String, Double> picScale = node.getPicScale();
        boolean cond1 = (picScale.get("ullat") < params.get("lrlat")); // lat 1
        boolean cond2 = (picScale.get("lrlon") < params.get("ullon")); // lon 1
        boolean cond3 = (picScale.get("lrlat") > params.get("ullat")); // lat 2
        boolean cond4 = (picScale.get("ullon") > params.get("lrlon")); // lon 2
        return cond1 || cond2 || cond3 || cond4;
    }

    private boolean P2(Map<String, Double> params, QuadTree.Node node, int depth) {
        if (depth >= 7) {return true;}
        Map<String, Double> picScale = node.getPicScale();
        double nodeLonDPP = lonDPP(picScale);
        double rasterLonDPP = lonDPP(params);
        return rasterLonDPP > nodeLonDPP;
        }

    private double lonDPP(Map<String, Double> param) {
        double q_upperLeftlon = param.get("ullon");
        double q_lowerRightlon = param.get("lrlon");
        double q_width = param.get("w");
        return (q_lowerRightlon - q_upperLeftlon) / q_width;
    }

    private String[][] listToArray(List<QuadTree.Node> fileList){
        Map<Double, LinkedList<QuadTree.Node>> map = new HashMap<>();
        for (QuadTree.Node node : fileList) {
            double ullat = node.getPicScale().get("ullat");
            if (!(map.containsKey(ullat))) {
                map.put(ullat, new LinkedList<QuadTree.Node>());
            }
        }
        for (QuadTree.Node node : fileList) {
            double ullat = node.getPicScale().get("ullat");
            LinkedList<QuadTree.Node> list = map.get(ullat);
            list.add(node);
        }
        /*
        for (Double key : map.keySet()) {
            LinkedList<QuadTree.Node> list = map.get(key);
            list.sort();
        }
        */
        Double[] latitudes = map.keySet().toArray(new Double[1]);
        java.util.Arrays.sort(latitudes);

        String[][] returnArray = new String[latitudes.length][];
        for (int i = 0; i < latitudes.length; i++) {
            LinkedList<QuadTree.Node> list = map.get(latitudes[i]);
            LinkedList<String> latList = new LinkedList<>();
            for (QuadTree.Node node : list) {
                latList.add((node.getFilename()));
            }
            String[] a = latList.toArray(new String[0]);
            java.util.Arrays.sort(a);
            returnArray[i] = a;
        }
        //
        //
        //Arrays.sort(returnArray, (a, b) -> String.(a[0], b[0]));
        reverse2DArray(returnArray);

        int l1 = returnArray.length - 1;
        int l2 = returnArray[l1].length - 1;
        String a = returnArray[0][0];
        String newA = a.substring(4, a.length() - 4);
        String b = returnArray[l1][l2];
        String newB = b.substring(4, a.length() - 4);

        QuadTree.Node upperLeftNode = quadTree.getNode(newA);
        raster_ul_lat = upperLeftNode.getPicScale().get("ullat");
        raster_ul_lon = upperLeftNode.getPicScale().get("ullon");
        QuadTree.Node lowerRightNode = quadTree.getNode(newB);
        raster_lr_lat = lowerRightNode.getPicScale().get("lrlat");
        raster_lr_lon = lowerRightNode.getPicScale().get("lrlon");
        //raster_ul_lat = returnArray[0][0];
        //raster_ul_lat = Double.parseDouble(returnArray[l][returnArray[l].length - 1]);
        return returnArray;
    }

    private double arrayMin(Double[] dArray) {
        double min = dArray[0];
        for (int i = 1; i < dArray.length; i++) {
            if (min > dArray[i]) {
                min = dArray[i];
            }
        }
        return min;
    }

    private void reverse2DArray(String[][] s) {
        for (int i = 0; i < s.length / 2; i++) {
            String[] a = s[i];
            String[] b = s[s.length - 1 - i];
            addPNG(a);
            addPNG(b);
            s[i] = b;
            s[s.length - 1 - i] = a;
        }
        if (s.length % 2 == 1) {
            addPNG(s[s.length / 2]);
        }
    }

    private void addPNG(String[] s) {
        String a;
        for (int i = 0; i < s.length; i++) {
            s[i] = img + s[i] + ".png";

            /*
            if (s[i].length() > 4) {

                //a = Character.toString(s[i].charAt(s.length - 4)) + Character.toString(s[i].charAt(s.length - 3)) + Character.toString(s[i].charAt(s.length - 2)) + Character.toString(s[i].charAt(s.length - 1));
                if (!(a.equals(".png"))) {
                    s[i] = img + s[i] + ".png";
                }
            } else {
                s[i] = img + s[i] + ".png";
            }
            */
        }
    }
}
