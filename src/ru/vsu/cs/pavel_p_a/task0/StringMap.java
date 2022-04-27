package ru.vsu.cs.pavel_p_a.task0;

import java.util.Arrays;

/**
 * Класс для хранения уникального множества строк
 */
public class StringMap {

    private class KeyValue {
        public String key;
        public String value;

        public KeyValue(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * Первоначальный размер массива для хранения элементов
     */
    public static final int DEFAULT_START_SIZE = 16;

    /**
     * Массив для хранения элементов множества
     */
    private KeyValue[] entries;
    /**
     * Кол-во элементов множества
     */
    private int size;

    /**
     * Конструктор с параметром
     * @param startSize Первоначальный размер массива для хранения элементов
     */
    public StringMap(int startSize) {
        entries = new KeyValue[startSize];
        size = 0;
    }

    /**
     * Конструктор по умолчанию (без параметров)
     */
    public StringMap() {
        // вызов конструктора с параметром со значением по умолчанию
        this(DEFAULT_START_SIZE);
    }

    public boolean put(String key, String value) throws Exception {
        if (key == null) {
            throw new Exception("Key is null");
        }

        int index  = getIndexOfKey(key);
        if (index >= 0) {
            entries[index].value = value;
            return true;
        }

        if (size >= entries.length) {
            // если закончилось место, пересоздаем массив data размером в 2 раза больше
            entries = Arrays.copyOf(entries, size * 2);
        }

        entries[size] = new KeyValue(key, value);
        size++;
        return true;
    }

    /**
     * Очистка множества
     * @return true - если множество было не пустое
     */
    public boolean clear() {
        boolean result = size > 0;
        for (int i = 0; i < size; i++) {
            entries[i] = null;
        }
        size = 0;
        return result;
    }

    /**
     * Поиск позиции (индекса) элемента в массиве
     * @param key Искомая строка
     * @return индекс элемента в массиве data
     */
    private int getIndexOfKey(String key) {
        for (int i = 0; i < size; i++) {
            if (entries[i].key.equals(key)) {
                return i;
            }
        }
        return -1;
    }

    private int getIndexOfValue(String value) {
        for (int i = 0; i < size; i++) {
            if (entries[i].value.equals(value)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Проверка наличия строки в множестве
     * @param str Проверяемая строка
     * @return true - если строка присутствует
     */
    public boolean containsKey(String str) {
        if (str == null) {
            return false;
        }
        return getIndexOfKey(str) >= 0;
    }

    public boolean containsValue(String value) {
        return getIndexOfValue(value) >= 0;
    }

    /**
     * Проверка на пустоту
     * @return true - если множество пустое
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Удаление строки из множества
     * @param key Удалаемая строка
     * @return true - если строка удалена, false - если строки не было
     */
    public boolean remove(String key) {
        if (key == null) {
            return false;
        }
        int index = getIndexOfKey(key);
        if (index < 0) {
            return false;
        }

        entries[index] = null;
        if (index < size - 1) {
            entries[index] = entries[size - 1];
            entries[size - 1] = null;
        }
        size--;
        return true;
    }

    /**
     * Размер множества
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * Элементы множества в виде массива
     * @return
     */
    public String[]	getKeys() {
        String[] keys = new String[size];
        for (int i = 0; i < size; i++) {
            keys[i] = entries[i].key;
        }
        return keys;
    }

    public String get(String key) throws Exception {
        int index = getIndexOfKey(key);

        if (index < 0) {
            throw new Exception("Ключ не найден");
        }

        return entries[index].value;
    }
}

