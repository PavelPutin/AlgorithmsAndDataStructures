package ru.vsu.cs.pavel_p_a.task1_19;

public enum TransformOrigin {
    LEFT(false, true),
    RIGHT(false, true),
    TOP(true, false),
    BOTTOM(true, false),
    CENTER(true, true);

    private final boolean vertical;
    private final boolean horizontal;

    TransformOrigin(boolean vertical, boolean horizontal) {
        this.vertical = vertical;
        this.horizontal = horizontal;
    }

    public boolean isVertical() {
        return vertical;
    }

    public boolean isHorizontal() {
        return horizontal;
    }
}
