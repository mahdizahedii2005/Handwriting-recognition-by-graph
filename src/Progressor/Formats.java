package Progressor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formats {
    public final static String NEW_GRAPH = "^NEW_GRAPH (\\d{2})$";
    public final static String ADD_VERTEX = "^ADD_VERTEX (\\d{2}) (\\d{8}) (-?\\d+(\\.\\d+)?)$";
    public final static String ADD_EDGE = "^ADD_EDGE (\\d{2}) (\\d{8}) (\\d{8}) (-?\\d+(\\.\\d+)?)$";
    public final static String DEL_VERTEX = "^DEL_VERTEX (\\d{2}) (\\d{8})$";
    public final static String DEL_EDGE = "^DEL_EDGE (\\d{2}) (\\d{8}) (\\d{8})$";
    public final static String EDIT_VERTEX = "^EDIT_VERTEX (\\d{2}) (\\d{8}) (-?\\d+(\\.\\d+)?)$";
    public final static String EDIT_EDGE = "^EDIT_EDGE (\\d{2}) (\\d{8}) (\\d{8}) (-?\\d+(\\.\\d+)?)$";
    public final static String SHOW_GRAPH = "^SHOW_GRAPH (\\d{2})$";
    public final static String GET_DISTANCE = "^GRAPH_DISTANCE (\\d{2}) (\\d{2})$";


    public static Matcher formatChecker(String query, String format) {
        return Pattern.compile(format).matcher(query);
    }
}
