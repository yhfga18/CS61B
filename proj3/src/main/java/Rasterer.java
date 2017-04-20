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

    QuadTree quadTree;
    private double ULLAT;
    private double ULLON;
    private double LRLAT;
    private double LRLON;
    private double Width;
    private double Height;
    private double LONDPP;
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

        this.ULLAT = params.get("ullat");
        this.ULLON = params.get("ullon");
        this.LRLAT = params.get("lrlat");
        this.LRLON = params.get("lrlon");
        this.Width = params.get("w");
        this.Height = params.get("h");
        this.LONDPP = (LRLON - ULLON) / Width;

        ////////////////////////////////////////////////////////////////////////////////
        fileList = new LinkedList<QuadTree.Node>();
        ////////////////////////////////////////////////////////////////////////////////

        Map<String, Object> results = new HashMap<>();
        QuadTree.Node root = quadTree.getRoot();
        search(root, 0);
        String[][] imageFiles = new String[1][];

        ////////////////////////////////////////////////////////////////////////////////
        if (query_success) {
            imageFiles = listToArray(fileList);
        }
        ////////////////////////////////////////////////////////////////////////////////

        results.put("render_grid", imageFiles);
        results.put("raster_ul_lon", raster_ul_lon);
        results.put("raster_ul_lat", raster_ul_lat);
        results.put("raster_lr_lon", raster_lr_lon);
        results.put("raster_lr_lat", raster_lr_lat);
        results.put("depth", depth);
        results.put("query_success", query_success);
        return results;
    }

    private void search(QuadTree.Node node, int d) {
        if (node == null) {
            return;
        }
        if ((P1(node))) { // (params, node)
            if (d == 0) {
                query_success = false;
            }
            return;
        }
        if (!(P2(node, d))) { // (params, node, d)
            search(node.getSub1(), d + 1);
            search(node.getSub2(), d + 1);
            search(node.getSub3(), d + 1);
            search(node.getSub4(), d + 1);
            return;
        }

        fileList.add(node);
        numOfPic += 1;
        depth = (int) d;
        return;
    }

    private boolean P1(QuadTree.Node node) {
        boolean cond1 = (node.getULLAT() < LRLAT); // lat 1
        boolean cond2 = (node.getLRLON() < ULLON); // lon 1
        boolean cond3 = (node.getLRLAT() > ULLAT); // lat 2
        boolean cond4 = (node.getULLON() > LRLON); // lon 2
        return cond1 || cond2 || cond3 || cond4;
    }

    private boolean P2(QuadTree.Node node, int depth) {
        if (depth >= 7) {return true;}
        double nodeLonDPP = (node.getLRLON() - node.getULLON()) / node.getWidth();
        return LONDPP > nodeLonDPP;
        }

    private String[][] listToArray(List<QuadTree.Node> fileList){
        Map<Double, LinkedList<QuadTree.Node>> map = new HashMap<>();
        for (QuadTree.Node node : fileList) {
            double ullat = node.getULLAT();
            if (!(map.containsKey(ullat))) {
                map.put(ullat, new LinkedList<QuadTree.Node>());
            }
        }
        for (QuadTree.Node node : fileList) {
            double ullat = node.getULLAT();
            LinkedList<QuadTree.Node> list = map.get(ullat);
            list.add(node);
        }

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

        reverse2DArray(returnArray);

        int l1 = returnArray.length - 1;
        int l2 = returnArray[l1].length - 1;
        String a = returnArray[0][0];
        String newA = a.substring(4, a.length() - 4);
        String b = returnArray[l1][l2];
        String newB = b.substring(4, a.length() - 4);

        QuadTree.Node upperLeftNode = quadTree.getNode(newA);
        raster_ul_lat = upperLeftNode.getULLAT();
        raster_ul_lon = upperLeftNode.getULLON();
        QuadTree.Node lowerRightNode = quadTree.getNode(newB);
        raster_lr_lat = lowerRightNode.getLRLAT();
        raster_lr_lon = lowerRightNode.getLRLON();
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
