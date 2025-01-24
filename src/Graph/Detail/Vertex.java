package Graph.Detail;

import java.util.ArrayList;
import java.util.HashMap;

public class Vertex implements Comparable<Vertex> {
    public int id;
    public HashMap<Integer, Edge> edgesFromDestination;// source is this vertex
    public HashMap<Integer, Edge> edgesFromSource;//destination is te vertex
    private double weight;

    public Vertex(int id, double weight) {
        this.id = id;
        this.weight = weight;
        edgesFromDestination = new HashMap<>();
        edgesFromSource = new HashMap<>();
    }

    @Override
    public String toString() {
        return id + "";
    }

    public boolean addEdge(Edge edge, boolean addFromDestination) {
        boolean result;
        if (addFromDestination) result = edgesFromDestination.putIfAbsent(edge.destination.id, edge) == null;
        else result = edgesFromSource.putIfAbsent(edge.source.id, edge) == null;
        return result;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(Vertex o) {
        return Integer.compare(id, o.id);
    }

    public ArrayList<Vertex> getNeighbor() {
        ArrayList<Vertex> vertices = new ArrayList<>();
        for (Edge edge : edgesFromDestination.values()) {
            vertices.add(edge.destination);
        }
        for (Edge edge : edgesFromSource.values()) {
            vertices.add(edge.source);
        }
        return vertices;
    }

    public int getDeg() {
        return edgesFromDestination.size() + edgesFromSource.size();
    }
}
