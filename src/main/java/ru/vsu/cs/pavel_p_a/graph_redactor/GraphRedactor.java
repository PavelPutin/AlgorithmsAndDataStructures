package ru.vsu.cs.pavel_p_a.graph_redactor;

import org.apache.batik.ext.awt.geom.Polygon2D;
import ru.vsu.cs.course1.graph.AdjListsDigraph;
import ru.vsu.cs.course1.graph.AdjListsGraph;
import ru.vsu.cs.course1.graph.Graph;
import ru.vsu.cs.util.DrawUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.sql.SQLOutput;
import java.util.*;
import java.util.List;

public class GraphRedactor extends JComponent {

    private enum VertexStatus {
        FROM, TO;
    }
    private static final int FONT_SIZE = 15;
    private static final Font VERTEX_FONT = new Font("Consolas", Font.PLAIN, FONT_SIZE);

    private static final double VERTEX_ECCENTRICITY = 0.5;
    private static final double SIZE_SCALE = 1;
    private static final double VERTEX_RADIUS = FONT_SIZE * SIZE_SCALE;
    private static final int DEFAULT_STROKE_WIDTH = 2;
    private static final double ARROW_ANGLE = Math.PI / 12;
    private static final double ARROW_HEIGHT = 20;
    private static final double ARROW_LENGTH = ARROW_HEIGHT / Math.cos(ARROW_ANGLE);

    private static final Color FILL_COLOR = Color.WHITE;
    private static final Color SELECTION_FILL_COLOR = Color.CYAN;
    private static final Color DEFAULT_STROKE_COLOR = Color.BLACK;
    private static final Color MARK_STROKE_COLOR = Color.RED;
    private static final Color DEFAULT_ERROR_FOREGROUND = Color.RED;

    private static final Dimension DEFAULT_CANVAS_DIMENSION = new Dimension(600, 600);

    List<Vertex> vertices = new ArrayList<>();
    List<Vertex> selectedVertices = new ArrayList<>();
    Map<VertexStatus, Vertex> newEdgePoints = new HashMap<>();
    boolean isCtrlPressed = false;
    boolean isShiftPressed = false;

    boolean isDigraph = false;
    boolean isWeighted = false;

    JScrollPane canvasScrollPane;
    Canvas canvas;

    JScrollPane controlScrollPane;
    JPanel controlPanel;

    JPanel graphTypesPanel;
    JLabel graphTypeLabel;
    JCheckBox isDigraphCheckBox;
    JCheckBox isWeightedGraphCheckBox;

    JPanel addVertexPanel;
    JLabel addVertexLabel;
    JLabel vertexTitleLabel;
    JLabel vertexTitleErrorLabel;
    JTextField vertexTitleTextField;
    JLabel vertexWeightLabel;
    JTextField vertexWeightTextField;
    JButton addVertexButton;

    JPanel edgeDeletingPanel;
    JLabel edgeDeletingLabel;
    JLabel edgeDeletingInfoLabel;
    JLabel fromVertexTitleLabel;
    JTextField fromVertexTitleTextField;
    JLabel toVertexTitleLabel;
    JTextField toVertexTitleTextField;
    JButton deleteEdgeButton;

    JPanel statusPanel;
    JLabel mousePositionOnCanvasLabel;
    JLabel isCanvasActiveLabel;

    public GraphRedactor() {
        initUIComponents();
    }

