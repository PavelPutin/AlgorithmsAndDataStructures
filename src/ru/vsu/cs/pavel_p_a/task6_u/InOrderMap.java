package ru.vsu.cs.pavel_p_a.task6_u;

import ru.vsu.cs.course1.tree.bst.BSTree;
import ru.vsu.cs.course1.tree.bst.BSTreeMap;
import ru.vsu.cs.course1.tree.bst.avl.AVLTree;
import ru.vsu.cs.course1.tree.bst.avl.AVLTreeMap;
import ru.vsu.cs.util.dummy.DefaultNotSupportedCollection;
import ru.vsu.cs.util.dummy.DefaultNotSupportedSet;

import java.util.*;

public class InOrderMap<K extends Comparable<? super K>, V> implements BSTreeMap<K, V> {

    private final BSTree<MapTreeEntry<K, V>> tree = new AVLTree<>();
    private BSTreeMap<Integer, MapTreeEntry<K, V>> orderEntry= new AVLTreeMap<>();
    private BSTreeMap<MapTreeEntry<K, V>, Integer> entryOrder= new AVLTreeMap<>();
    private Integer totalEntriesNumber = 0;

    @Override
    public BSTree<MapTreeEntry<K, V>> getTree() {
        return tree;
    }

    @Override
    public V put(K key, V value) {
        MapTreeEntry<K, V> entry = new MapTreeEntry(key, value);
        orderEntry.put(totalEntriesNumber, entry);
        entryOrder.put(entry, totalEntriesNumber);
        totalEntriesNumber += 1;
        entry = getTree().put(entry);
        return entry == null ? null : entry.value;
    }

    @Override
    public V remove(Object key) {
        MapTreeEntry<K, V> entry = getTree().remove(new MapTreeEntry((K) key, null));
        if (entry == null) {
            return null;
        }
        Integer order = entryOrder.get(entry);
        entryOrder.remove(entry);
        orderEntry.remove(order);
        return entry.value;
    }
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.entrySet().forEach((entry) -> {
            MapTreeEntry<K, V> newEntry = new MapTreeEntry(entry.getKey(), entry.getValue());
            getTree().put(newEntry);
            orderEntry.put(totalEntriesNumber, newEntry );
            entryOrder.put(newEntry , totalEntriesNumber);
            totalEntriesNumber += 1;
        });
    }

    @Override
    public void clear() {
        getTree().clear();
        orderEntry.clear();
        entryOrder.clear();
        totalEntriesNumber = 0;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new DefaultNotSupportedSet<Entry<K, V>>() {
            Iterator<MapTreeEntry<Integer, MapTreeEntry<K, V>>> entryIterator = orderEntry.getTree().iterator();

            @Override
            public int size() {
                return InOrderMap.this.size();
            }

            @Override
            public Iterator<Entry<K, V>> iterator() {
                return new Iterator<Entry<K, V>>() {
                    @Override
                    public boolean hasNext() {
                        return entryIterator.hasNext();
                    }

                    @Override
                    public Entry<K, V> next() {
                        return entryIterator.next().getValue();
                    }
                };
            }
        };
    }



}
