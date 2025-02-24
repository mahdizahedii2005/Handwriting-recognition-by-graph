package Graph.Detail;

import java.util.ArrayList;
import java.util.HashMap;
import Graph.Graph;
import Tool.Helper;
public class TwoMonomorphicGraph {
    public Graph graph1;
    public Graph graph2;

    public TwoMonomorphicGraph(Graph graph1, Graph graph2) {
        this.graph1 = graph1;
        this.graph2 = graph2;
    }

    public double getDistance() {
        ArrayList<HashMap<Integer, Integer>> maps = new ArrayList<>();
        getAllMapping(maps, new HashMap<>(), new ArrayList<>(graph1.vertexes.values()), new ArrayList<>(graph2.vertexes.values()), graph1.vertexes.size());
        double distanceLess = Double.MAX_VALUE;
        for (HashMap<Integer, Integer> map : maps) {
            boolean valid = true;
            double distanceForThisMap = 0;
            for (Edge edge : graph1.edges.values()) {
                Vertex vertex1 = graph2.vertexes.get(map.get(edge.source.id));
                Vertex vertex2 = graph2.vertexes.get(map.get(edge.destination.id));
                Edge targetEdge = graph2.edges.get(Helper.PAIR_CODE(vertex1.id, vertex2.id));
                if (targetEdge == null) targetEdge = graph2.edges.get(Helper.PAIR_CODE(vertex2.id, vertex1.id));
                if (targetEdge == null) {
                    valid = false;
                    break;
                }
                distanceForThisMap += Math.abs(edge.getWeight() - targetEdge.getWeight());
            }
            if (!valid) {
                continue;
            }
            Integer[] ver1 = map.keySet().toArray(new Integer[0]);
            Integer[] ver2 = map.values().toArray(new Integer[0]);
            for (int i = 0; i < ver1.length; i++) {
                distanceForThisMap += Math.abs(graph1.vertexes.get(ver1[i]).getWeight() - graph2.vertexes.get(ver2[i]).getWeight());
            }
            distanceForThisMap += (graph1.getPriceFromOriginal() + graph2.getPriceFromOriginal());
            if (distanceLess > distanceForThisMap) distanceLess = distanceForThisMap;
        }
        return distanceLess;
    }

    @Override
    public String toString() {
        return graph1.toSerialNumber() + graph2.toSerialNumber();
    }

    private void getAllMapping(ArrayList<HashMap<Integer, Integer>> maps, HashMap<Integer, Integer> currentMap, ArrayList<Vertex> vertex1, ArrayList<Vertex> vertex2, int validMapSize) {
        if (vertex1.size() != vertex2.size()) return;
        if (vertex1.isEmpty()) {
            if (validMapSize == currentMap.size()) {
                maps.add(new HashMap<>(currentMap));
                return;
            }
        }
        Vertex vertex11 = vertex1.get(0);
        for (Vertex vertex22 : vertex2) {
            if (vertex11.getDeg() == vertex22.getDeg()) {
                ArrayList<Vertex> clone1 = new ArrayList<>(vertex1);
                ArrayList<Vertex> clone2 = new ArrayList<>(vertex2);
                clone1.remove(vertex11);
                clone2.remove(vertex22);
                HashMap<Integer, Integer> newCurrentMap = new HashMap<>(currentMap);
                newCurrentMap.put(vertex11.id, vertex22.id);
                getAllMapping(maps, newCurrentMap, clone1, clone2, validMapSize);
            }
        }

    }

    @Override
    public boolean equals(Object obj) {
        //try {
        TwoMonomorphicGraph twoMonomorphicGraph = (TwoMonomorphicGraph) obj;
        boolean a = twoMonomorphicGraph.graph1.equals(graph1);
        boolean b = twoMonomorphicGraph.graph2.equals(graph2);

        boolean w = twoMonomorphicGraph.graph1.equals(graph2);
        boolean e = twoMonomorphicGraph.graph2.equals(graph1);
        if ((a && b) || (w && e)) return true;
//        } catch (Exception e) {
//        }
        return false;
    }
}
