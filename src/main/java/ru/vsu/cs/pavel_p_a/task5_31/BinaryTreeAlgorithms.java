package ru.vsu.cs.pavel_p_a.task5_31;

import java.util.*;

public class BinaryTreeAlgorithms {

    @FunctionalInterface
    public interface Visitor<T> {
        void visit(T value, int level);
    }

    // NLR
    public static <T> void preOrderVisit(BinaryTree.TreeNode<T> treeNode, Visitor<T> visitor) {
        class Inner {
            void preOrderVisit(BinaryTree.TreeNode<T> treeNode, Visitor<T> visitor, int level) {
                if (treeNode == null) {
                    return;
                }
                visitor.visit(treeNode.getValue(), level);
                preOrderVisit(treeNode.getLeft(), visitor, level + 1);
                preOrderVisit(treeNode.getRight(), visitor, level + 1);
            }
        }

        new Inner().preOrderVisit(treeNode, visitor, 0);
    }

    public static <T> Iterable<T> preOrderValues(BinaryTree.TreeNode<T> treeNode) {
        return () -> {
            Stack<BinaryTree.TreeNode<T>> stack = new Stack<>();
            stack.push(treeNode);

            return new Iterator<T>() {

                @Override
                public boolean hasNext() {
                    return !stack.isEmpty();
                }

                @Override
                public T next() {
                    BinaryTree.TreeNode<T> node = stack.pop();
                    if (node.getLeft() != null) {
                        stack.push(node.getLeft());
                    }
                    if (node.getRight() != null) {
                        stack.push(node.getRight());
                    }
                    return node.getValue();
                }
            };
        };
    }

    // LNR
    public static <T> void inOrderVisit(BinaryTree.TreeNode<T> treeNode, Visitor<T> visitor) {
        class Inner {
            void inOrderValues(BinaryTree.TreeNode<T> treeNode, Visitor<T> visitor, int level) {
                if (treeNode == null) {
                    return;
                }

                inOrderValues(treeNode.getLeft(), visitor, level + 1);
                visitor.visit(treeNode.getValue(), level);
                inOrderValues(treeNode.getRight(), visitor, level + 1);
            }
        }

        new Inner().inOrderValues(treeNode, visitor, 0);
    }

    public static <T> Iterable<T> inOrderValues(BinaryTree.TreeNode<T> treeNode) {
        return () -> {
            Stack<BinaryTree.TreeNode<T>> stack = new Stack<>();
            BinaryTree.TreeNode<T> node = treeNode;
            while (node != null) {
                stack.push(node);
                node = node.getLeft();
            }

            return new Iterator<T>() {
                @Override
                public boolean hasNext() {
                    return !stack.isEmpty();
                }

                @Override
                public T next() {
                    BinaryTree.TreeNode<T> node = stack.pop();
                    T result = node.getValue();

                    if (node.getRight() != null) {
                        node = node.getRight();
                        while (node != null) {
                            stack.push(node);
                            node = node.getLeft();
                        }
                    }

                    return result;
                }
            };

        };
    }

    // LRN
    public static <T> void postOrderVisit(BinaryTree.TreeNode<T> treeNode, Visitor<T> visitor) {
        class Inner {
            void inOrderValues(BinaryTree.TreeNode<T> treeNode, Visitor<T> visitor, int level) {
                if (treeNode == null) {
                    return;
                }

                inOrderValues(treeNode.getLeft(), visitor, level + 1);
                inOrderValues(treeNode.getRight(), visitor, level + 1);
                visitor.visit(treeNode.getValue(), level);
            }
        }

        new Inner().inOrderValues(treeNode, visitor, 0);
    }

    public static <T> Iterable<T> postOrderValues(BinaryTree.TreeNode<T> treeNode) {
        return () -> {
            BinaryTree.TreeNode<T> emptyNode = () -> null;

            Stack<BinaryTree.TreeNode<T>> stack = new Stack<>();
            Stack<T> valuesStack = new Stack<>();
            stack.push(treeNode);

            return new Iterator<T>() {
                @Override
                public boolean hasNext() {
                    return !stack.isEmpty();
                }

                @Override
                public T next() {
                    for (BinaryTree.TreeNode<T> node = stack.pop(); node != emptyNode; node = stack.pop()) {
                        if (node.getRight() == null && node.getLeft() == null) {
                            return node.getValue();
                        }

                        valuesStack.push(node.getValue());
                        stack.push(emptyNode);

                        if (node.getLeft() != null) {
                            stack.push(node.getLeft());
                        }
                        if (node.getRight() != null) {
                            stack.push(node.getRight());
                        }
                    }
                    return valuesStack.pop();
                }
            };
        };
    }

    private static class QueueItem<T> {
        public BinaryTree.TreeNode<T> node;
        public int level;

