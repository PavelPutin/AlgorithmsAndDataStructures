package ru.vsu.cs.pavel_p_a.task0;

import java.util.Locale;


public class Program {
    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.ROOT);

        String[] keys = {
            "aa", "bb", "cc", "d", "bb"
        };
        String[] values = {
            "aa", "bb", "ccc", "dd", "aa"
        };

        StringMap map1  = new StringMap();
        for (int i = 0; i < Math.min(keys.length, values.length); i++) {
            map1.put(keys[i], values[i]);
        }

        printMap(map1);

        printContainsKey(map1, "cc");
        map1.remove("cc");
        printContainsKey(map1, "cc");

        printMap(map1);

        map1.put("Pavel", "Putin");
        map1.put("ФКН", "Факультет компьютерных наук");


        printMap(map1);

        map1.clear();

        printMap(map1);

        keys = new String[]{
                "Десятириков0",
                "Малыхин0",
                "Фадеев0",
                "Деревянко0",
                "Десятириков1",
                "Малыхин1",
                "Фадеев1",
                "Деревянко1",
                "Десятириков2",
                "Малыхин2",
                "Фадеев2",
                "Деревянко2",
                "Десятириков3",
                "Малыхин4",
                "Фадеев5",
                "Деревянко6",
                "Десятириков01",
                "Малыхин01",
                "Фадеев01",
                "Деревянко01",
                "Десятириков11",
                "Малыхин11",
                "Фадеев11",
                "Деревянко11",
                "Десятириков21",
                "Малыхин21",
                "Фадеев21",
                "Деревянко21",
                "Десятириков31",
                "Малыхин41",
                "Фадеев51",
                "Деревянко61",
                "Клышникова"
        };

        values = new String[] {
                "5.1", "5.2",
                "5.1", "5.2",
                "5.1", "5.2",
                "5.1", "5.2",
                "5.1", "5.2",
                "5.1", "5.2",
                "5.1", "5.2",
                "5.1", "5.2",
                "5.1", "5.2",
                "5.1", "5.2",
                "5.1", "5.2",
                "5.1", "5.2",
                "5.1", "5.2",
                "5.1", "5.2",
                "5.1", "5.2",
                "5.1", "5.2",
                "5.1"
        };

        for (int i = 0; i < Math.min(keys.length, values.length); i++) {
            map1.put(keys[i], values[i]);
        }

        printMap(map1);
    }

    public static void printMap(StringMap map) throws Exception {
        System.out.printf("Map size: %d\n", map.size());
        for (String key : map.getKeys()) {
            System.out.printf("%s: %s\n", key, map.get(key));
        }
        if (map.isEmpty()) {
            System.out.println("<empty>");
        } else {
            System.out.println("\n");
        }
    }

    public static void printContainsKey(StringMap map, String key) {
        System.out.printf("Содержится ли ключ %s: %b\n", key, map.containsKey(key));
    }
}
