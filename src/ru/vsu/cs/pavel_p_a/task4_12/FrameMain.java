package ru.vsu.cs.pavel_p_a.task4_12;

import ru.vsu.cs.util.ArrayUtils;
import ru.vsu.cs.util.JTableUtils;
import ru.vsu.cs.util.SwingUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Locale;

public class FrameMain extends JFrame {

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
        panelMain.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        labelCanvas = new JLabel();
        labelCanvas.setText("");
        panelMain.add(labelCanvas, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 4, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 50), null, null, 0, false));
        tableInput = new JTable();
        tableInput.setPreferredScrollableViewportSize(new Dimension(600, 100));
        scrollPane1.setViewportView(tableInput);
        buttonLoadFromFile = new JButton();
        buttonLoadFromFile.setText("Загрузить из файла");
        panel1.add(buttonLoadFromFile, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(378, 38), null, 0, false));
        buttonSaveToFile = new JButton();
        buttonSaveToFile.setText("Сохранить в файл");
        panel1.add(buttonSaveToFile, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelControlButtons = new JPanel();
        panelControlButtons.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panelControlButtons, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonPrevState = new JButton();
        buttonPrevState.setText("Предыдущий");
        panelControlButtons.add(buttonPrevState, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonPlayPause = new JButton();
        buttonPlayPause.setText("Пауза/Продолжить");
        panelControlButtons.add(buttonPlayPause, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonNextState = new JButton();
        buttonNextState.setText("Следующий");
        panelControlButtons.add(buttonNextState, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelCurrentFrameNumber = new JLabel();
        labelCurrentFrameNumber.setText("Стадия n из N");
        panelControlButtons.add(labelCurrentFrameNumber, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonSort = new JButton();
        buttonSort.setText("Отсортировать");
        panel1.add(buttonSort, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelMaxItemsQuantity = new JLabel();
        labelMaxItemsQuantity.setText("Максимальное число элементов: m");
        panel1.add(labelMaxItemsQuantity, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spinnerSpeed = new JSpinner();
        panel1.add(spinnerSpeed, new com.intellij.uiDesigner.core.GridConstraints(3, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelTime = new JLabel();
        labelTime.setText("Время между кадрами, мс:");
        panel1.add(labelTime, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelAvailableValues = new JLabel();
        labelAvailableValues.setText("Элементы должны быть положительными целыми числами");
        panel1.add(labelAvailableValues, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }

    private static class IllegalSortStateIndexException extends Exception {
        public IllegalSortStateIndexException() {
        }

        public IllegalSortStateIndexException(String message) {
            super(message);
        }
    }

    private static class TooManyItemsException extends Exception {
        public TooManyItemsException() {
        }

        public TooManyItemsException(String message) {
            super(message);
        }
    }

    private static class IllegalValue extends Exception {

        public IllegalValue() {
        }

        public IllegalValue(String message) {
            super(message);
        }
    }

    // константы-ограничители массива
    private static final int MAX_ITEMS_QUANTITY = 100;
    private static final int MAX_VALUE = 100;
    private static final int MIN_VALUE = 1;

    private static final int DEFAULT_CELL_SIZE = 40;

    // константы холста
    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 600;
    private static final Dimension canvasDimension = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);

    // константы таймера
    private static final int DEFAULT_DELAY = 100;
    private static final int MAX_DELAY = 1000;
    private static final int MIN_DELAY = 10;

    private JPanel panelMain;
    private JTable tableInput;
    private JButton buttonLoadFromFile;
    private JButton buttonSaveToFile;
    private JButton buttonPrevState;
    private JButton buttonPlayPause;
    private JButton buttonNextState;
    private JPanel panelControlButtons;
    private JButton buttonSort;
    private JLabel labelCanvas;
    private JLabel labelMaxItemsQuantity;
    private JLabel labelCurrentFrameNumber;
    private JSpinner spinnerSpeed;
    private JLabel labelTime;
    private JLabel labelAvailableValues;

    private JFileChooser fileChooserOpen;
    private JFileChooser fileChooserSave;

    private int currentState;
    private boolean isPause = false;
    private java.util.List<SortState> sortStateList;
    private Timer timer;

    public FrameMain() throws HeadlessException {
        Locale.setDefault(Locale.ROOT);
        this.setTitle("Task 4 - 12");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panelMain);
        panelControlButtons.setVisible(false);

        labelCanvas.setPreferredSize(canvasDimension);

        labelAvailableValues.setText(String.format("Элементы должны принадлежать [%d; %d]", MIN_VALUE, MAX_VALUE));
        labelMaxItemsQuantity.setText(String.format("Максимальное количество элементов: %d", MAX_ITEMS_QUANTITY));
        labelTime.setText(String.format("Время между кадрами, от %d до %d мс:", MIN_DELAY, MAX_DELAY));
        buttonPlayPause.setText(isPause ? "Продолжить" : "Пауза");

        spinnerSpeed.setModel(new SpinnerNumberModel(DEFAULT_DELAY, MIN_DELAY, MAX_DELAY, 1));
        this.pack();

        fileChooserOpen = initFileChooser();
        fileChooserSave = initFileChooserSave();

        JTableUtils.initJTableForArray(tableInput, DEFAULT_CELL_SIZE, false, true, false, true);
        int[] arr = {4, 7, 3, 2, 6, 1, 11};
        JTableUtils.writeArrayToJTable(tableInput, arr);

        buttonSort.addActionListener(e -> startShowing());
        buttonLoadFromFile.addActionListener(e -> loadFromFile());
        buttonSaveToFile.addActionListener(e -> saveToFile());
        buttonPrevState.addActionListener(e -> prev());
        buttonNextState.addActionListener(e -> next());
        buttonPlayPause.addActionListener(e -> togglePlayPause());

        timer = new Timer(DEFAULT_DELAY, et -> {
            if (!isPause) {
                changeCurrentState(1);
                try {
                    drawState(currentState);
                } catch (IllegalSortStateIndexException ex) {
                    SwingUtils.showErrorMessageBox(ex);
                }
            }

            if (currentState >= sortStateList.size() - 1) {
                pause();
            }
        });
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
                int[] arr = ArrayUtils.readIntArrayFromFile(fileChooserOpen.getSelectedFile().getPath());
                JTableUtils.writeArrayToJTable(tableInput, arr);
                startShowing();
            }
        } catch (Exception exception) {
            SwingUtils.showErrorMessageBox(exception);
        }
    }

    private void saveToFile() {
        try {
            if (fileChooserSave.showSaveDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooserSave.getSelectedFile();
                ArrayUtils.writeArrayToFile(file.getAbsolutePath(), JTableUtils.readIntArrayFromJTable(tableInput));
            }
        } catch (Exception exception) {
            SwingUtils.showErrorMessageBox(exception);
        }
    }

    private void sort() {
        try {
            int[] arr = JTableUtils.readIntArrayFromJTable(tableInput);
            checkItemsQuantity(arr);
            checkValues(arr);
            sortStateList = GnomeSort.sort(arr);
//            sortStateList = BubbleSort.sort(arr);

            panelControlButtons.setVisible(true);
            changeCurrentState(0);
        } catch (Exception exception) {
            SwingUtils.showErrorMessageBox(exception);
        }
    }

    private void drawState(int i) throws IllegalSortStateIndexException {
        checkSortStateIndex(i);

        BufferedImage sortStateFrame = new BufferedImage(FRAME_WIDTH, FRAME_HEIGHT, BufferedImage.TYPE_INT_BGR);
        Graphics2D g2D = sortStateFrame.createGraphics();
        g2D.setColor(Color.WHITE);
        g2D.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);

        g2D.setColor(Color.BLACK);

        SortState state = sortStateList.get(i);
        int[] array = state.getArray();
        int max = array[0];
        for (int element : array) {
            if (element > max) {
                max = element;
            }
        }

        int elementsQuantity = array.length;
        int columnWidth = FRAME_WIDTH / roundUpToTens(elementsQuantity),
                columnHeightCoefficient = FRAME_HEIGHT / roundUpToTens(max);

        for (int columnNumber = 0; columnNumber < elementsQuantity; columnNumber++) {
            int element = array[columnNumber];

            int columnHeight = element * columnHeightCoefficient;
            int x = columnWidth * columnNumber;
            int y = FRAME_HEIGHT - columnHeight;

            g2D.setColor(Color.BLACK);
            if (columnNumber == state.getI()) {
                g2D.setColor(Color.RED);
            } else if (columnNumber == state.getJ()) {
                g2D.setColor(Color.BLUE);
            }

            if (currentState == sortStateList.size() - 1) {
                g2D.setColor(Color.GREEN);
            }

            g2D.fillRect(x, y, columnWidth, columnHeight);

            g2D.setColor(Color.WHITE);
            g2D.setStroke(new BasicStroke(2));
            g2D.drawRect(x, y, columnWidth, columnHeight);
            g2D.setStroke(new BasicStroke(0));
        }

        labelCanvas.setIcon(new ImageIcon(sortStateFrame));
    }

    private int roundUpToTens(int value) {
        return (value % 10 == 0) ? value : value - value % 10 + 10;
    }

    private void startShowing() {
        sort();
        int delay = (Integer) spinnerSpeed.getValue();
        timer.setDelay(delay);
        timer.start();
        play();
    }

    private void pause() {
        buttonPlayPause.setText("Продолжить");
        isPause = true;
    }

    private void play() {
        buttonPlayPause.setText("Пауза");
        isPause = false;
    }

    private void togglePlayPause() {
        if (isPause) {
            play();
        } else {
            pause();
        }
    }

    private void prev() {
        try {
            pause();
            changeCurrentState(-1);
            drawState(currentState);
        } catch (IllegalSortStateIndexException e) {
            SwingUtils.showErrorMessageBox(e);
        }

    }

    private void next() {
        try {
            pause();
            changeCurrentState(1);
            drawState(currentState);
        } catch (IllegalSortStateIndexException e) {
            SwingUtils.showErrorMessageBox(e);
        }
    }


    /**
     * @param changeDirection если 0, то currentState = 0; если > 0, то currentState++; иначе currentState--;
     */
    private void changeCurrentState(int changeDirection) {
        if (changeDirection == 0) {
            currentState = 0;
        } else if (changeDirection > 0 && currentState < sortStateList.size() - 1) {
            currentState++;
        } else if (changeDirection < 0 && currentState > 0) {
            currentState--;
        }

        buttonNextState.setEnabled(currentState < sortStateList.size() - 1);
        buttonPrevState.setEnabled(currentState > 0);

        labelCurrentFrameNumber.setText(String.format("Стадия %d из %d", currentState + 1, sortStateList.size()));
    }

    private void checkSortStateIndex(int i) throws IllegalSortStateIndexException {
        if (i < 0 || sortStateList.size() <= i) {
            String message = String.format("Индекс %d не входит в границы [0, %d]", i, sortStateList.size());
            throw new IllegalSortStateIndexException(message);
        }
    }

    private void checkItemsQuantity(int[] arr) throws TooManyItemsException {
        int length = arr.length;
        if (length > MAX_ITEMS_QUANTITY) {
            String message = String.format("Слишком много элементов: %d", length);
            throw new TooManyItemsException(message);
        }
    }

    private void checkValues(int[] arr) throws IllegalValue {
        for (int i = 0; i < arr.length; i++) {
            int element = arr[i];
            if (!(MIN_VALUE <= element && element <= MAX_VALUE)) {
                throw new IllegalValue(String.format("Элемент c индексом %d не принадлежит [%d; %d].", i, MIN_VALUE, MAX_VALUE));
            }
        }
    }
}