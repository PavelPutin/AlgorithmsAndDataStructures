package ru.vsu.cs.pavel_p_a.task4_12;

import java.util.Arrays;

public class SortState {
    private int[] array;
    private int i;
    private int j;

    public SortState(int[] array, int i, int j) {
        this.array = array;
        this.i = i;
        this.j = j;
    }

    public int[] getArray() {
        return array;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    @Override
    public String toString() {
        return "SortState{" +
                "array=" + Arrays.toString(array) +
                ", i=" + i +
                ", j=" + j +
                '}';
    }
}
