package Graph;

import Graph.Detail.Edge;
import Graph.Detail.TwoMonomorphicGraph;
import Graph.Detail.Vertex;
import Tool.CustomHashSet;
import Tool.Helper;

import java.awt.*;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.List;

public class Graph implements Cloneable, Comparable<Graph> {
    public static HashMap<Integer, Graph> GRAPHS = new HashMap<>();
    private static ArrayList<Integer> GRAPHS_ID = new ArrayList<>();
    public final int id;
    public HashMap<Integer, Vertex> vertexes;
    public HashMap<Integer, Edge> edges;
    public boolean haveNegValue = false;
    private double priceFromOriginal = 0;

    public Graph(int id) {
        this.id = id;
        vertexes = new HashMap<>();
        edges = new HashMap<>();
    }

    public boolean isEmpty() {
        return vertexes.isEmpty() && edges.isEmpty();
    }

    public static Graph getLastGraph() {
        return GRAPHS.get(GRAPHS_ID.get(GRAPHS_ID.size() - 1));
    }

    public char getLetter() {
        return (char) id;
    }

    public static Graph CreatGraph(int id) {
        Graph graph = new Graph(id);
        if (GRAPHS.putIfAbsent(id, graph) == null) {
            GRAPHS_ID.add(id);
        }
        return graph;
    }

    public static Graph CreatGraph(char letter) {
        Graph graph = new Graph(letter);
        if (GRAPHS.putIfAbsent((int) letter, graph) == null) {
            GRAPHS_ID.add((int) letter);
        }
        return graph;
    }

    public static void removeLastGraph() {
        int key = GRAPHS_ID.get(GRAPHS_ID.size() - 1);
        GRAPHS.remove(key);
        GRAPHS_ID.remove((Object) key);
    }

    public ArrayList<Integer> getVertexId() {
        return new ArrayList<>(vertexes.keySet());
    }

    public Graph[] getBindingComponents() {
        ArrayList<Graph> bindingComponents = new ArrayList<>();
        Graph clone = clone();
        while (!clone.vertexes.isEmpty()) {
            Graph graph = new Graph(Helper.HashMapToList(clone.vertexes).get(0).id);
            HashSet<Integer> current = new HashSet<>();
            Vertex vertex = Helper.HashMapToList(clone.vertexes).get(0);
            current.add(vertex.id);
            graph.addVertex(vertex.id, vertex.position, vertex.getWeight());
            while (!current.isEmpty()) {
                vertex = clone.vertexes.get(current.toArray()[0]);
                current.remove(vertex.id);
                for (Vertex vertex1 : vertex.getNeighbor()) {
                    if (current.add(vertex1.id)) graph.addVertex(vertex1.id, vertex1.position, vertex1.getWeight());
                }
                for (Edge edge : vertex.getAllEdges()) {
                    graph.addEdge(edge.source.id, edge.destination.id, edge.getWeight());
                }
                clone.deleteVertex(vertex.id);
            }
            bindingComponents.add(graph);
        }
        return Helper.toArray(bindingComponents);
    }

    public List<List<Integer>> getEdges() {
        List<List<Integer>> result = new ArrayList<>();
        for (Edge edge : edges.values()) {
            List<Integer> arrayList = new ArrayList<>();
            arrayList.add(edge.source.id);
            arrayList.add(edge.destination.id);
            result.add(arrayList);
        }
        return result;
    }

    private static CustomHashSet getAllUnimorphs(Graph graph1, Graph graph2) {
        CustomHashSet arrayList = new CustomHashSet();
        Graph first;
        Graph second;
        if (graph1.getKeyNumber() >= graph2.getKeyNumber()) {
            first = graph1;
            second = graph2;
        } else {
            first = graph2;
            second = graph1;
        }
        HashSet<String> cash = new HashSet<>();
        arrayList.addAll(doContractionVertexForAll(first, second, cash));
        cash.remove(graph1.toSerialNumber() + graph2.toSerialNumber() + 'v');
        cash.remove(graph2.toSerialNumber() + graph1.toSerialNumber() + 'v');

        arrayList.addAll(doContractionEdgeForAll(first, second, cash));
        return arrayList;
    }

