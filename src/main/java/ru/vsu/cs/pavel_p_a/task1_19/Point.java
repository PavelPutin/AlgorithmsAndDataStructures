package ru.vsu.cs.pavel_p_a.task1_19;

import java.awt.geom.Point2D;

public class Point extends Point2D.Double {

    public Point(double x, double y) {
        super(x, y);
    }

    @Override
    public double getX() {
        return this.x;
    }

    @Override
    public double getY() {
        return this.y;
    }

    @Override
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
