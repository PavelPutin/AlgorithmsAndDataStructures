package ru.vsu.cs.course1.tree.bst.rb;

import java.awt.Color;
import ru.vsu.cs.course1.tree.BinaryTree;
import ru.vsu.cs.course1.tree.bst.BSTree;
import ru.vsu.cs.course1.tree.bst.BSTreeAlgorithms;

/**
 * Рализация красно-черного дерева:
 * https://cs.lmu.edu/~ray/notes/redblacktrees/
 * https://cs.lmu.edu/~ray/notes/binarysearchtrees/
 *
 * @param <T>
 */
public class RBTree<T extends Comparable<? super T>> implements BSTree<T> {

    static final boolean RED   = true;
    static final boolean BLACK = false;

    // не указываем модификаторы доступа, чтобы был доступ из того же пакета
    // (в частности из RBTreeMap)
    class RBTreeNode implements BinaryTree.TreeNode<T> {

        public T value;
        public boolean color;
        public RBTreeNode left;
        public RBTreeNode right;
        public RBTreeNode parent;

        public RBTreeNode(T value, boolean color, RBTreeNode left, RBTreeNode right, RBTreeNode parent) {
            this.value = value;
            this.color = color;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }

        public RBTreeNode(T value, RBTreeNode parent) {
            this(value, BLACK, null, null, parent);
        }

        public RBTreeNode(T value) {
            this(value, null);
        }

        // Ниже идет реализация интерфейса BinaryTree.TreeNode<T>

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public BinaryTree.TreeNode<T> getLeft() {
            return left;
        }

        @Override
        public BinaryTree.TreeNode<T> getRight() {
            return right;
        }

        @Override
        public Color getColor() {
            return color == RED ? Color.RED : Color.BLACK;
        }
    }

    RBTreeNode root = null;
    private int size = 0;

    // Ниже идет реализация интерфейса BSTree<T> (@Override-методы)

