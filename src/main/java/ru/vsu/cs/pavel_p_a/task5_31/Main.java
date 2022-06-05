package ru.vsu.cs.pavel_p_a.task5_31;

import java.awt.*;
import java.util.function.Function;

import static ru.vsu.cs.pavel_p_a.task5_31.BinaryTreeAlgorithms.findAllEqualSubtrees;

public class Main {
    public static void main(String[] args) throws Exception {
        /*String str = "a(d(c(c(c;c)));c(d(c(c(c;c)))))";
        Function<String, String> readString = s -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                if (Character.isAlphabetic(s.charAt(i)) || Character.isDigit(s.charAt(i))) {
                    sb.append(s.charAt(i));
                } else {
                    break;
                }
            }
            return sb.toString();
        };
        SimpleBinaryTree<String> bt = new SimpleBinaryTree<>(readString);

        bt.fromBracketNotation(str);
        var result = findAllEqualSubtrees(bt);
        for (var pair : result) {
            System.out.printf("%s; %s\n", pair.get(0).toBracketStr(), pair.get(1).toBracketStr());
        }*/
        java.awt.EventQueue.invokeLater(() -> new FrameMain().setVisible(true));
    }
}
