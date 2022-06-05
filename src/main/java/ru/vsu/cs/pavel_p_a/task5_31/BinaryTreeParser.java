/*
package ru.vsu.cs.pavel_p_a.task5u;

public class BinaryTreeParser {

    */
/*
     * tree -> tree ("(" (node (";" node)? | ";" node) ")")? // old variant
     * tree -> tree("(" (^";"^")"node)? (";"node)? ")")? // new variant
     *//*


    private final String str;
    private int index;

    public BinaryTreeParser(String str) {
        this.str = str.replaceAll("\\s", "");
        this.index = 0;
    }

    private SimpleBinaryTree<String> tree() {
        SimpleBinaryTree<String> result;
        StringBuilder value = new StringBuilder();
        while (isLiteral(str.charAt(index))) {
            value.append(str.charAt(index++));
        }
        result = new SimpleBinaryTree<>(value.toString());

        if (str.charAt(index) == '(') {
            index++; // пропускаем открывающую скобку

            if (str.charAt(index) != ';' && str.charAt(index) != ')') {
                result.setLeft(tree());
            }
            if (str.charAt(index) == ';') {
                index++;
                result.setRight(tree());
            }

            index++; // пропускаем закрывающую скобку
        }

        return result;
    }

    public SimpleBinaryTree<String> parse() {
        return tree();
    }

    private boolean isLiteral(char input) {
        return Character.isAlphabetic(input) || Character.isDigit(input);
    }
}
*/
