package ru.vsu.cs.pavel_p_a.task5_31;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import ru.vsu.cs.util.SwingUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class FrameMain extends JFrame {
    private static final int MIN_CURRENT_PAIR_NUMBER = 0;

    private JPanel panelMain;
    private JSplitPane splitPaneMain;
    private JTextField textFieldBracketNotationTree;
    private JButton buttonMakeTree;
    private JButton buttonLoadBracketNotationTree;
    private JButton buttonSaveBracketNotationTree;
    private JCheckBox checkBoxTransparent;
    private JButton buttonSaveImage;
    private JPanel panelPaintArea;
    private JButton buttonGetAllEqualSubtrees;
    private JTextArea textAreaSystemOut;
    private JButton buttonPreviousPair;
    private JButton buttonNextPair;
    private JLabel labelCurrentPairNumber;
    private JPanel panelControlElements;

    private JPanel paintPanel = null;
    private BinaryTree<Integer> tree = new SimpleBinaryTree<>();
    private List<List<BinaryTree.TreeNode<Integer>>> allEqualSubTrees;
    private int currentPairNumber;
    private int maxPairNumber;

    private JFileChooser fileChooserOpen;
    private JFileChooser fileChooserSave;

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

    public FrameMain() {
        this.setTitle("task5_31");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panelMain);
        panelControlElements.setVisible(false);
        this.pack();

        // инициализация fileChooser
        fileChooserOpen = initFileChooser();
        fileChooserSave = initFileChooserSave();

        // инициализация разделителя
        splitPaneMain.setDividerLocation(0.5);
        splitPaneMain.setResizeWeight(1.0);
        splitPaneMain.setBorder(null);

        // инициализация панели рисования
        paintPanel = new JPanel() {
            private Dimension paintSize = new Dimension(0, 0);

            @Override
            public void paintComponent(Graphics gr) {
                super.paintComponent(gr);
                paintSize = BinaryTreePainter.paint(tree, gr);
                if (!paintSize.equals(this.getPreferredSize())) {
                    SwingUtils.setFixedSize(this, paintSize.width, paintSize.height);
                }
            }
        };
        JScrollPane paintJScrollPane = new JScrollPane(paintPanel);
        panelPaintArea.add(paintJScrollPane);

        buttonMakeTree.addActionListener(actionEvent -> {
            try {
                SimpleBinaryTree<Integer> tree = new SimpleBinaryTree<>(Integer::parseInt);
                tree.fromBracketNotation(textFieldBracketNotationTree.getText());
                this.tree = tree;
                repaintTree();
            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }
        });
        buttonLoadBracketNotationTree.addActionListener(e -> {
            loadFromFile();
        });
        buttonSaveBracketNotationTree.addActionListener(e -> {
            saveToFile();
        });
        buttonSaveImage.addActionListener(actionEvent -> {
            if (tree == null) {
                return;
            }
            try {
                if (fileChooserSave.showSaveDialog(FrameMain.this) == JFileChooser.APPROVE_OPTION) {
                    String filename = fileChooserSave.getSelectedFile().getPath();
                    if (!filename.toLowerCase().endsWith(".svg")) {
                        filename += ".svg";
                    }
                    BinaryTreePainter.saveIntoFile(tree, filename, checkBoxTransparent.isSelected());
                }
            } catch (Exception e) {
                SwingUtils.showErrorMessageBox(e);
            }
        });
        buttonGetAllEqualSubtrees.addActionListener(e -> {
            try {
                SimpleBinaryTree<Integer> tree = new SimpleBinaryTree<>(Integer::parseInt);
                tree.fromBracketNotation(textFieldBracketNotationTree.getText());
                this.tree = tree;
            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }
            allEqualSubTrees = BinaryTreeAlgorithms.findAllEqualSubtrees(this.tree);
            showSystemOut(() -> {
                System.out.println("Пары одинаковых поддеревьев:");
                int i = 1;
                for (var pair : allEqualSubTrees) {
                    System.out.printf("%d) %s; %s\n", i, pair.get(0).toBracketStr(), pair.get(1).toBracketStr());
                    i++;
                }
            });

            maxPairNumber = allEqualSubTrees.size();
            setCurrentPairNumberToZero();
            updateLabelCurrentPairNumberText();

            if (!panelControlElements.isVisible()) {
                panelControlElements.setVisible(true);
                this.pack();
            }

            List<BinaryTree.TreeNode<Integer>> pair = allEqualSubTrees.get(currentPairNumber);
            for (int i = 0; i < 2; i++) {
                pair.get(i).setColor(BinaryTree.TreeNode.ACCENT_COLOR);
            }

            repaintTree();
        });
        buttonPreviousPair.addActionListener(e -> {
            List<BinaryTree.TreeNode<Integer>> pair = allEqualSubTrees.get(currentPairNumber);
            for (int i = 0; i < 2; i++) {
                pair.get(i).setColor(BinaryTree.TreeNode.DEFAULT_COLOR);
            }
            decreaseCurrentPairNumber();
            updateLabelCurrentPairNumberText();
            pair = allEqualSubTrees.get(currentPairNumber);
            for (int i = 0; i < 2; i++) {
                pair.get(i).setColor(BinaryTree.TreeNode.ACCENT_COLOR);
            }
            repaintTree();
        });
        buttonNextPair.addActionListener(e -> {
            List<BinaryTree.TreeNode<Integer>> pair = allEqualSubTrees.get(currentPairNumber);
            for (int i = 0; i < 2; i++) {
                pair.get(i).setColor(BinaryTree.TreeNode.DEFAULT_COLOR);
            }
            increaseCurrentPairNumber();
            updateLabelCurrentPairNumberText();
            pair = allEqualSubTrees.get(currentPairNumber);
            for (int i = 0; i < 2; i++) {
                pair.get(i).setColor(BinaryTree.TreeNode.ACCENT_COLOR);
            }
            repaintTree();
        });
    }

    public void repaintTree() {
            paintPanel.repaint();
    }

    private void increaseCurrentPairNumber() {
        buttonNextPair.setEnabled(++currentPairNumber < maxPairNumber - 1);
        buttonPreviousPair.setEnabled(currentPairNumber > MIN_CURRENT_PAIR_NUMBER);
    }

    private void decreaseCurrentPairNumber() {
        buttonPreviousPair.setEnabled(--currentPairNumber > MIN_CURRENT_PAIR_NUMBER);
        buttonNextPair.setEnabled(currentPairNumber < maxPairNumber - 1);
    }

    private void setCurrentPairNumberToZero() {
        currentPairNumber = MIN_CURRENT_PAIR_NUMBER;
        buttonNextPair.setEnabled(currentPairNumber < maxPairNumber - 1);
        buttonPreviousPair.setEnabled(currentPairNumber > MIN_CURRENT_PAIR_NUMBER);
    }

    private void updateLabelCurrentPairNumberText() {
        labelCurrentPairNumber.setText("Текущая пара " + currentPairNumber);
    }

    private JFileChooser initFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        FileFilter filter = new FileNameExtensionFilter("Text", "txt");
        fileChooser.addChoosableFileFilter(filter);
        return fileChooser;
    }

    private JFileChooser initFileChooserSave() {
        JFileChooser fileChooser = initFileChooser();

        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setApproveButtonText("Save");
        return fileChooser;
    }

    private void loadFromFile() {
        try {
            if (fileChooserOpen.showOpenDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
                Scanner scanner = new Scanner(fileChooserOpen.getSelectedFile());
                String bracketNotation = scanner.nextLine();
                textFieldBracketNotationTree.setText(bracketNotation);
            }
        } catch (Exception exception) {
            SwingUtils.showErrorMessageBox(exception);
        }
    }

    private void saveToFile() {
        try {
            if (fileChooserSave.showSaveDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooserSave.getSelectedFile();
                PrintWriter out = new PrintWriter(file);

                String bracketNotation = textFieldBracketNotationTree.getText();
                out.println(bracketNotation);
            }
        } catch (Exception exception) {
            SwingUtils.showErrorMessageBox(exception);
        }
    }

    private void showSystemOut(Runnable action) {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(baos, true, "UTF-8"));

            action.run();

            textAreaSystemOut.setText(baos.toString("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            SwingUtils.showErrorMessageBox(e);
        }
        System.setOut(oldOut);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelMain = new JPanel();
        panelMain.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPaneMain = new JSplitPane();
        panelMain.add(splitPaneMain, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        splitPaneMain.setLeftComponent(panel1);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Дерево в скобочной нотации");
        panel2.add(label1, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldBracketNotationTree = new JTextField();
        panel2.add(textFieldBracketNotationTree, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        buttonMakeTree = new JButton();
        buttonMakeTree.setText("Построить дерево");
        panel2.add(buttonMakeTree, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonLoadBracketNotationTree = new JButton();
        buttonLoadBracketNotationTree.setText("Загрузить из файла");
        panel2.add(buttonLoadBracketNotationTree, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonSaveBracketNotationTree = new JButton();
        buttonSaveBracketNotationTree.setText("Сохранить в файл");
        panel2.add(buttonSaveBracketNotationTree, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        checkBoxTransparent = new JCheckBox();
        checkBoxTransparent.setText("прозрачность");
        panel3.add(checkBoxTransparent, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonSaveImage = new JButton();
        buttonSaveImage.setText("Сохранить изображение в SVG");
        panel3.add(buttonSaveImage, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        panelPaintArea = new JPanel();
        panelPaintArea.setLayout(new BorderLayout(0, 0));
        panel1.add(panelPaintArea, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panelPaintArea.add(spacer2, BorderLayout.CENTER);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPaneMain.setRightComponent(panel4);
        buttonGetAllEqualSubtrees = new JButton();
        buttonGetAllEqualSubtrees.setText("Найти все одинаковые поддеревья");
        panel4.add(buttonGetAllEqualSubtrees, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel4.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textAreaSystemOut = new JTextArea();
        scrollPane1.setViewportView(textAreaSystemOut);
        panelControlElements = new JPanel();
        panelControlElements.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panelControlElements, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonPreviousPair = new JButton();
        buttonPreviousPair.setText("Предыдущая пара");
        panelControlElements.add(buttonPreviousPair, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelCurrentPairNumber = new JLabel();
        labelCurrentPairNumber.setText("Label");
        panelControlElements.add(labelCurrentPairNumber, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonNextPair = new JButton();
        buttonNextPair.setText("Следующая пара");
        panelControlElements.add(buttonNextPair, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }

}
