package ru.vsu.cs.course1.tree.bst.avl;

import ru.vsu.cs.course1.tree.bst.BSTreeMap;
import ru.vsu.cs.course1.tree.bst.BSTree;

/**
 * Реализация словаря на базе АВЛ-деревьев
 *
 * @param <K>
 * @param <V>
 */
public class AVLTreeMap<K extends Comparable<K>, V> implements BSTreeMap<K, V> {

    private final BSTree<MapTreeEntry<K, V>> tree = new AVLTree<>();

    @Override
    public BSTree<MapTreeEntry<K, V>> getTree() {
        return tree;
    }
}
