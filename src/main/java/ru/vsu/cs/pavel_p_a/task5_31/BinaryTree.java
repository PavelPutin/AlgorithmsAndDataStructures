package ru.vsu.cs.pavel_p_a.task5_31;

import java.awt.*;
import java.util.Iterator;

public interface BinaryTree<T> extends Iterable<T> {

    interface TreeNode<T> extends Iterable<T> {
        Color ACCENT_COLOR = Color.RED;
        Color DEFAULT_COLOR = Color.BLACK;

        T getValue();

        default TreeNode<T> getLeft() {
            return null;
        }

        default TreeNode<T> getRight() {
            return null;
        }

        default Color getColor() {
            return DEFAULT_COLOR;
        }

        default void setColor(Color color) {return;};

        @Override
        default Iterator<T> iterator() {
            return BinaryTreeAlgorithms.inOrderValues(this).iterator();
        }

        default String toBracketStr() {
            return BinaryTreeAlgorithms.toBracketStr(this);
        }
    }

    TreeNode<T> getRoot();

    @Override
    default Iterator<T> iterator() {
        return this.getRoot().iterator();
    }

    default String toBracketStr() {
        return this.getRoot().toBracketStr();
    }
}