    private static CustomHashSet doContractionVertexForAll(Graph first, Graph second, HashSet<String> cash) {
        Graph graph1;
        Graph graph2;
        if (first.getKeyNumber() >= second.getKeyNumber()) {
            graph1 = first;
            graph2 = second;
        } else {
            graph1 = second;
            graph2 = first;
        }
        CustomHashSet result = new CustomHashSet();

        if (areThisTwoTheSame(graph1, graph2)) {
            result.add(new TwoMonomorphicGraph(graph1, graph2));
            if (!graph1.haveNegValue && !graph2.haveNegValue) {
                return result;
            } else {
                if (graph1.edges.isEmpty() && graph2.edges.isEmpty()) return result;
            }
        }
        if (graph1.edges.isEmpty() || graph1.vertexes.isEmpty()) {
            if (graph2.edges.isEmpty()) return new CustomHashSet();
        }
        boolean ab = !cash.add(graph1.toSerialNumber() + graph2.toSerialNumber() + 'v');
        boolean ba = !graph2.toSerialNumber().equals(graph1.toSerialNumber()) && !cash.add(graph2.toSerialNumber() + graph1.toSerialNumber() + 'v');
        if (ab || ba) return new CustomHashSet();
        for (Vertex vertex : graph1.vertexes.values()) {
            if (vertex.getDeg() == 0) continue;
            Graph clone1 = graph1.clone();
            Graph clone2 = graph2.clone();
            Vertex vertex1 = clone1.vertexes.get(vertex.id);
            clone1.priceFromOriginal += clone1.contractionVertex(vertex1);
            result.addAll(doContractionVertexForAll(clone1, clone2, cash));
            Graph clone11 = graph1.clone();
            Graph clone22 = graph2.clone();
            Vertex vertex11 = clone11.vertexes.get(vertex.id);
            clone11.priceFromOriginal += clone11.contractionVertex(vertex11);
            result.addAll(doContractionEdgeForAll(clone11, clone22, cash));
        }
        return result;
    }

    private static CustomHashSet doContractionEdgeForAll(Graph first, Graph second, HashSet<String> cash) {
        Graph graph1;
        Graph graph2;
        if (first.getKeyNumber() >= second.getKeyNumber()) {
            graph1 = first;
            graph2 = second;
        } else {
            graph1 = second;
            graph2 = first;
        }
        if (graph1.edges.isEmpty() || graph1.vertexes.isEmpty()) {
            if (graph2.edges.isEmpty()) return new CustomHashSet();
        }
        boolean ab = !cash.add(graph1.toSerialNumber() + graph2.toSerialNumber() + 'e');
        boolean ba = graph2.toSerialNumber().equals(graph1.toSerialNumber()) ? false : !cash.add(graph2.toSerialNumber() + graph1.toSerialNumber() + 'e');
        if (ab || ba) return new CustomHashSet();
        CustomHashSet result = new CustomHashSet();
        if (areThisTwoTheSame(graph1, graph2)) {
            result.add(new TwoMonomorphicGraph(graph1, graph2));
            if (!graph1.haveNegValue && !graph2.haveNegValue) {
                return result;
            } else {
                if (graph1.edges.isEmpty() && graph2.edges.isEmpty()) return result;
            }
        }
        for (Edge edge : graph1.edges.values()) {
//            long time = System.currentTimeMillis();
            Graph clone1 = graph1.clone();
            Graph clone2 = graph2.clone();
            Edge edge1 = clone1.edges.get(Helper.PAIR_CODE(edge.source.id, edge.destination.id));
            clone1.priceFromOriginal += clone1.contractionEdge(edge1);
            result.addAll(doContractionVertexForAll(clone1, clone2, cash));
            Graph clone11 = graph1.clone();
            Graph clone22 = graph2.clone();
            Edge edge11 = clone11.edges.get(Helper.PAIR_CODE(edge.source.id, edge.destination.id));
            clone11.priceFromOriginal += clone11.contractionEdge(edge11);

            result.addAll(doContractionEdgeForAll(clone11, clone22, cash));
        }
        return result;
    }

