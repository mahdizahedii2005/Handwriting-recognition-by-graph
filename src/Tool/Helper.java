package Tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import Graph.Graph;
public class Helper {
    public static int PAIR_CODE(int x, int y) {
        int a = x >= 0 ? 2 * x : -2 * x - 1;
        int b = y >= 0 ? 2 * y : -2 * y - 1;

        return (a + b) * (a + b + 1) / 2 + b;
    }

    public static Graph[][] sortTheText(Graph[] graphs) {
        Arrays.sort(graphs);
        HashMap<Integer, ArrayList<Graph>> graphMap = new HashMap<>();
        for (Graph graph : graphs) {
            if (graphMap.containsKey(graph.lowestY())) {
                graphMap.get(graph.lowestY()).add(graph);
            } else {
                ArrayList<Graph> graphList = new ArrayList<>();
                graphList.add(graph);
                graphMap.put(graph.lowestY(), graphList);
            }
        }
        Graph[][] result = new Graph[graphMap.keySet().size()][];
        Object[] d = graphMap.keySet().toArray();
        Arrays.sort(d);
        for (int i = 0; i < result.length; i++) {
            ArrayList<Graph> graphList = graphMap.get(d[i]);
            result[result.length - 1 - i] = new Graph[graphList.size()];
            for (int j = 0; j < result[result.length - 1 - i].length; j++) {
                result[result.length - 1 - i][j] = graphList.get(j);
            }
        }
        return result;
    }

    public static <T> ArrayList<T> HashMapToList(HashMap<?, T> map) {
        return new ArrayList<>(map.values());
    }

    public static <T> HashSet<T> Intersect(HashSet<T> set1, HashSet<T> set2) {
        HashSet<T> result = new HashSet<>();
        for (T d : set1) {
            if (set2.contains(d)) result.add(d);
        }
        return result;
    }

    public static Graph[] toArray(ArrayList<Graph> arrayList) {
        Graph[] result = new Graph[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            result[i] = arrayList.get(i);
        }
        return result;
    }

    public static <T> HashSet<T> Minus(HashSet<T> set1, HashSet<T> set2) {
        HashSet<T> result = new HashSet<>();
        for (T d : set1) {
            if (!set2.contains(d)) result.add(d);
        }
        return result;
    }
}
