package ru.vsu.cs.pavel_p_a.task2_29;

import java.util.Comparator;
import java.util.Iterator;

public class SimpleLinkedList <T> implements Iterable<T> {

    public static class SimpleLinkedListException extends Exception{
        public SimpleLinkedListException(String message) {
            super(message);
        }
    }

    private class SimpleLinkedListNode {
        public T value;
        public SimpleLinkedListNode next;

        public SimpleLinkedListNode(T value, SimpleLinkedListNode next) {
            this.value = value;
            this.next = next;
        }

        public SimpleLinkedListNode(T value) {
            this(value, null);
        }
    }

    private SimpleLinkedListNode head = null; // first top
    private SimpleLinkedListNode tail = null; // last
    private int size = 0;

    public int size() {
        return size;
    }

    public void addFirst(T value) {
        head = new SimpleLinkedListNode(value, head);
        if (size == 0) {
            tail = head = new SimpleLinkedListNode(value);
        }
        size++;
    }

    public void removeFirst() throws SimpleLinkedListException {
        checkEmptyError();
        head = head.next;
        if (size == 1) {
            tail = null;
        }
        size--;
    }


    public void addLast(T value) {
        if (size == 0) {
            head = tail = new SimpleLinkedListNode(value);
        } else {
            tail.next = new SimpleLinkedListNode(value);
            tail = tail.next;
        }
        size++;
    }

    public void removeLast() throws SimpleLinkedListException {
        checkEmptyError();
        if (size == 1) {
            head = tail = null;
        } else {
            tail = getNode(size - 2);
            tail.next = null;
        }
        size--;
    }

    public T get(int index) throws SimpleLinkedListException {
        checkEmptyError();
        checkIncorrectIndexError(index);
        return getNode(index).value;
    }

    public void remove(int index) throws SimpleLinkedListException {
        checkEmptyError();
        checkIncorrectIndexError(index);

        if (index == 0) {
            removeFirst();
        } else {
            SimpleLinkedListNode prev = getNode(index - 1);
            prev.next = prev.next.next;
            if (prev.next == null) {
                tail = prev;
            }
        }
        size--;
    }

    public T getFirst() throws SimpleLinkedListException {
        checkEmptyError();
        return head.value;
    }

    public T getLast() throws SimpleLinkedListException {
        checkEmptyError();
        return tail.value;
    }

    public void bubbleSort(Comparator<T> comparator) {
        for (int i = 0; i < size - 1; i++) {
            boolean wasChange = false;

            SimpleLinkedListNode top = head;
            if (comparator.compare(top.value, top.next.value) < 0) {
                head = top.next;
                SimpleLinkedListNode temp = top.next.next;
                top.next.next = top;
                top.next = temp;
                wasChange = true;
            }

            int j = 0;
            for (SimpleLinkedListNode curr = head; j < size - 2; curr = curr.next) {
                if (comparator.compare(curr.next.value, curr.next.next.value) < 0) {
                    if (j == size - 3) {
                        tail = curr.next;
                    }

                    SimpleLinkedListNode cn = curr.next;
                    SimpleLinkedListNode cnnn = curr.next.next.next;

                    curr.next = curr.next.next;
                    curr.next.next = cn;
                    curr.next.next.next = cnnn;

                    wasChange = true;
                }
                j++;
            }
            if (!wasChange) {
                break;
            }
        }
    }

    @Override
    public Iterator iterator() {
        class SimpleLinkedListIterator implements Iterator<T> {
            SimpleLinkedListNode curr = head;

            @Override
            public boolean hasNext() {
                return curr != null;
            }

            @Override
            public T next() {
                T value = curr.value;
                curr = curr.next;
                return value;
            }
        }
        return new SimpleLinkedListIterator();
    }

    private void checkEmptyError() throws SimpleLinkedListException {
        if (size == 0) {
            throw new SimpleLinkedListException("Empty list");
        }
    }

    private void checkIncorrectIndexError(int index) throws SimpleLinkedListException {
        if (index < 0 || index >= size) {
            throw new SimpleLinkedListException("Index is incorrect");
        }
    }

    private SimpleLinkedListNode getNode(int index) {
        SimpleLinkedListNode current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current;
    }
}
