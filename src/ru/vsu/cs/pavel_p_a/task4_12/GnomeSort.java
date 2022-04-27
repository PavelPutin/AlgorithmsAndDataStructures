package ru.vsu.cs.pavel_p_a.task4_12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GnomeSort {

    public static List<SortState> sort(int[] arr) {
        List<SortState> sortStates = new ArrayList<>();

        int i = 1;
        int j = 2;
        sortStates.add(new SortState(Arrays.copyOf(arr, arr.length), i, j));
        while (i < arr.length) {
            if (arr[i - 1] <= arr[i]) {
                i = j;
                j++;
            } else {
                swap(arr, i - 1, i);
                i--;
                if (i == 0) {
                    i = j;
                    j++;
                }
            }
            sortStates.add(new SortState(Arrays.copyOf(arr, arr.length), i, j));
        }

        return sortStates;
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
