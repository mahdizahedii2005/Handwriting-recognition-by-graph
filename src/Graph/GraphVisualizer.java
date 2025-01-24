package Graph;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphVisualizer extends JPanel {
    private final List<Integer> vertices;
    private final List<List<Integer>> edges;
    private final Map<Integer, Point> positions;

    public GraphVisualizer(List<Integer> vertices, List<List<Integer>> edges) {
        this.vertices = vertices;
        this.edges = edges;
        this.positions = new HashMap<>();
        calculatePositions();
    }

    public static void showGraph(List<Integer> vertices, List<List<Integer>> edges) {
        JFrame frame = new JFrame("Graph.Graph Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.add(new GraphVisualizer(vertices, edges));
        frame.setVisible(true);
    }

    private void calculatePositions() {
        int radius = 200; // شعاع دایره برای نمایش گره‌ها
        int centerX = 300;
        int centerY = 300;
        int n = vertices.size();
        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n;
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));
            positions.put(vertices.get(i), new Point(x, y));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw edges
        g2d.setColor(Color.GRAY);
        for (int i = 0; i < vertices.size(); i++) {
            int fromVertex = vertices.get(i);
            Point fromPoint = positions.get(fromVertex);
            if (fromPoint != null && i < edges.size() && edges.get(i) != null) {
                for (int toVertex : edges.get(i)) {
                    Point toPoint = positions.get(toVertex);
                    if (toPoint != null) {
                        g2d.drawLine(fromPoint.x, fromPoint.y, toPoint.x, toPoint.y);
                    }
                }
            }
        }

        // Draw vertices
        g2d.setColor(Color.BLUE);
        for (int vertex : vertices) {
            Point p = positions.get(vertex);
            if (p != null) {
                g2d.fillOval(p.x - 10, p.y - 10, 20, 20);
                g2d.setColor(Color.BLACK);
                g2d.drawString(String.valueOf(vertex), p.x - 5, p.y + 5);
                g2d.setColor(Color.BLUE);
            }
        }
    }
}