    private void initUIComponents() {
        setLayout(new BorderLayout());
        canvasScrollPane = new JScrollPane();
        canvasScrollPane.setPreferredSize(DEFAULT_CANVAS_DIMENSION);
        add(canvasScrollPane, BorderLayout.WEST);

        canvas = new Canvas();
        canvas.setPreferredSize(DEFAULT_CANVAS_DIMENSION);
        canvasScrollPane.setViewportView(canvas);

        controlScrollPane = new JScrollPane();
        add(controlScrollPane, BorderLayout.EAST);

        controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));
        controlScrollPane.setViewportView(controlPanel);

        graphTypesPanel = new JPanel();
        graphTypesPanel.setLayout(new BoxLayout(graphTypesPanel, BoxLayout.PAGE_AXIS));
        controlPanel.add(graphTypesPanel);

        graphTypeLabel = new JLabel("Тип графа:");
        graphTypesPanel.add(graphTypeLabel);

        isDigraphCheckBox = new JCheckBox("ориентированный");
        isDigraphCheckBox.setSelected(false);
        isDigraphCheckBox.addChangeListener(e -> switchDigraph(e));
        graphTypesPanel.add(isDigraphCheckBox);

        isWeightedGraphCheckBox = new JCheckBox("взвешенный");
        //недоделано
        isWeightedGraphCheckBox.setVisible(false);
        isWeightedGraphCheckBox.setSelected(false);
        isWeightedGraphCheckBox.addChangeListener(e -> switchGraphWeight(e));
        graphTypesPanel.add(isWeightedGraphCheckBox);

        addVertexPanel = new JPanel();
        addVertexPanel.setLayout(new BoxLayout(addVertexPanel, BoxLayout.PAGE_AXIS));
        controlPanel.add(addVertexPanel);

        addVertexLabel = new JLabel("Создать новую вершину");
        addVertexPanel.add(addVertexLabel);

        vertexTitleLabel = new JLabel("Введите название вершины");
        addVertexPanel.add(vertexTitleLabel);

        vertexTitleErrorLabel = new JLabel("Вершина с таким именем уже существует");
        vertexTitleErrorLabel.setForeground(DEFAULT_ERROR_FOREGROUND);
        vertexTitleErrorLabel.setVisible(false);
        addVertexPanel.add(vertexTitleErrorLabel);

        vertexTitleTextField = new JTextField();
        addVertexPanel.add(vertexTitleTextField);

        vertexWeightLabel = new JLabel("Введите вес вершины");
        vertexWeightLabel.setVisible(this.isWeighted);
        addVertexPanel.add(vertexWeightLabel);

        vertexWeightTextField = new JTextField();
        vertexWeightTextField.setVisible(this.isWeighted);
        addVertexPanel.add(vertexWeightTextField);

        addVertexButton = new JButton("Создать вершину");
        addVertexButton.addActionListener(e -> addVertexWithButton());
        addVertexPanel.add(addVertexButton);

        edgeDeletingPanel = new JPanel();
        edgeDeletingPanel.setLayout(new BoxLayout(edgeDeletingPanel, BoxLayout.PAGE_AXIS));
        controlPanel.add(edgeDeletingPanel);

        edgeDeletingLabel = new JLabel("Удалить ребро");
        edgeDeletingPanel.add(edgeDeletingLabel);

        edgeDeletingInfoLabel = new JLabel("Порядок вершин неважен");
        edgeDeletingInfoLabel.setVisible(this.isDigraph);
        edgeDeletingPanel.add(edgeDeletingInfoLabel);

        fromVertexTitleLabel = new JLabel("Название вершины исхода");
        edgeDeletingPanel.add(fromVertexTitleLabel);

        fromVertexTitleTextField = new JTextField();
        edgeDeletingPanel.add(fromVertexTitleTextField);

        toVertexTitleLabel = new JLabel("Название вершины захода");
        edgeDeletingPanel.add(toVertexTitleLabel);

        toVertexTitleTextField = new JTextField();
        edgeDeletingPanel.add(toVertexTitleTextField);

        deleteEdgeButton = new JButton("Удалить ребро");
        deleteEdgeButton.addActionListener(e -> deleteEdgeWithButton());
        edgeDeletingPanel.add(deleteEdgeButton);

        statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        add(statusPanel, BorderLayout.SOUTH);

        mousePositionOnCanvasLabel = new JLabel("X: -; Y: -;");
        statusPanel.add(mousePositionOnCanvasLabel);

        isCanvasActiveLabel = new JLabel("-");
        statusPanel.add(isCanvasActiveLabel);
    }

    private void deleteEdgeWithButton() {
        String fromVertexTitle = fromVertexTitleTextField.getText();
        Vertex fromVertex = getVertexWithTitle(fromVertexTitle);
        if (fromVertex == null) {
            return;
        }

        String toVertexTitle = toVertexTitleTextField.getText();
        Vertex toVertex = getVertexWithTitle(toVertexTitle);
        if (toVertex == null) {
            return;
        }

        Edge deletingEdge;
        if (isDigraph) {
            deletingEdge = getOrientedEdgeBetween(fromVertex, toVertex);
        } else {
            deletingEdge = getEdgeBetween(fromVertex, toVertex);
        }
        if (deletingEdge == null) {
            return;
        }
        boolean result = deleteEdge(deletingEdge);
        if (result) {
            fromVertexTitleTextField.setText("");
            toVertexTitleTextField.setText("");
        }
    }

    private Vertex getVertexWithTitle(String title) {
        return vertices.stream()
                .filter(v -> v.title.equals(title))
                .findFirst().orElse(null);

    }

    private Edge getEdgeBetween(Vertex v1, Vertex v2) {
        return v1.incidentEdges.stream()
                .filter(edge -> edge.fromVertex.equals(v1) && edge.toVertex.equals(v2) ||
                edge.fromVertex.equals(v2) && edge.toVertex.equals(v1))
                .findFirst().orElse(null);
    }

    private boolean deleteEdge(Edge edge) {
         boolean result = edge.fromVertex.incidentEdges.remove(edge) &&
            edge.toVertex.incidentEdges.remove(edge);
         repaint();
         return result;
    }

    private Edge getOrientedEdgeBetween(Vertex fromV, Vertex toV) {
        return fromV.incidentEdges.stream()
                .filter(edge -> edge.fromVertex.equals(fromV) && edge.toVertex.equals(toV))
                .findFirst().orElse(null);
    }

    private void switchDigraph(ChangeEvent e) {
        isDigraph = ((JCheckBox) e.getSource()).isSelected();
        canvas.repaint();
    }

    private void switchGraphWeight(ChangeEvent e) {
        this.isWeighted = ((JCheckBox) e.getSource()).isSelected();
        vertexWeightLabel.setVisible(this.isWeighted);
        vertexWeightTextField.setVisible(this.isWeighted);
        canvas.repaint();
    }

    private void addVertexWithButton() {
        String title = vertexTitleTextField.getText();
        Point newVertexCenter = new Point((int) VERTEX_RADIUS, (int) VERTEX_RADIUS);
        String weight = vertexWeightTextField.getText();
        Double weightValue = isWeighted ? Double.parseDouble(weight) : null;

        Vertex newVertex = addVertex(title, newVertexCenter);
        if (newVertex == null) {
            vertexTitleErrorLabel.setVisible(true);
        } else {
            vertexTitleErrorLabel.setVisible(false);
            newVertex.weight = weightValue;
        }

        vertexTitleTextField.setText("");
        vertexWeightTextField.setText("");
    }

    private void rightClickOnVertex(MouseEvent e, Vertex clickedVertex) {
        switch (newEdgePoints.size()) {
            case 0 -> {
                newEdgePoints.put(VertexStatus.FROM, clickedVertex);
                clickedVertex.mark();
            }
            case 1 -> {
                newEdgePoints.put(VertexStatus.TO, clickedVertex);
                Vertex from = newEdgePoints.get(VertexStatus.FROM);
                from.unmark();
                Vertex to = newEdgePoints.get(VertexStatus.TO);
                Edge createdEdge = createEdge(from, to);
                from.incidentEdges.add(createdEdge);
                to.incidentEdges.add(createdEdge);
                newEdgePoints.clear();
            }
        }
    }

    private String getMousePositionString(MouseEvent e) {
        return String.format("X: %d; Y: %d;", e.getX(), e.getY());
    }

    private String getCanvasActiveStatusString() {
        boolean isActive = this.canvas.isFocusOwner();
        return isActive ? "Canvas is active" : "Canvas isn't active (click on it to activate)";
    }

    private Edge createEdge(Vertex from, Vertex to) {
        return new Edge(from, to);
    }

    private void leftClickWithShiftOnEmptySpace(MouseEvent e) {
        if (!canvas.contains(e.getPoint())) {
            return;
        };
        addVertex(vertices.size() + "", e.getPoint());
    }

    private void leftClickOnEmptySpace(MouseEvent e) {
        clearSelection();
    }

    private void leftClickOnVertex(MouseEvent e, Vertex clickedVertex) {
        clearSelection();
        addVertexToSelection(clickedVertex);
    }

    private void leftClickWithCtrlOnVertex(MouseEvent e, Vertex clickedVertex) {
        if (clickedVertex.selected) {
            removeVertexFromSelection(clickedVertex);
        } else {
            addVertexToSelection(clickedVertex);
        }
    }

    private void controlPressed(KeyEvent e) {
        isCtrlPressed = true;
    }

    private void deletePressed(KeyEvent e) {
        deleteVertices(selectedVertices);
        clearSelection();
    }

    private void shiftPressed(KeyEvent e) {
        isShiftPressed = true;
    }

    private void shiftReleased(KeyEvent e) {
        isShiftPressed = false;
    }

    private void controlReleased(KeyEvent e) {
        isCtrlPressed = false;
    }

    private void dragVertex(MouseEvent e, Vertex vertex) {
        vertex.moveTo(e.getPoint());
    }

    private void addVertexToSelection(Vertex vertex) {
        vertex.select();
        selectedVertices.add(vertex);
    }

    private void deleteVertices(List<Vertex> verticesForDeleting) {
        for (Vertex v : verticesForDeleting) {
            vertices.remove(v);
            if (newEdgePoints.containsValue(v)) {
                newEdgePoints.clear();
            }
            v.delete();
        }
    }

    private void clearSelection() {
        selectedVertices.forEach(Vertex::unselect);
        selectedVertices.clear();
    }

    private void removeVertexFromSelection(Vertex vertex) {
        vertex.unselect();
        selectedVertices.remove(vertex);
    }

    private Vertex addVertex(String title, Point centerPoint) {
        if (vertices.stream().anyMatch(vertex -> vertex.title.equals(title))) {
            return null;
        }
        Vertex vertex = new Vertex(title, centerPoint);
        vertices.add(vertex);
        repaint();
        return vertex;
    }

    private void updateMousePositionLabel(MouseEvent e) {
        mousePositionOnCanvasLabel.setText(getMousePositionString(e));
    }

    private void updateCanvasActiveLabel() {
        isCanvasActiveLabel.setText(getCanvasActiveStatusString());
    }

    public Graph getGraph() {
        Graph graph;
        if (isDigraph) {
            graph = new AdjListsDigraph();
        } else {
            graph = new AdjListsGraph();
        }

        Map<Vertex, Integer> vertexToInt = new HashMap<>();
        int vertexIndex = 0;
        for (Vertex v : vertices) {
            vertexToInt.put(v, vertexIndex++);
        }
        for (Vertex v : vertices) {
            for (Edge e : v.incidentEdges) {
                int from = vertexToInt.get(e.fromVertex);
                int to = vertexToInt.get(e.toVertex);
                System.out.printf("from: %d -> to: %d%n", from, to);
                graph.addAdge(from, to);
            }
        }
        return graph;
    }

    private class Canvas extends JComponent {
        protected Canvas() {
            this.setFocusable(true);
            this.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    updateCanvasActiveLabel();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    updateCanvasActiveLabel();
                }
            });
            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                        controlPressed(e);
                    }
                    if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                        shiftPressed(e);
                    }
                    if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                        deletePressed(e);
                    }
                    repaint();
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                        controlReleased(e);
                    }
                    if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                        shiftReleased(e);
                    }
                    repaint();
                }
            });
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    canvas.grabFocus();
                    Point clickedPoint = e.getPoint();
                    Vertex clickedVertex = vertices.stream()
                            .filter(v -> v.shape.contains(clickedPoint))
                            .max(Comparator.comparingInt(Vertex::getId)).orElse(null);
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        if (clickedVertex == null) {
                            leftClickOnEmptySpace(e);
                            if (isShiftPressed) {
                                leftClickWithShiftOnEmptySpace(e);
                            }
                        } else {
                            if (isCtrlPressed) {
                                leftClickWithCtrlOnVertex(e, clickedVertex);
                            } else {
                                leftClickOnVertex(e, clickedVertex);
                            }
                        }
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        if (clickedVertex != null) {
                            rightClickOnVertex(e, clickedVertex);
                        }
                    }
                    repaint();
                }
            });
            this.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    updateMousePositionLabel(e);
                    Point draggedPoint = e.getPoint();
                    Vertex draggedVertex = vertices.stream()
                            .filter(v -> v.shape.contains(draggedPoint) && v.selected)
                            .max(Comparator.comparingInt(Vertex::getId)).orElse(null);
                    if (draggedVertex != null) {
                        dragVertex(e, draggedVertex);
                    }
                    repaint();
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    updateMousePositionLabel(e);
                }
            });
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2D = (Graphics2D) g;
            g2D.setColor(FILL_COLOR);
            g2D.fillRect(0, 0, this.getWidth(), this.getHeight());
            vertices.forEach(vertex -> vertex.incidentEdges.forEach(edge -> edge.draw(g2D, isDigraph)));
            vertices.forEach(vertex -> vertex.draw(g2D));
        }
    }

    private class Vertex {
        static int totalQuantity = 0;
        boolean selected = false;
        int id;
        Color fillColor;
        int strokeWidth;
        Color strokeColor;
        String title;
        Double weight;
        List<Edge> incidentEdges = new ArrayList<>();
        WeightLabel weightLabel;
        double x;
        double y;
        double w;
        double h;
        Shape shape;

        public Vertex(String title, double x, double y) {
            this.w = getWidth(title);
            this.h = getHeight(title);

            this.x = x;
            x = getCenterX();

            this.y = y;
            y = getCenterY();

            this.shape = setShape();
            this.fillColor = FILL_COLOR;
            this.strokeColor = DEFAULT_STROKE_COLOR;
            this.strokeWidth = DEFAULT_STROKE_WIDTH;
            this.title = title;
            this.weight = 0.0;
            this.id = totalQuantity++;
        }

        public Vertex(String title, Point centerPoint) {
            this(title, centerPoint.x, centerPoint.y);
        }

        public void setWeight(double value) {
            this.weight = value;
//            weightLabel = new WeightLabel();
        }

        private double getWidth(String title) {
            if (title.length() > 1) {
                return FONT_SIZE * (title.length() * SIZE_SCALE + 1);
            }
            return 2 * VERTEX_RADIUS;
        }

        private double getHeight(String title) {
            return 2 * VERTEX_RADIUS;
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

            g2D.setColor(fillColor);
            g2D.fill(shape);

            g2D.setColor(strokeColor);
            g2D.draw(shape);

            Rectangle2D vertexShapeBounds = shape.getBounds();
            int x = (int) vertexShapeBounds.getX(),
                y = (int) vertexShapeBounds.getY(),
                w = (int) vertexShapeBounds.getWidth(),
                h = (int) vertexShapeBounds.getHeight();
            DrawUtils.drawStringInCenter(g2D,
                    VERTEX_FONT, title,
                    x, y, w, h);

            g2D.setColor(oldColor);
            g2D.setFont(oldFont);
        }

        void select() {
            this.selected = true;
            this.fillColor = SELECTION_FILL_COLOR;
        }

        void unselect() {
            this.selected = false;
            this.fillColor = FILL_COLOR;
        }

        void mark() {
            this.strokeColor = MARK_STROKE_COLOR;
        }

        void unmark() {
            this.strokeColor = DEFAULT_STROKE_COLOR;
        }

        int getId() {
            return this.id;
        }

        double getCenterX() {
            return this.x - w / 2;
        }

        double getCenterY() {
            return this.y - h / 2;
        }

        Shape setShape() {
            return new Ellipse2D.Double(getCenterX(), getCenterY(), w, h);
        }

        void moveTo(Point centerPoint) {
            this.x = centerPoint.getX();
            double widthHalf = this.shape.getBounds().getWidth() / 2;
            if (this.x < widthHalf) {
                this.x = widthHalf;
            } else if (this.x > canvas.getWidth() - widthHalf) {
                this.x = canvas.getWidth() - widthHalf;
            }

            this.y = centerPoint.getY();
            double heightHalf = this.shape.getBounds().getHeight() / 2;
            if (this.y < heightHalf) {
                this.y = heightHalf;
            } else if (this.y > canvas.getHeight() - heightHalf) {
                this.y = canvas.getHeight() - heightHalf;
            }

            this.shape = setShape();
            this.incidentEdges.forEach(Edge::updateLine);
        }

        void delete() {
            while (!incidentEdges.isEmpty()) {
                Edge e = incidentEdges.get(0);
                e.fromVertex.incidentEdges.remove(e);
                e.toVertex.incidentEdges.remove(e);
            }
        }
    }

    private static class Edge {
        Color strokeColor;
        String title;
        Double weight;
        Vertex fromVertex;
        Vertex toVertex;
        WeightLabel weightLabel;
        Line2D line;

        public Edge(Vertex from, Vertex to) {
            this.fromVertex = from;
            this.toVertex = to;
            double x1 = fromVertex.x, y1 = fromVertex.y,
                    x2 = toVertex.x, y2 = toVertex.y;
            this.line = new Line2D.Double(x1, y1, x2, y2);
            this.strokeColor = DEFAULT_STROKE_COLOR;
            this.title = null;
            this.weight = null;
            this.weightLabel = null;
        }

        void updateLine() {
            double x1 = fromVertex.x, y1 = fromVertex.y,
                    x2 = toVertex.x, y2 = toVertex.y;
            this.line.setLine(new Line2D.Double(x1, y1, x2, y2));
        }

        void draw(Graphics g, boolean isDigraph) {
            Graphics2D g2D = (Graphics2D) g;
            Color oldColor = g2D.getColor();

            g2D.setColor(this.strokeColor);
            g2D.draw(line);

            if (isDigraph) {
                double x1 = fromVertex.x, y1 = fromVertex.y,
                        x2 = toVertex.x, y2 = toVertex.y;
                double distanceBetweenVertices = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
                double xa = VERTEX_RADIUS * (x1 - x2) / distanceBetweenVertices + x2;
                double ya = VERTEX_RADIUS * (y1 - y2) / distanceBetweenVertices + y2;

                double k = (y2 - y1) / (x2 - x1);
                double cosRotation = 1 / Math.sqrt(1 + Math.pow(k, 2));
                double sinRotation = k / Math.sqrt(1 + Math.pow(k, 2));
                if (x2 - x1 == 0) {
                    cosRotation = 0;
                    sinRotation = 1;
                }

                double xbTemp = ARROW_LENGTH * Math.cos(ARROW_ANGLE);
                double ybTemp = ARROW_LENGTH * Math.sin(ARROW_ANGLE);

                double xb = xbTemp * cosRotation - ybTemp * sinRotation + xa;
                double yb = xbTemp * sinRotation + ybTemp * cosRotation + ya;

                double xcTemp = ARROW_LENGTH * Math.cos(-ARROW_ANGLE);
                double ycTemp = ARROW_LENGTH * Math.sin(-ARROW_ANGLE);

                double xc = xcTemp * cosRotation - ycTemp * sinRotation + xa;
                double yc = xcTemp * sinRotation + ycTemp * cosRotation + ya;

                int[] arrowX = {(int) xa, (int) xb, (int) xc};
                int[] arrowY = {(int) ya, (int) yb, (int) yc};

                Polygon2D arrow = new Polygon2D(arrowX, arrowY, 3);
                g2D.fill(arrow);

                double minusxb = -xbTemp * cosRotation + ybTemp * sinRotation + xa;
                double minusyb = -xbTemp * sinRotation - ybTemp * cosRotation + ya;

                double minusxc = -xcTemp * cosRotation + ycTemp * sinRotation + xa;
                double minusyc = -xcTemp * sinRotation - ycTemp * cosRotation + ya;

                int[] minusArrowX = {(int) xa, (int) minusxb, (int) minusxc};
                int[] minusArrowY = {(int) ya, (int) minusyb, (int) minusyc};

                Polygon2D minusArrow = new Polygon2D(minusArrowX, minusArrowY, 3);
                g2D.fill(minusArrow);
            }

            g2D.setColor(oldColor);
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
