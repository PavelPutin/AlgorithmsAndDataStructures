package ru.vsu.cs.pavel_p_a.task3_06;

import java.util.ArrayDeque;
import java.util.Queue;

public class LogicJava {

    public static int[] getOrigin(int[] sequence) {
        Queue<Integer> originQueue = new ArrayDeque<>();
        int[] origin = new int[sequence.length];

        for (int i = sequence.length - 1; i >= 0; i--) {
            int element = sequence[i];
            originQueue.add(element);
            if (originQueue.size() < sequence.length) {
                Integer head = originQueue.poll();
                originQueue.add(head);
            }
        }
//
        for (int i = sequence.length - 1; i >= 0; i--) {
            origin[i] = originQueue.poll();
        }

        return origin;
    }

}
