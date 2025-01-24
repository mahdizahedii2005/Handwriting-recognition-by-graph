package Graph.Detail;

public class Edge implements Comparable<Edge> {
    public Vertex source;
    public Vertex destination;
    private double weight;

    public Edge(Vertex source, Vertex destination, double weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return source + "-->" + destination;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            Edge e = (Edge) obj;
            if (source.equals(e.source) && destination.equals(e.destination) && getWeight() == e.getWeight())
                return true;
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public int compareTo(Edge o) {
        if (source.id > o.source.id) return 1;
        else if (source.id < o.source.id) return -1;
        else return Integer.compare(destination.id, o.destination.id);
    }
}
