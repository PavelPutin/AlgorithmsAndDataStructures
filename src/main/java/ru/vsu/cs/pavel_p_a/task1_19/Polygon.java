package ru.vsu.cs.pavel_p_a.task1_19;

import java.awt.*;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Polygon {

    private List<Point> pointList = new ArrayList();

    private double minX, minY, maxX, maxY;

    private TransformOrigin originX = TransformOrigin.CENTER;
    private TransformOrigin originY = TransformOrigin.CENTER;
    private Point transformOrigin = new Point(0, 0);

    public Polygon(Point ... points) {
        for (Point point : points) {
            double x = point.getX(), y = point.getY();
            pointList.add(new Point(x, y));
        }
        setTransformOrigin();
    }

    public Polygon(List<Point> pointList) {
        for (Point point : pointList) {
            double x = point.getX(), y = point.getY();
            pointList.add(new Point(x, y));
        }
        setTransformOrigin();
    }

    public double getArea() {
        if (pointList.size() <= 2) {
            return 0;
        }
        double area = 0;
        int n = pointList.size();

        for (int i = 0; i < n - 1; i++) {
            area += pointList.get(i).getX() * pointList.get(i + 1).getY();
        }
        area += pointList.get(n - 1).getX() * pointList.get(0).getY();

        for (int i = 0; i < n - 1; i++) {
            area -= pointList.get(i + 1).getX() * pointList.get(i).getY();
        }
        area -= pointList.get(0).getX() * pointList.get(n - 1).getY();

        return Math.abs(area) / 2;
    }

    public double getPerimeter() {
        double perimeter = 0;
        int n = pointList.size();

        for (int i = 0; i < n - 1; i++) {
            perimeter += pointList.get(i).distance(pointList.get(i + 1));
        }

        return perimeter;
    }

    public void translate(double x, double y) {
        for (Point point : pointList) {
            point.setLocation(point.getX() + x, point.getY() + y);
        }
        setTransformOrigin(originX, originY);
    }

    public void translateX(double x) {
        for (Point point : pointList) {
            point.setLocation(point.getX() + x, point.getY());
        }
        setTransformOrigin(originX, originY);
    }

    public void translateY(double y) {
        for (Point point : pointList) {
            point.setLocation(point.getX(), point.getY() + y);
        }
        setTransformOrigin(originX, originY);
    }

    public void scale(double x, double y) {
        double x0 = transformOrigin.getX();
        double y0 = transformOrigin.getY();

        for (Point point : pointList) {
            double deltaX = (x0 - point.getX());
            double deltaY = (y0 - point.getY());

            point.setLocation(x0 - x * deltaX, y0 - y * deltaY);
        }

        setTransformOrigin(originX, originY);
    }

    public void scaleX(double x) {
        double x0 = transformOrigin.getX();

        for (Point point : pointList) {
            double deltaX = (x0 - point.getX());

            point.setLocation(x0 - x * deltaX, point.getY());
        }

        setTransformOrigin(originX, originY);
    }

    public void scaleY(double y) {
        double y0 = transformOrigin.getY();

        for (Point point : pointList) {
            double deltaY = (y0 - point.getY());

            point.setLocation(point.getX(), y0 - y * deltaY);
        }

        setTransformOrigin(originX, originY);
    }

    public void matrix(double scaleX, double scaleY, double translateX, double translateY) {
        translate(translateX, translateY);
        scale(scaleX, scaleY);
    }

    public void setTransformOrigin() {
        this.setTransformOrigin(TransformOrigin.CENTER, TransformOrigin.CENTER);
    }

    public void setTransformOrigin(TransformOrigin origin1, TransformOrigin origin2) {
        if (origin1.ordinal() == origin2.ordinal() && origin1 != TransformOrigin.CENTER) {
            throw new IllegalArgumentException("Переданы неверные параметры точки трансформации");
        }

        TransformOrigin originX, originY;
        if (origin1.isHorizontal() && origin2.isVertical()) {
            originX = origin1;
            originY = origin2;
        } else {
            originY = origin1;
            originX = origin2;
        }

        calcMaxAndMinCoordinates();

        double x, y;

        x = switch (originX) {
            case LEFT -> minX;
            case CENTER -> (maxX + minX) / 2;
            case RIGHT -> maxX;
            default -> 0;
        };

        y = switch (originY) {
            case TOP -> minY;
            case CENTER -> (maxY + minY) / 2;
            case BOTTOM -> maxY;
            default -> 0;
        };

        transformOrigin = new Point(x, y);
        this.originX = originX;
        this.originY = originY;
    }

    public Polygon getCircumscribedRectangle() {
        calcMaxAndMinCoordinates();
        return new Polygon(
                new Point(minX, maxY),
                new Point(maxX, maxY),
                new Point(maxX, minY),
                new Point(minX, minY));
    }

    public void draw(Graphics2D g2D) {
        for (int i = 0; i < pointList.size(); i++) {
            Point point1 = pointList.get(i);
            Point point2 = pointList.get((i + 1) % pointList.size());

            int x1 = (int) point1.getX(), y1 = (int) point1.getY(),
                    x2 = (int) point2.getX(), y2 = (int) point2.getY();
            g2D.drawLine(x1, y1, x2, y2);
        }
    }

    public void saveAsTxt(File file) throws Exception {
        validateFile(file, "poly");
        StringBuilder pointsCoordinatesString = new StringBuilder();

        for (Point point : pointList) {
            double x = point.getX();
            double y = point.getY();

            pointsCoordinatesString.append(x).append(" ").append(y).append("\n");
        }

        PrintStream out = new PrintStream(file);
        out.println(pointsCoordinatesString);
        out.close();
    }

    public void saveAsSvgTo(File file) throws Exception {
        validateFile(file, "svg");

        StringBuilder svg = new StringBuilder("<?xml version=\"1.0\" standalone=\"no\"?>");
        svg.append(String.format("<svg width=\"%1$f\" height=\"%2$f\" viewBox=\"0 0 %1$f %2$f\"\n" +
                "     xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">", Math.abs(maxX), Math.abs(maxY)));
        svg.append("<polygon points=\"");

        boolean first = true;
        for (Point point : pointList) {
            double x = point.getX();
            double y = point.getY();

            svg.append((first ? "" : " ")).append(x).append(",").append(y);
            first = false;
        }

        svg.append("\" fill=\"none\" stroke-width=\"1\" stroke=\"black\"/></svg>");

        PrintStream out = new PrintStream(file);
        out.print(svg);
        out.close();
    }

    private void validateFile(File file, String extension) throws Exception {
        if (!file.isFile()) {
            throw new IllegalArgumentException("Передан не файл");
        }

        if (!file.canWrite()) {
            throw new Exception("Файл недоступен для записи");
        }

        if (!file.getName().endsWith("." + extension)) {
            throw new IllegalArgumentException(String.format("Передан не %s файл", extension));
        }
    }


    private void calcMaxAndMinCoordinates() {
        Point firstPoint = pointList.get(0);
        minX = (int) firstPoint.getX();
        minY = (int) firstPoint.getY();
        maxX = (int) firstPoint.getX();
        maxY = (int) firstPoint.getY();

        for (Point point : pointList) {
            int x = (int) point.getX(), y = (int) point.getY();
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }
    }
}