    private static boolean areThisTwoTheSame(Graph graph1, Graph graph2) {
        Graph clone1 = graph1.clone();
        Graph clone2 = graph2.clone();
        if (clone1.vertexes.size() != clone2.vertexes.size() || clone1.edges.size() != clone2.edges.size()) {
            return false;
        }
        if (((graph1.vertexes.size() * (graph1.vertexes.size() - 1)) / 2) - 1 <= graph1.edges.size()) return true;
        if (graph1.edges.isEmpty()) return true;
        for (Vertex vertex1 : clone1.vertexes.values()) {
            for (Vertex vertex2 : clone2.vertexes.values()) {
                if (goInto(clone1, vertex1, clone2, vertex2)) return true;
            }
        }
        return false;
    }

    private static boolean goInto(Graph graph1, Vertex vertex1, Graph graph2, Vertex vertex2) {
        if (vertex1 == null || vertex2 == null) return false;
        if (graph1.edges.isEmpty() && graph2.edges.isEmpty()) return true;
        if (vertex1.getDeg() != vertex2.getDeg()) return false;
        if (vertex1.getDeg() == 0) {
            for (Vertex v1 : graph1.vertexes.values()) {
                for (Vertex v2 : graph1.vertexes.values()) {
                    if (v1.getDeg() != 0 && v2.getDeg() != 0) {
                        if (goInto(graph1, v1, graph2, v2)) return true;
                    }
                }
            }
            return false;
        }
        for (Vertex neighbor1 : vertex1.getNeighbor()) {
            for (Vertex neighbor2 : vertex2.getNeighbor()) {
                Graph clone1 = graph1.clone();
                Graph clone2 = graph2.clone();
                clone1.deleteEdge(vertex1.id, neighbor1.id);
                clone1.deleteEdge(neighbor1.id, vertex1.id);
                clone2.deleteEdge(vertex2.id, neighbor2.id);
                clone2.deleteEdge(neighbor2.id, vertex2.id);
//                System.out.println("clone1 size :" + clone1.edges.size());
//                System.out.println("clone2 size :" + clone2.edges.size());
                if (goInto(clone1, clone1.vertexes.get(neighbor1.id), clone2, clone2.vertexes.get(neighbor2.id)))
                    return true;
            }
        }
        return false;
    }

    public double getPriceFromOriginal() {
        return priceFromOriginal;
    }

    public boolean addVertex(int vertexId, Point2D point2D, double weight) {
        if (weight < 0) haveNegValue = true;
        return vertexes.putIfAbsent(vertexId, new Vertex(vertexId, point2D, weight)) == null;
    }

