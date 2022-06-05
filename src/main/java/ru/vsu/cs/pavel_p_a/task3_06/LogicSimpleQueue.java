package ru.vsu.cs.pavel_p_a.task3_06;

public class LogicSimpleQueue {
    public static int[] getOrigin(int[] sequence) throws Exception {
        SimpleQueue<Integer> originQueue = new SimpleLinkedListQueue2<>();
        int[] origin = new int[sequence.length];

        for (int i = sequence.length - 1; i >= 0; i--) {
            int element = sequence[i];
            originQueue.add(element);
            if (originQueue.count() < sequence.length) {
                Integer head = originQueue.remove();
                originQueue.add(head);
            }
        }

        for (int i = sequence.length - 1; i >= 0; i--) {
            origin[i] = originQueue.remove();
        }

        return origin;
    }
}
