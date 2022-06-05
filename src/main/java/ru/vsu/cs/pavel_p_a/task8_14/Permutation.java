package ru.vsu.cs.pavel_p_a.task8_14;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.IntStream;

public class Permutation implements Iterable<Integer[]> {
    private Integer[] currentPermutation;
    private int n;
    private boolean last;

    public Permutation(int n) {
        this.n = n;
        this.last = false;
        currentPermutation = new Integer[n];
        Arrays.fill(currentPermutation, 0);

    }
    @Override
    public Iterator<Integer[]> iterator() {
        return new Iterator<Integer[]>() {
            @Override
            public boolean hasNext() {
                return !last;
            }

            @Override
            public Integer[] next() {
                boolean emptyPermutation = Arrays.stream(currentPermutation).allMatch((item) -> item.equals(0));
                if (emptyPermutation) {
                    for (int i = 0; i < n; i++) {
                        currentPermutation[i] = i;
                    }
                } else {
                    int firstLessElementIndex = n - 1;
                    while (--firstLessElementIndex >= 0 && currentPermutation[firstLessElementIndex] > currentPermutation[firstLessElementIndex + 1]);

                    for (int i = firstLessElementIndex + 1, j = n - 1; i < j; i++, j--) {
                        swapArrayElements(currentPermutation, i, j);
                    }

                    int leastGreaterElement = firstLessElementIndex + 1;
                    while (currentPermutation[leastGreaterElement] < currentPermutation[firstLessElementIndex]) {
                        leastGreaterElement++;
                    }

                    swapArrayElements(currentPermutation, firstLessElementIndex, leastGreaterElement);

                    last = true;
                    for (int i = 1; i < n; i++) {
                        last = last && currentPermutation[i] < currentPermutation[i - 1];
                    }
                }
                return currentPermutation;
            }
        };
    }

    private int getOppositeInt(int from, int to, int currentValue) {
        return to - currentValue + from - 1;
    }

    private <T> void  swapArrayElements(T[] array, int swapIndex1, int swapIndex2) {
        T temp = array[swapIndex1];
        array[swapIndex1] = array[swapIndex2];
        array[swapIndex2] = temp;
    }
}
