package ru.vsu.cs.pavel_p_a.task4_12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BubbleSort {

    public static List<SortState> sort(int[] arr) {
        List<SortState> sortStates = new ArrayList<>();

        int size = arr.length;
        sortStates.add(new SortState(Arrays.copyOf(arr, arr.length), 1, size - 1));

        for (int i = 1; i < size - 1; i++) {
            for (int j = size - 1; j >= i; j--) {
                if (arr[j - 1] > arr[j]) {
                    swap(arr, j - 1, j);
                }
                sortStates.add(new SortState(Arrays.copyOf(arr, arr.length), i, j));
            }
        }

        return sortStates;
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