    @Override
    public BinaryTree.TreeNode<T> getRoot() {
        return root;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T put(T value) {
        if (root == null) {
            setRoot(new RBTreeNode(value));
            size++;
            return null;
        }
        RBTreeNode node = root;
        while (true) {
            int cmp = value.compareTo(node.value);
            if (cmp == 0) {
                // в узле значение, равное value
                T oldValue = node.value;
                node.value = value;
                return oldValue;
            } else if (cmp < 0) {
                if (node.left == null) {
                    setLeft(node, new RBTreeNode(value));
                    size++;
                    correctAfterAdd(node.left);
                    return null;
                }
                node = node.left;
            } else {
                if (node.right == null) {
                    setRight(node, new RBTreeNode(value));
                    size++;
                    correctAfterAdd(node.right);
                    return null;
                }
                node = node.right;
            }
        }
    }

    @Override
    public T remove(T value) {
        RBTreeNode node = (RBTreeNode) getNode(value);
        if (node == null) {
            // в дереве нет такого значения
            return null;
        }
        T oldValue = node.value;
        if (node.left != null && node.right!= null) {
            RBTreeNode nextValueNode = (RBTreeNode) BSTreeAlgorithms.getMinNode(node.right);
            node.value = nextValueNode.value;
            node = nextValueNode;
        }
        // здесь node имеет не более одного потомка
        RBTreeNode child = (node.left != null) ? node.left : node.right;
        if (child != null) {
            if (node == root) {
                setRoot(child);
                root.color = BLACK;
            } else if (node.parent.left == node) {
                // child - левый потомок node
                setLeft(node.parent, child);
            } else {
                // child - правый потомок node
                setRight(node.parent, child);
            }
            if (node.color == BLACK) {
                // если удалили черный узел, то нарушилось равновесие по черной высоте
                correctAfterRemove(child);
            }
        } else if (node == root) {
            root = null;
        } else {
            // если удалили черный узел, то нарушилось равновесие по черной высоте
            if (node.color == BLACK) {
                correctAfterRemove(node);
            }
            removeFromParent(node);
        }
        size--;
        return oldValue;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }


    /**
     * Классический алгоритм восстановления красно-черного дерева после добавления узла
     */
    private void correctAfterAdd(RBTreeNode node) {
        // шаг 1: Цвет узла - красный
        if (node != null) {
            node.color = RED;
        }

        // шаг 2: Корректировка двух подряд красных узлов (если имеет место быть)
        if (node != null && node != root && colorOf(node.parent) == RED) {

            // Step 2a (simplest): Recolor, and move up to see if more work
            // needed
            if (isRed(siblingOf(parentOf(node)))) {
                setColor(parentOf(node), BLACK);
                setColor(siblingOf(parentOf(node)), BLACK);
                setColor(grandparentOf(node), RED);
                correctAfterAdd(grandparentOf(node));
            }

            // Step 2b: Restructure for a parent who is the left child of the
            // grandparent. This will require a single right rotation if n is
            // also
            // a left child, or a left-right rotation otherwise.
            else if (parentOf(node) == leftOf(grandparentOf(node))) {
                if (node == rightOf(parentOf(node))) {
                    leftRotate(node = parentOf(node));
                }
                setColor(parentOf(node), BLACK);
                setColor(grandparentOf(node), RED);
                rightRotate(grandparentOf(node));
            }

            // Step 2c: Restructure for a parent who is the right child of the
            // grandparent. This will require a single left rotation if n is
            // also
            // a right child, or a right-left rotation otherwise.
            else if (parentOf(node) == rightOf(grandparentOf(node))) {
                if (node == leftOf(parentOf(node))) {
                    rightRotate(node = parentOf(node));
                }
                setColor(parentOf(node), BLACK);
                setColor(grandparentOf(node), RED);
                leftRotate(grandparentOf(node));
            }
        }

        // шаг 3: раскрасить корень дерева черным
        setColor(root, BLACK);
    }


    /**
     * Classic algorithm for fixing up a tree after removing a node; the
     * parameter to this method is the node that was pulled up to where the
     * removed node was.
     */
    private void correctAfterRemove(RBTreeNode node) {
        while (node != root && isBlack(node)) {
            if (node == leftOf(parentOf(node))) {
                // Pulled up node is a left child
                RBTreeNode sibling = rightOf(parentOf(node));
                if (isRed(sibling)) {
                    setColor(sibling, BLACK);
                    setColor(parentOf(node), RED);
                    leftRotate(parentOf(node));
                    sibling = rightOf(parentOf(node));
                }
                if (isBlack(leftOf(sibling)) && isBlack(rightOf(sibling))) {
                    setColor(sibling, RED);
                    node = parentOf(node);
                } else {
                    if (isBlack(rightOf(sibling))) {
                        setColor(leftOf(sibling), BLACK);
                        setColor(sibling, RED);
                        rightRotate(sibling);
                        sibling = rightOf(parentOf(node));
                    }
                    setColor(sibling, colorOf(parentOf(node)));
                    setColor(parentOf(node), BLACK);
                    setColor(rightOf(sibling), BLACK);
                    leftRotate(parentOf(node));
                    node = root;
                }
            } else {
                // pulled up node is a right child
                RBTreeNode sibling = leftOf(parentOf(node));
                if (isRed(sibling)) {
                    setColor(sibling, BLACK);
                    setColor(parentOf(node), RED);
                    rightRotate(parentOf(node));
                    sibling = leftOf(parentOf(node));
                }
                if (isBlack(leftOf(sibling)) && isBlack(rightOf(sibling))) {
                    setColor(sibling, RED);
                    node = parentOf(node);
                } else {
                    if (isBlack(leftOf(sibling))) {
                        setColor(rightOf(sibling), BLACK);
                        setColor(sibling, RED);
                        leftRotate(sibling);
                        sibling = leftOf(parentOf(node));
                    }
                    setColor(sibling, colorOf(parentOf(node)));
                    setColor(parentOf(node), BLACK);
                    setColor(leftOf(sibling), BLACK);
                    rightRotate(parentOf(node));
                    node = root;
                }
            }
        }
        setColor(node, BLACK);
    }

    private void leftRotate(RBTreeNode node) {
        if (node.right == null) {
            return;
        }
        RBTreeNode right = node.right;
        setRight(node, right.left);
        if (node.parent == null) {
            setRoot(right);
        } else if (node.parent.left == node) {
            setLeft(node.parent, right);
        } else {
            setRight(node.parent, right);
        }
        setLeft(right, node);
    }

    private void rightRotate(RBTreeNode node) {
        if (node.left == null) {
            return;
        }
        RBTreeNode left = node.left;
        setLeft(node, left.right);
        if (node.parent == null) {
            setRoot(left);
        } else if (node.parent.left == node) {
            setLeft(node.parent, left);
        } else {
            setRight(node.parent, left);
        }
        setRight(left, node);
    }

    private void removeFromParent(RBTreeNode node) {
        if (node.parent != null) {
            if (node.parent.left == node) {
                node.parent.left = null;
            } else if (node.parent.right == node) {
                node.parent.right = null;
            }
            node.parent = null;
        }
    }


    // вспомогательные методы, сильно облегчающие жизнь
    // (в частности проверяют все на null)

    private boolean colorOf(RBTreeNode node) {
        return node == null ? BLACK : node.color;
    }

    private boolean isRed(RBTreeNode node) {
        return colorOf(node) == RED;
    }

    private boolean isBlack(RBTreeNode node) {
        return colorOf(node) == BLACK;
    }

    private void setColor(RBTreeNode node, boolean color) {
        if (node != null) {
            node.color = color;
        }
    }

    private void setLeft(RBTreeNode node, RBTreeNode left) {
        if (node != null) {
            node.left = left;
            if (left != null) {
                left.parent = node;
            }
        }
    }

    private void setRight(RBTreeNode node, RBTreeNode right) {
        if (node != null) {
            node.right = right;
            if (right != null) {
                right.parent = node;
            }
        }
    }

    private void setRoot(RBTreeNode node) {
        root = node;
        if (node != null) {
            node.parent = null;
        }
    }

    private RBTreeNode parentOf(RBTreeNode node) {
        return node == null ? null : node.parent;
    }

    private RBTreeNode grandparentOf(RBTreeNode node) {
        // return parentOf(parentOf(node));
        return (node == null || node.parent == null) ? null : node.parent.parent;
    }

    private RBTreeNode siblingOf(RBTreeNode node) {
        return (node == null || node.parent == null)
            ? null
            : (node == node.parent.left)
                ? node.parent.right : node.parent.left;
    }

    private RBTreeNode leftOf(RBTreeNode node) {
        return node == null ? null : node.left;
    }

    private RBTreeNode rightOf(RBTreeNode node) {
        return node == null ? null : node.right;
    }

    private int getHeight(RBTreeNode node) {
        return (node == null) ? -1 : Math.max(getHeight(node.left), getHeight(node.right)) + 1;
    }

    public int getHeight() {
        return getHeight(root);
    }
}