        public QueueItem(BinaryTree.TreeNode<T> node, int level) {
            this.node = node;
            this.level = level;
        }
    }

    public static <T> void byLevelVisit(BinaryTree.TreeNode<T> treeNode, Visitor<T> visitor) {
        Queue<QueueItem<T>> queue = new LinkedList<>();
        queue.add(new QueueItem<>(treeNode, 0));

        while (!queue.isEmpty()) {
            QueueItem<T> item = queue.poll();
            if (item.node.getLeft() != null) {
                queue.add(new QueueItem<>(item.node.getLeft(), item.level + 1));
            }
            if (item.node.getRight() != null) {
                queue.add(new QueueItem<>(item.node.getRight(), item.level + 1));
            }
            visitor.visit(item.node.getValue(), item.level);
        }
    }

    public static <T> Iterable<T> byLevelValues(BinaryTree.TreeNode<T> treeNode) {
        return () -> {
            Queue<QueueItem<T>> queue = new LinkedList<>();
            queue.add(new QueueItem<>(treeNode, 0));

            return new Iterator<T>() {

                @Override
                public boolean hasNext() {
                    return !queue.isEmpty();
                }

                @Override
                public T next() {
                    QueueItem<T> item = queue.poll();
                    if (item == null) {
                        return null;
                    }
                    if (item.node.getLeft() != null) {
                        queue.add(new QueueItem<>(item.node.getLeft(), item.level + 1));
                    }
                    if (item.node.getRight() != null) {
                        queue.add(new QueueItem<>(item.node.getRight(), item.level + 1));
                    }
                    return item.node.getValue();
                }
            };
        };
    }

    public static <T> String toBracketStr(BinaryTree.TreeNode<T> treeNode) {

        class Inner {
            void printTo(BinaryTree.TreeNode<T> node, StringBuilder sb) {
                if (node == null) {
                    return;
                }

                sb.append(node.getValue());
                if (node.getLeft() != null || node.getRight() != null) {
                    sb.append(" (");
                    printTo(node.getLeft(), sb);
                    if (node.getRight() != null) {
                        sb.append(", ");
                        printTo(node.getRight(), sb);
                    }
                    sb.append(")");
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        new Inner().printTo(treeNode, sb);
        return sb.toString();
    }

    public static <T extends Comparable<T>> List<List<BinaryTree.TreeNode<T>>> findAllEqualSubtrees(BinaryTree binaryTree) {
        return findAllEqualSubtrees(binaryTree, T::compareTo);
    }

    public static <T> List<List<BinaryTree.TreeNode<T>>> findAllEqualSubtrees(BinaryTree binaryTree, Comparator<T> c) {
        List<List<BinaryTree.TreeNode<T>>> result = new ArrayList<>();
        List<BinaryTree.TreeNode<T>> nodesList = new ArrayList<>();

        Queue<QueueItem<T>> queue = new LinkedList<>();
        queue.add(new QueueItem<>(binaryTree.getRoot(), 0));

        while (!queue.isEmpty()) {
            QueueItem<T> item = queue.poll();
            if (item.node.getLeft() != null) {
                queue.add(new QueueItem(item.node.getLeft(), item.level + 1));
            }
            if (item.node.getRight() != null) {
                queue.add(new QueueItem(item.node.getRight(), item.level + 1));
            }
            nodesList.add(item.node);
        }

        for (int i = 0; i < nodesList.size(); i++) {
            for (int j = 0; j < i; j++) {
                BinaryTree.TreeNode<T> node1 = nodesList.get(i);
                BinaryTree.TreeNode<T> node2 = nodesList.get(j);

                if (areTreesEqual(node1, node2, c)) {
                    List<BinaryTree.TreeNode<T>> pair = new ArrayList<>();
                    pair.add(node1);
                    pair.add(node2);
                    result.add(pair);
                }
            }
        }

        return result;
    }

    public static <T> boolean areTreesEqual(BinaryTree.TreeNode<T> treeNode1, BinaryTree.TreeNode<T> treeNode2, Comparator<T> c) {
        if (treeNode1 == treeNode2 || (treeNode1 != null && treeNode2 != null && c.compare(treeNode1.getValue(), treeNode2.getValue()) == 0)) {
            if (treeNode1 == null) {
                return true;
            }
            return areTreesEqual(treeNode1.getLeft(), treeNode2.getLeft(), c) && areTreesEqual(treeNode1.getRight(), treeNode2.getRight(), c);
        }
        return false;
    }

    public static <T extends Comparable<T>> boolean areTreesEqual(BinaryTree.TreeNode<T> treeNode1, BinaryTree.TreeNode<T> treeNode2) {
        return areTreesEqual(treeNode1, treeNode2, T::compareTo);
    }
}
