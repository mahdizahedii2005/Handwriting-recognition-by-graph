package Progressor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formats {
    public final static String NEW_GRAPH = "^NEW_GRAPH ([A-Za-z])\\s*$";
    public final static String ADD_VERTEX = "^ADD_VERTEX (\\d+) (\\d+) (\\d+)\\s*$";
    public final static String ADD_EDGE = "^ADD_EDGE (\\d+) (\\d+)\\s*$";
    public final static String DEL_VERTEX = "^DEL_VERTEX (\\d+) (\\d+)\\s*$";
    public final static String DEL_EDGE = "^DEL_EDGE (\\d+) (\\d+) (\\d+)\\s*$";
    public final static String EDIT_VERTEX = "^EDIT_VERTEX (\\d+) (\\d+) (-?\\d+(\\.\\d+)?)\\s*$";
    public final static String EDIT_EDGE = "^EDIT_EDGE (\\d+) (\\d+) (\\d+) (-?\\d+(\\.\\d+)?)\\s*$";
    public final static String SHOW_GRAPH = "^SHOW_GRAPH ([A-Za-z])\\s*$";
    public final static String GET_DISTANCE = "^GRAPH_DISTANCE (\\d+) (\\d+)\\s*$";
    public final static String READ_TEXT = "^READ_TEXT\\s*$";

    public static Matcher formatChecker(String query, String format) {
        return Pattern.compile(format).matcher(query);
    }
}
