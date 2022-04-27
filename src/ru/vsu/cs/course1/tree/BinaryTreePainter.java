package ru.vsu.cs.course1.tree;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUtils;
import ru.vsu.cs.util.DrawUtils;

/**
 * Класс, выполняющий отрисовку дерева на Graphics
 */
public class BinaryTreePainter {

    public static final int TREE_NODE_WIDTH = 70,
            TREE_NODE_HEIGHT = 30,
            HORIZONTAL_INDENT = 10,
            VERTICAL_INDENT = 50;

    public static final Font FONT = new Font("Microsoft Sans Serif", Font.PLAIN, 20);

    private static class NodeDrawResult {

        public int center;
        public int maxX;
        public int maxY;

        public NodeDrawResult(int center, int maxX, int maxY) {
            this.center = center;
            this.maxX = maxX;
            this.maxY = maxY;
        }
    }

    private static <T extends Comparable<T>> NodeDrawResult paint(BinaryTree.TreeNode<T> node, Graphics2D g2d,
                                                                  int x, int y) {
        if (node == null) {
            return null;
        }

        NodeDrawResult leftResult = paint(node.getLeft(), g2d, x, y + (TREE_NODE_HEIGHT + VERTICAL_INDENT));
        int rightX = (leftResult != null) ? leftResult.maxX : x + (TREE_NODE_WIDTH + HORIZONTAL_INDENT) / 2;
        NodeDrawResult rightResult = paint(node.getRight(), g2d, rightX, y + (TREE_NODE_HEIGHT + VERTICAL_INDENT));
        int thisX;
        if (leftResult == null) {
            thisX = x;
        } else if (rightResult == null) {
            thisX = Math.max(x + (TREE_NODE_WIDTH + HORIZONTAL_INDENT) / 2, leftResult.center + HORIZONTAL_INDENT / 2);
        } else {
            thisX = (leftResult.center + rightResult.center) / 2 - TREE_NODE_WIDTH / 2;
        }

        Color color = node.getColor() == Color.BLACK ? Color.WHITE : node.getColor();
        g2d.setColor(color);
        g2d.fillRect(thisX, y, TREE_NODE_WIDTH, TREE_NODE_HEIGHT);
        g2d.setColor(Color.BLACK);
        if (leftResult != null) {
            g2d.drawLine(thisX + TREE_NODE_WIDTH / 2, y + TREE_NODE_HEIGHT, leftResult.center, y + TREE_NODE_HEIGHT + VERTICAL_INDENT);
        }
        if (rightResult != null) {
            g2d.drawLine(thisX + TREE_NODE_WIDTH / 2, y + TREE_NODE_HEIGHT, rightResult.center, y + TREE_NODE_HEIGHT + VERTICAL_INDENT);
        }
        g2d.drawRect(thisX, y, TREE_NODE_WIDTH, TREE_NODE_HEIGHT);
        g2d.setColor(DrawUtils.getContrastColor(color));
        DrawUtils.drawStringInCenter(g2d, FONT, node.getValue().toString(), thisX, y, TREE_NODE_WIDTH, TREE_NODE_HEIGHT);

        int maxX = Math.max((leftResult == null) ? 0 : leftResult.maxX, (rightResult == null) ? 0 : rightResult.maxX);
        int maxY = Math.max((leftResult == null) ? 0 : leftResult.maxY, (rightResult == null) ? 0 : rightResult.maxY);
        return new NodeDrawResult(
                thisX + TREE_NODE_WIDTH / 2,
                Math.max(thisX + TREE_NODE_WIDTH + HORIZONTAL_INDENT, maxX),
                Math.max(y + TREE_NODE_HEIGHT, maxY)
        );
    }

    /**
     * Рисование дерева
     *
     * @param <T> Тип элементов в дереве
     * @param tree Дерево
     * @param gr   Graphics
     * @return Размеры картинки
     */
    public static <T extends Comparable<T>> Dimension paint(BinaryTree<T> tree, Graphics gr) {
        Graphics2D g2d = (Graphics2D) gr;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        NodeDrawResult rootResult = paint(tree.getRoot(), g2d, HORIZONTAL_INDENT, HORIZONTAL_INDENT);
        return new Dimension((rootResult == null) ? 0 : rootResult.maxX, (rootResult == null) ? 0 : rootResult.maxY + HORIZONTAL_INDENT);
    }

    /**
     * Сохранение изображения дерева в SVG-файл
     *
     * @param tree Двоичное дерево
     * @param filename Имя файла
     * @param backgroundTransparent Оставлять ли прозрачным фон
     * @throws IOException Возможное исключение
     */
    public static <T extends Comparable<T>> void saveIntoFile(BinaryTree<T> tree, String filename, boolean backgroundTransparent)
            throws IOException {
        // первый раз рисуем, только чтобы размеры изображения определить
        SVGGraphics2D g2 = new SVGGraphics2D(1, 1);
        Dimension size = BinaryTreePainter.paint(tree, g2);
        // второй раз рисуем непосредственно для сохранения
        g2 = new SVGGraphics2D(size.width, size.height);
        if (!backgroundTransparent) {
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, size.width, size.height);
        }
        BinaryTreePainter.paint(tree, g2);

        SVGUtils.writeToSVG(new File(filename), g2.getSVGElement());
    }

    /**
     * Сохранение изображения дерева в SVG-файл (с белым фоном)
     *
     * @param tree Двоичное дерево
     * @param filename Имя файла
     * @throws IOException Возможное исключение
     */
    public static <T extends Comparable<T>> void saveIntoFile(BinaryTree<T> tree, String filename)
            throws IOException {
        saveIntoFile(tree, filename, false);
    }
}
