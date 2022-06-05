package ru.vsu.cs.pavel_p_a.task6_u;

import ru.vsu.cs.course1.tree.bst.BSTreeMap;
import ru.vsu.cs.util.dummy.DefaultNotSupportedCollection;
import ru.vsu.cs.util.dummy.DefaultNotSupportedMap;
import ru.vsu.cs.util.dummy.DefaultNotSupportedSet;

import java.util.*;

public class InOrderMap<K extends Comparable<? super K>, V> implements DefaultNotSupportedMap<K, V> {

    private class Pair {
        public V value;
        public int order;

        Pair(V value, int order) {
            this.value = value;
            this.order = order;
        }
    }
    private Map<K, Pair> map = new TreeMap<>();
    private Map<Integer, K> orderEntry = new TreeMap<>();
    private Integer totalEntriesNumber = 0;

    @Override
    public V put(K key, V value) {
        Pair prevValue = map.put(key, new Pair(value, totalEntriesNumber));
        orderEntry.put(totalEntriesNumber, key);
        totalEntriesNumber += 1;
        return prevValue == null ? null : prevValue.value;
    }

    @Override
    public V remove(Object key) {
        Pair pair = map.remove(key);
        int order = pair.order;
        orderEntry.remove(order);
        return pair.value;
    }
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.entrySet().forEach((entry) -> {
            map.put(entry.getKey(), new Pair(entry.getValue(), totalEntriesNumber));
            orderEntry.put(totalEntriesNumber, entry.getKey());
            totalEntriesNumber += 1;
        });
    }

    @Override
    public void clear() {
        map.clear();
        orderEntry.clear();
        totalEntriesNumber = 0;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new DefaultNotSupportedSet<Entry<K, V>>() {

            @Override
            public int size() {
                return InOrderMap.this.size();
            }

            @Override
            public Iterator<Entry<K, V>> iterator() {

                return new Iterator<Entry<K, V>>() {
                    Iterator<Map.Entry<Integer, K>> orderIterator = orderEntry.entrySet().iterator();
                    @Override
                    public boolean hasNext() {
                        return orderIterator.hasNext();
                    }

                    @Override
                    public Entry<K, V> next() {
                        K key = orderIterator.next().getValue();
                        Pair pair = map.get(key);
                        Entry<K, V> entry = new Entry<K, V>() {

                            @Override
                            public K getKey() {
                                return key;
                            }

                            @Override
                            public V getValue() {
                                return pair.value;
                            }

                            @Override
                            public V setValue(V value) {
                                V oldValue = pair.value;
                                pair.value = value;
                                return oldValue;
                            }
                        };
                        return entry;
                    }
                };
            }
        };
    }


    @Override
    public Set<K> keySet() {
        return new DefaultNotSupportedSet<K>() {
            Iterator<Map.Entry<K, V>> entryIterator = entrySet().iterator();

            @Override
            public int size() {
                return InOrderMap.this.size();
            }

            @Override
            public Iterator<K> iterator() {
                return new Iterator<K>() {
                    @Override
                    public boolean hasNext() {
                        return entryIterator.hasNext();
                    }

                    @Override
                    public K next() {
                        return entryIterator.next().getKey();
                    }

                };
            }

            // надо будет потом реализовать остальные методы
        };
    }

    @Override
    public Collection<V> values() {
        return new DefaultNotSupportedCollection<V>() {
            Iterator<Map.Entry<K, V>> entryIterator = entrySet().iterator();

            @Override
            public int size() {
                return InOrderMap.this.size();
            }

            @Override
            public Iterator<V> iterator() {
                return new Iterator<V>() {
                    @Override
                    public boolean hasNext() {
                        return entryIterator.hasNext();
                    }

                    @Override
                    public V next() {
                        return entryIterator.next().getValue();
                    }

                };
            }

            // надо будет потом реализовать остальные методы
        };
    }
}
