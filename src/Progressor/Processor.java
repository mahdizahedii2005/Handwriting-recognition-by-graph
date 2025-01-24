package Progressor;

import Graph.Graph;

import java.util.regex.Matcher;

public class Processor implements IProcessor {
    @Override
    public String Progress(String query) {
        try {
            Matcher matcher;
            if ((matcher = Formats.formatChecker(query, Formats.NEW_GRAPH)).matches()) {
                if (Graph.CreatGraph(Integer.parseInt(matcher.group(1))) != null) return "INVALID COMMAND\n";
            } else if ((matcher = Formats.formatChecker(query, Formats.ADD_VERTEX)).matches()) {
                Graph targetedGraph = Graph.GRAPHS.get(Integer.parseInt(matcher.group(1)));
                if (!targetedGraph.addVertex(Integer.parseInt(matcher.group(2)), Double.parseDouble(matcher.group(3))))
                    return "INVALID COMMAND\n";
            } else if ((matcher = Formats.formatChecker(query, Formats.ADD_EDGE)).matches()) {
                Graph targetedGraph = Graph.GRAPHS.get(Integer.parseInt(matcher.group(1)));
                int source = Integer.parseInt(matcher.group(2));
                int destination = Integer.parseInt(matcher.group(3));
                if (source > destination) {
                    int temp = source;
                    source = destination;
                    destination = temp;
                }
                if (source == destination || !targetedGraph.addEdge(source, destination, Double.parseDouble(matcher.group(4))))
                    return "INVALID COMMAND\n";
            } else if ((matcher = Formats.formatChecker(query, Formats.DEL_VERTEX)).matches()) {
                Graph targetedGraph = Graph.GRAPHS.get(Integer.parseInt(matcher.group(1)));
                targetedGraph.deleteVertex(Integer.parseInt(matcher.group(2)));
            } else if ((matcher = Formats.formatChecker(query, Formats.DEL_EDGE)).matches()) {
                Graph targetedGraph = Graph.GRAPHS.get(Integer.parseInt(matcher.group(1)));
                int source = Integer.parseInt(matcher.group(2));
                int destination = Integer.parseInt(matcher.group(3));
                if (source > destination) {
                    int temp = source;
                    source = destination;
                    destination = temp;
                }
                if (!targetedGraph.deleteEdge(source, destination)) return "INVALID COMMAND\n";
            } else if ((matcher = Formats.formatChecker(query, Formats.EDIT_VERTEX)).matches()) {
                Graph targetedGraph = Graph.GRAPHS.get(Integer.parseInt(matcher.group(1)));
                targetedGraph.vertexWeightEdit(Integer.parseInt(matcher.group(2)), Double.parseDouble(matcher.group(3)));
            } else if ((matcher = Formats.formatChecker(query, Formats.EDIT_EDGE)).matches()) {
                Graph targetedGraph = Graph.GRAPHS.get(Integer.parseInt(matcher.group(1)));
                int source = Integer.parseInt(matcher.group(2));
                int destination = Integer.parseInt(matcher.group(3));
                if (source > destination) {
                    int temp = source;
                    source = destination;
                    destination = temp;
                }
                targetedGraph.edgeWeightEdit(source, destination, Double.parseDouble(matcher.group(4)));
            } else if ((matcher = Formats.formatChecker(query, Formats.SHOW_GRAPH)).matches()) {
                Graph targetedGraph = Graph.GRAPHS.get(Integer.parseInt(matcher.group(1)));
//                GraphVisualizer.showGraph(targetedGraph.getVertexId(), targetedGraph.getEdges());
                return targetedGraph.toString();
            } else if ((matcher = Formats.formatChecker(query, Formats.GET_DISTANCE)).matches()) {
                Graph firstGraph = Graph.GRAPHS.get(Integer.parseInt(matcher.group(1)));
                Graph secondGraph = Graph.GRAPHS.get(Integer.parseInt(matcher.group(2)));
                return firstGraph.Distanced(secondGraph);
            } else {
                return "INVALID COMMAND\n";
            }
        } catch (Exception we) {
            return "INVALID COMMAND\n";
        }
        return "";
    }
}
