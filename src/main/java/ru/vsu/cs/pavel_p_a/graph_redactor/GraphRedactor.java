package ru.vsu.cs.pavel_p_a.graph_redactor;

import ru.vsu.cs.util.DrawUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class GraphRedactor extends JComponent {

    private static final int FONT_SIZE = 15;
    private static final Font VERTEX_FONT = new Font("Consolas", Font.PLAIN, FONT_SIZE);

    List<Vertex> vertices = new ArrayList<>();

    public GraphRedactor() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                vertices.add(new Vertex(Integer.toString(vertices.size()), e.getX(), e.getY()));
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        vertices.forEach(vertex -> vertex.draw(g2D));
    }


    private static class Vertex {
        static final double eccentricity = 0.5;
        static final double SIZE_SCALE = 1;
        static final double radius = FONT_SIZE * SIZE_SCALE;
        static final Color FILL_COLOR = Color.WHITE;
        static final Color DEFAULT_STROKE_COLOR = Color.BLACK;
        static final int DEFAULT_STROKE_WIDTH = 2;

        int strokeWidth;
        Color strokeColor;
        String title;
        double weight;
        List<Edge> incidentEdges;
        WeightLabel weightLabel;
        double x;
        double y;
        Shape vertexShape;

        public Vertex(String title, double x, double y) {
            double w = getWidth(title),
                    h = getHeight(title);
            this.title = title;
            this.weight = 0;
            this.x = x;
            this.y = y;
            vertexShape = new Ellipse2D.Double(x - w / 2, y - h / 2, w, h);
            strokeColor = DEFAULT_STROKE_COLOR;
            strokeWidth = DEFAULT_STROKE_WIDTH;
        }

        private double getWidth(String title) {
            if (title.length() > 1) {
                return 2 * Math.floor(Math.sqrt(radius * radius / (1 - eccentricity * eccentricity)));
            }
            return 2 * radius;
        }

        private double getHeight(String title) {
            return 2 * radius;
        }

        void setX(double x) {
            this.x = x;
        }

        void setY(double y) {
            this.y = y;
        }

        void setCenter(Point point) {
            this.x = point.x;
            this.y = point.y;
        }

        void draw(Graphics g) {
            Graphics2D g2D = (Graphics2D) g;
            Color oldColor = g2D.getColor();
            Font oldFont = g2D.getFont();
            g2D.setColor(FILL_COLOR);
            g2D.fill(vertexShape);

            g2D.setColor(strokeColor);
            g2D.draw(vertexShape);

            Rectangle2D vertexShapeBounds = vertexShape.getBounds();
            int x = (int) vertexShapeBounds.getX();
            int y = (int) vertexShapeBounds.getY();
            int w = (int) vertexShapeBounds.getWidth();
            int h = (int) vertexShapeBounds.getHeight();
            DrawUtils.drawStringInCenter(g2D,
                    VERTEX_FONT, title,
                    x, y,
                    w, h);

            g2D.setColor(oldColor);
            g2D.setFont(oldFont);
        }
    }

    private static class Edge extends Line2D.Double {
        Color strokeColor;
        String title;
        double weight;
        Vertex fromVertex;
        Vertex toVertex;
        WeightLabel weightLabel;

        public Edge(double x1, double y1, double x2, double y2) {
            super(x1, y1, x2, y2);
        }

        public Edge(Point2D p1, Point2D p2) {
            super(p1, p2);
        }
    }

    private static class WeightLabel extends Rectangle2D.Double {
        int strokeWidth;
        Color strokeColor;
        String title;

        public WeightLabel(double x, double y, double w, double h) {
            super(x, y, w, h);
        }
    }


}
