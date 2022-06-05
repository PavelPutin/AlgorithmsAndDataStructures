package ru.vsu.cs.pavel_p_a.task5_31;

import java.awt.*;
import java.util.*;
import java.util.function.Function;

public class SimpleBinaryTree<T> implements BinaryTree<T> {

    protected class SimpleTreeNode implements BinaryTree.TreeNode<T> {
        public T value;
        public SimpleTreeNode left;
        public SimpleTreeNode right;
        public Color color = TreeNode.DEFAULT_COLOR;

        public SimpleTreeNode(T value, SimpleTreeNode left, SimpleTreeNode right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }

        public SimpleTreeNode(T value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public TreeNode<T> getLeft() {
                return left;
        }

        @Override
        public TreeNode<T> getRight() {
            return right;
        }

        @Override
        public Color getColor() {
            return color;
        }

        @Override
        public void setColor(Color color) {
            this.color = color;
        }

        @Override
        public Iterator<T> iterator() {
            return BinaryTreeAlgorithms.inOrderValues(this).iterator();
        }
    }

    protected SimpleTreeNode root = null;

    protected Function<String, T> fromStrFunc;
    protected Function<T, String> toStrFunc;

    public SimpleBinaryTree(Function<String, T> fromStrFunc, Function<T, String> toStrFunc) {
        this.fromStrFunc = fromStrFunc;
        this.toStrFunc = toStrFunc;
    }

    public SimpleBinaryTree(Function<String, T> fromStrFunc) {
        this(fromStrFunc, Object::toString);
    }

    public SimpleBinaryTree() {
        this(null);
    }

    @Override
    public TreeNode<T> getRoot() {
        return root;
    }

    public void clear() {
        root = null;
    }

    private T fromStr(String s) throws Exception {
        s = s.trim();
        if (s.length() > 0 && s.charAt(0) == '"') {
            s = s.substring(1);
        }
        if (s.length() > 0 && s.charAt(s.length() - 1) == '"') {
            s = s.substring(0, s.length() - 1);
        }

        if (fromStrFunc == null) {
            throw new Exception("Не определена функция конвертации строки в T");
        }
        return fromStrFunc.apply(s);
    }

    private static class IndexWrapper {
        public int index = 0;
    }

    private void skipSpaces(String bracketStr, IndexWrapper iw) {
        while (iw.index < bracketStr.length() && Character.isWhitespace(bracketStr.charAt(iw.index))) {
            iw.index++;
        }
    }

    private T readValue(String bracketStr, IndexWrapper iw) throws Exception {
        skipSpaces(bracketStr, iw);
        if (iw.index >= bracketStr.length()) {
            return null;
        }

        int from = iw.index;
        boolean quote = bracketStr.charAt(iw.index) == '"';
        if (quote) {
            iw.index++;
        }

        while (iw.index < bracketStr.length() && (
                quote && bracketStr.charAt(iw.index) != '"' ||
                        !quote && !Character.isWhitespace(bracketStr.charAt(iw.index)) && "();".indexOf(bracketStr.charAt(iw.index)) < 0
        )) {
            iw.index++;
        }
        if (quote && bracketStr.charAt(iw.index) == '"') {
            iw.index++;
        }

        String valueStr = bracketStr.substring(from, iw.index);
        T value = fromStr(valueStr);
        skipSpaces(bracketStr, iw);
        return value;
    }

    private SimpleTreeNode fromBracketStr(String bracketStr, IndexWrapper iw) throws Exception {
        T parentValue = readValue(bracketStr, iw);
        SimpleTreeNode parentNode = new SimpleTreeNode(parentValue);

        // TODO: добавлена проверка на конец строки
        if (iw.index < bracketStr.length() && bracketStr.charAt(iw.index) == '(') {
            iw.index++;
            skipSpaces(bracketStr, iw);

            if (bracketStr.charAt(iw.index) != ';') {
                parentNode.left = fromBracketStr(bracketStr, iw);
                skipSpaces(bracketStr, iw);
            }

            if (bracketStr.charAt(iw.index) == ';') {
                iw.index++;
                skipSpaces(bracketStr, iw);
            }

            if (bracketStr.charAt(iw.index) != ')') {
                parentNode.right = fromBracketStr(bracketStr, iw);
                skipSpaces(bracketStr, iw);
            }

            if (bracketStr.charAt(iw.index) != ')') {
                throw new Exception(String.format("Ожидалось ')' [%d]", iw.index));
            }
            iw.index++;
        }

        return parentNode;
    }

    public void fromBracketNotation(String bracketStr) throws Exception {
        IndexWrapper iw = new IndexWrapper();
        SimpleTreeNode root = fromBracketStr(bracketStr, iw);
        if (iw.index < bracketStr.length()) {
            throw new Exception(String.format("Ожидался конец строки [%d]", iw.index));
        }
        this.root = root;
    }
}

/*private T value;
    private BinaryTree<T> left = null;
    private BinaryTree<T> right = null;

    public BinaryTree(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public BinaryTree getLeft() {
        return left;
    }

    public void setLeft(BinaryTree left) {
        this.left = left;
    }

    public BinaryTree getRight() {
        return right;
    }

    public void setRight(BinaryTree right) {
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryTree<?> that = (BinaryTree<?>) o;
        return Objects.equals(value, that.value) && Objects.equals(left, that.left) && Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, left, right);
    }*/