    public boolean addEdge(int sourceId, int destinationId, double weight) {
        try {
            Vertex sourceVertex = vertexes.get(sourceId);
            Vertex destinationVertex = vertexes.get(destinationId);
            if (weight == 0) {
                weight = sourceVertex.position.distance(destinationVertex.position);
            }
            if (weight < 0) haveNegValue = true;
            Edge edge = new Edge(vertexes.get(sourceId), vertexes.get(destinationId), weight);
            boolean a = sourceVertex.addEdge(edge, true);
            boolean b = destinationVertex.addEdge(edge, false);
            return edges.putIfAbsent(Helper.PAIR_CODE(sourceId, destinationId), edge) == null && a && b;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public int getKeyNumber() {
        return vertexes.size() + edges.size();
    }

    public void deleteVertex(int vertexId) {
        Vertex target = vertexes.get(vertexId);
        ArrayList<Edge> edges1 = new ArrayList<>(target.edgesFromDestination.values());
        for (Edge edge : edges1) {
            deleteEdge(edge);
        }
        ArrayList<Edge> edges2 = new ArrayList<>(target.edgesFromSource.values());
        for (Edge edge : edges2) {
            deleteEdge(edge);
        }
        vertexes.remove(vertexId);
    }

    public boolean deleteEdge(Edge edge) {
        deleteEdge(edge.source, edge.destination);
        return edges.remove(Helper.PAIR_CODE(edge.source.id, edge.destination.id)) != null;
    }

    public boolean deleteEdge(int sourceId, int destinationId) {
        try {
            return deleteEdge(edges.get(Helper.PAIR_CODE(sourceId, destinationId)));
        } catch (Exception e) {
            return false;
        }
    }

    public void deleteEdge(Vertex sourceId, Vertex destinationId) {
        sourceId.edgesFromDestination.remove(destinationId.id);
        destinationId.edgesFromSource.remove(sourceId.id);
    }

    public void vertexWeightEdit(int vertexId, double newWeight) {
        if (newWeight < 0) haveNegValue = true;

        vertexes.get(vertexId).setWeight(newWeight);
    }

    public void edgeWeightEdit(int sourceId, int destinationId, double newWeight) {
        if (newWeight < 0) haveNegValue = true;
        edges.get(Helper.PAIR_CODE(sourceId, destinationId)).setWeight(newWeight);
    }

    @Override
    public String toString() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("0.000000", symbols);
        StringBuilder result = new StringBuilder(getLetter() + " " + vertexes.values().size() + " " + edges.values().size() + '\n');
        ArrayList<Edge> e = new ArrayList<>(edges.values());
        Collections.sort(e);
        ArrayList<Vertex> v = new ArrayList<>(vertexes.values());
        Collections.sort(v);
        for (Vertex vertex : v) {
            result.append(id).append(" ").append(vertex.id).append(" ").append(df.format(vertex.getWeight())).append('\n');
        }
        for (Edge edge : e) {
            result.append(id).append(" ").append(edge.source.id).append(" ").append(edge.destination.id).append(" ").append(df.format(edge.getWeight())).append('\n');
        }
        return result.toString();

    }

    private double contractionVertex(Vertex vertex) {
        if (vertex == null) {
            return 0;
        }
        HashSet<Edge> neighbor = new HashSet<>(vertex.edgesFromDestination.values());
        for (Edge neigh : neighbor) {
            ContractionEdge(neigh, true);
        }

        neighbor = new HashSet<>(vertex.edgesFromSource.values());
        for (Edge neigh : neighbor) {
            ContractionEdge(neigh, false);
        }
        return vertex.getWeight();
    }

    private void ContractionEdge(Edge edge, boolean isSourceTarget) {
        deleteEdge(edge);
        if (isSourceTarget) Integration(edge.source, edge.destination, edge.getWeight());
        else Integration(edge.destination, edge.source, edge.getWeight());
    }

    private double contractionEdge(Edge edge) {
        deleteEdge(edge);
        return Integration(edge.source, edge.destination, edge.getWeight());
    }

    private double Integration(Vertex vertex1, Vertex vertex2, double weight) {
        if (vertex1 == null || vertex2 == null) return 0;
        HashSet<Integer> vertex2Collection = new HashSet<>(vertex2.edgesFromSource.keySet());
        vertex2Collection.addAll(vertex2.edgesFromDestination.keySet());

        HashSet<Integer> intersectDes = Helper.Intersect(vertex2Collection, new HashSet<>(vertex1.edgesFromDestination.keySet()));
        for (int edge : intersectDes) {
            Edge edge1 = vertex2.edgesFromSource.get(edge);
            if (edge1 == null) {
                edge1 = vertex2.edgesFromDestination.get(edge);
            }
            Edge targetedEdge = edges.get(Helper.PAIR_CODE(vertex1.id, edge));
            if (targetedEdge == null) targetedEdge = edges.get(Helper.PAIR_CODE(edge, vertex1.id));
            if (targetedEdge == null) return 0;
            targetedEdge.setWeight(edge1.getWeight() + targetedEdge.getWeight());
            this.deleteEdge(edge1);
        }
        HashSet<Integer> intersectSour = Helper.Intersect(vertex2Collection, new HashSet<>(vertex1.edgesFromSource.keySet()));
        for (int edge : intersectSour) {
            Edge edge1 = vertex2.edgesFromSource.get(edge);
            if (edge1 == null) {
                edge1 = vertex2.edgesFromDestination.get(edge);
            }
            Edge targetedEdge = edges.get(Helper.PAIR_CODE(edge, vertex1.id));
            if (targetedEdge == null) return 0;
            targetedEdge.setWeight(edge1.getWeight() + targetedEdge.getWeight());
            this.deleteEdge(edge1);
        }

        HashSet<Integer> vertex1Collection = new HashSet<>(vertex1.edgesFromSource.keySet());
        vertex1Collection.addAll(vertex1.edgesFromDestination.keySet());
        HashSet<Integer> notIntersect = Helper.Minus(vertex2Collection, vertex1Collection);
        for (int edge : notIntersect) {
            Edge ne1 = vertex2.edgesFromSource.get(edge);
            if (ne1 == null) ne1 = vertex2.edgesFromDestination.get(edge);
            if (ne1 == null) continue;
            addEdge(vertex1.id, edge, ne1.getWeight());
        }
        vertex1.setWeight(vertex1.getWeight() + vertex2.getWeight() + weight);
        this.deleteVertex(vertex2.id);
        return vertex1.getWeight();
    }

    public String Distanced(Graph graph) {
        CustomHashSet allUnimorphs = getAllUnimorphs(this, graph);
        ArrayList<Double> difPoint = new ArrayList<>();
        for (TwoMonomorphicGraph twoMonomorphicGraph : allUnimorphs) {
            difPoint.add(twoMonomorphicGraph.getDistance());
        }
        if (difPoint.isEmpty()) return "inf" + '\n';
        else {
            int i = 0;
            int finall = 0;
            double mostLess = difPoint.get(0);
            for (double point : difPoint) {
                if (mostLess > point) {
                    mostLess = point;
                    finall = i;
                }
                i++;
            }
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
            DecimalFormat df = new DecimalFormat("0.000000", symbols);
            return df.format(mostLess) + '\n';
        }
    }

    public Edge GetVertexById(int sourceId, int destinationId) {
        return edges.get(Helper.PAIR_CODE(sourceId, destinationId));
    }

    public Vertex GetEdgeById(int id) {
        return vertexes.get(id);
    }

    @Override
    public Graph clone() {
        Graph clone = new Graph(id);
        for (int id : vertexes.keySet()) {
            clone.addVertex(id, vertexes.get(id).position, vertexes.get(id).getWeight());
        }
        for (int id : edges.keySet()) {
            Edge edge = edges.get(id);
            clone.addEdge(edge.source.id, edge.destination.id, edge.getWeight());
        }
        clone.priceFromOriginal = priceFromOriginal;
        return clone;
    }

    @Override
    public boolean equals(Object obj) {
        Graph graph = (Graph) obj;
        return (graph.toString() + graph.priceFromOriginal).equals(toString() + priceFromOriginal);
    }

    public String toSerialNumber() {
        StringBuilder result = new StringBuilder(vertexes.values().size() + edges.values().size());
        ArrayList<Edge> e = new ArrayList<>(edges.values());
        Collections.sort(e);
        ArrayList<Vertex> v = new ArrayList<>(vertexes.values());
        Collections.sort(v);
        for (Vertex vertex : v) {
            result.append(vertex.id).append((int) (vertex.getWeight() * 1000000));
        }
        for (Edge edge : e) {
            result.append(edge.source.id).append(edge.destination.id).append((int) (edge.getWeight() * 1000000));
        }
        result.append((int) (priceFromOriginal * 1000000));
        return result.toString();
    }

    private Point2D findLeftest() {
        Point2D point2D = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
        for (Vertex vertex : vertexes.values()) {
            if (point2D.getX() > vertex.position.getX()) {
                point2D = vertex.position;
            }
        }
        return point2D;
    }

    public int lowestY() {
        int result = Integer.MAX_VALUE;
        for (Vertex vertex : vertexes.values()) {
            if (vertex.position.getY() < result) {
                result = (int) vertex.position.getY();
            }
        }
        return result;
    }

    public String whatIsThisChar() {
        Graph closetGraph = GRAPHS.get(GRAPHS_ID.get(0));
        double price = Double.POSITIVE_INFINITY;
        for (int i = 0; i < Graph.GRAPHS_ID.size() - 1; i++) {
            Graph kandid = GRAPHS.get(GRAPHS_ID.get(i));
            String priceValue = this.Distanced(kandid);
            priceValue = priceValue.substring(0, priceValue.length() - 1);
            double difference = ("inf").equals(priceValue) ? Double.POSITIVE_INFINITY : Double.parseDouble(priceValue);
            if (difference < price) {
                price = difference;
                closetGraph = kandid;
            } else if (difference == price) {
                if (closetGraph.getLetter() > kandid.getLetter()) {
                    closetGraph = kandid;
                }
            }
        }
        return closetGraph == null ? "" : closetGraph.getLetter() + "";
    }

    @Override
    public int compareTo(Graph o) {
        Point2D p1 = findLeftest();
        Point2D p2 = o.findLeftest();
        return Integer.compare((int) p1.getX(), (int) p2.getX());
    }
}
