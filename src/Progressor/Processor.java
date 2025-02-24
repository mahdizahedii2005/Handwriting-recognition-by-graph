package Progressor;

import Listener.Listenable;

import java.awt.*;
import java.util.regex.Matcher;
import Graph.Graph;
import Tool.Helper;

public class Processor implements IProcessor {
    @Override
    public String Progress(String query, Listenable listenable) {
        //try {
        Matcher matcher;
        if ((matcher = Formats.formatChecker(query, Formats.NEW_GRAPH)).matches()) {
            if (Graph.CreatGraph((matcher.group(1).charAt(0))) == null) return "INVALID COMMAND\n";
        } else if ((matcher = Formats.formatChecker(query, Formats.ADD_VERTEX)).matches()) {
            Graph targetedGraph = Graph.getLastGraph();
            if (!targetedGraph.addVertex(Integer.parseInt(matcher.group(1)), new Point(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3))), 1000))
                return "INVALID COMMAND\n";
        } else if ((matcher = Formats.formatChecker(query, Formats.ADD_EDGE)).matches()) {
            Graph targetedGraph = Graph.getLastGraph();
            int source = Integer.parseInt(matcher.group(1));
            int destination = Integer.parseInt(matcher.group(2));
            if (source > destination) {
                int temp = source;
                source = destination;
                destination = temp;
            }
            if (source == destination || !targetedGraph.addEdge(source, destination, 0)) return "INVALID COMMAND\n";
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
        } else if ((matcher = Formats.formatChecker(query, Formats.READ_TEXT)).matches()) {
            int numberOfQuery = Integer.parseInt(listenable.listen());
            StringBuilder answer = new StringBuilder();
            Graph.CreatGraph(-1);
            Graph target = Graph.getLastGraph();
            for (int i = 0; i < numberOfQuery; i++) {
                String lineQuery = listenable.listen();
                Progress(lineQuery, listenable);
            }
            Graph[][] graphs = Helper.sortTheText(target.getBindingComponents());
            if (target.isEmpty()) {
                graphs = new Graph[1][1];
                graphs[0][0] = target;
            }
            for (int i = 0; i < graphs.length; i++) {
                for (int j = 0; j < graphs[i].length; j++) {
                    answer.append(graphs[i][j].whatIsThisChar());
                }
                if (i != graphs.length - 1) answer.append("\n");
            }
            Graph.removeLastGraph();
            return answer.toString();
        } else {
            return "INVALID COMMAND\n";
        }
//        } catch (Exception we) {
//            return "INVALID COMMAND\n";
//        }
        return "";
    }
}
