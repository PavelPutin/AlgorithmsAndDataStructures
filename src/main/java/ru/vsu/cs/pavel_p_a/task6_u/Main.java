package ru.vsu.cs.pavel_p_a.task6_u;

import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        Map<String, Integer> m = new InOrderMap<>();
        m.put("P", 1);
        m.put("a", 2);
        m.put("13", 3);
        m.remove("a");
        m.put("B", 4);
        m.put("A", 5);
        m.put("a", 6);
        for (var entry : m.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println();
        for (var value : m.values()) {
            System.out.println(value);
        }
        System.out.println();
        for (var entry : m.keySet()) {
            System.out.println(entry);
        }
        System.out.println();
    }
}
