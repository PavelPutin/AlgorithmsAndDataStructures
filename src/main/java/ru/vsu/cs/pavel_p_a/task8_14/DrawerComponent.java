package ru.vsu.cs.pavel_p_a.task8_14;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class DrawerComponent extends JComponent {
    public static class Ellipse2DColored extends Ellipse2D.Double implements Shape {
        private Color fillColor = Color.white;
        public java.util.List<Edge> edges = new ArrayList<>();

        public Ellipse2DColored() {
            super();
        }

        public Ellipse2DColored(double x, double y, double w, double h) {
            super(x, y, w, h);
        }

        private void setFillColor(Color color) {
            this.fillColor = color;
        }

        private Color getFillColor() {
            return this.fillColor;
        }
    }

    public static class Edge extends Line2D.Double {
        public Ellipse2DColored fromVertex;
        public Ellipse2DColored toVertex;

        public Edge() {
        }

        public Edge(double x1, double y1, double x2, double y2) {
            super(x1, y1, x2, y2);
        }

        public Edge(Point2D p1, Point2D p2) {
            super(p1, p2);
        }

        public void updateCoordinates(Ellipse2DColored vertex) {
            if (vertex.equals(fromVertex)) {
                this.x1 = fromVertex.getX() + 25;
                this.y1 = fromVertex.getY() + 25;
            } else if (vertex.equals(toVertex)) {
                this.x2 = toVertex.getX() + 25;
                this.y2 = toVertex.getY() + 25;
            }
        }

        public void setFromVertex(Ellipse2DColored fromVertex, double x, double y) {
            this.fromVertex = fromVertex;
            this.x1 = x;
            this.y1 = y;
        }

        public void setToVertex(Ellipse2DColored toVertex, double x, double y) {
            this.toVertex = toVertex;
            this.x2 = x;
            this.y2 = y;
        }
    }
    java.util.List<Ellipse2DColored> circleList = new ArrayList<>();
    java.util.Map<Ellipse2DColored, Point> newEdgePoints = new HashMap<>();

    public DrawerComponent() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    double xCoordinate = e.getX();
                    double yCoordinate = e.getY();
                    Ellipse2DColored clicked = circleList.stream().filter(circle -> circle.contains(e.getPoint()) && !(circle.getFillColor().equals(Color.RED))).findFirst().orElse(null);
                    if (clicked == null) {
                        circleList.add(new Ellipse2DColored(xCoordinate - 25, yCoordinate - 25, 50, 50));
                    } else {
                        clicked.setFillColor(Color.red);
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    double xCoordinate = e.getX();
                    double yCoordinate = e.getY();
                    Ellipse2DColored clicked = circleList.stream().filter(circle -> circle.contains(xCoordinate, yCoordinate))
                            .findFirst().orElse(null);
                    if (clicked != null) {
                        while (!clicked.edges.isEmpty()){
                            Edge edge = clicked.edges.get(0);
                            edge.fromVertex.edges.remove(edge);
                            edge.toVertex.edges.remove(edge);
                        }
                        circleList.remove(clicked);
                    }

                } else if (SwingUtilities.isMiddleMouseButton(e)) {
                    Ellipse2DColored clicked = circleList.stream().filter(circle -> circle.contains(e.getPoint())).findFirst().orElse(null);
                    if (clicked != null) {
                        Point edgePoint = new Point((int) clicked.x + 25, (int) clicked.y + 25);
                        newEdgePoints.put(clicked, edgePoint);
                        if (newEdgePoints.size() == 2) {
                            Edge edge = new Edge();
                            boolean fromVertex = true;
                            for (Ellipse2DColored circle : newEdgePoints.keySet()) {
                                circle.edges.add(edge);
                                double x = newEdgePoints.get(circle).getX();
                                double y = newEdgePoints.get(circle).getY();
                                if (fromVertex) {
                                    fromVertex = false;
                                    edge.setFromVertex(circle, x, y);
                                } else {
                                    edge.setToVertex(circle, x, y);
                                }
                            }
                            newEdgePoints.clear();
                        }
                    }

                }

                repaint();
            }
        });
        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Ellipse2DColored dragged = circleList.stream()
                        .filter(circle -> circle.contains(e.getPoint()) && circle.getFillColor().equals(Color.RED))
                        .findFirst().orElse(null);
                if (dragged != null) {
                    dragged.x = Math.max(e.getX() - 25, 0);
                    dragged.y = e.getY() - 25;
                    dragged.edges.forEach(edge -> edge.updateCoordinates(dragged));
                }
                repaint();
            }
        });
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        circleList.forEach(circle -> {
            circle.edges.forEach(g2d::draw);
        });
        circleList.forEach(circle -> {
            Color oldColor = g2d.getColor();
            g2d.setColor(circle.getFillColor());
            g2d.fill(circle);
            g2d.setColor(oldColor);
        });
    }
}
