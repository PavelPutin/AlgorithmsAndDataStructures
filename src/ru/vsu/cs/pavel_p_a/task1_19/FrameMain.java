package ru.vsu.cs.pavel_p_a.task1_19;

import ru.vsu.cs.util.ArrayUtils;
import ru.vsu.cs.util.JTableUtils;
import ru.vsu.cs.util.SwingUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.ParseException;
import java.util.Locale;

public class FrameMain extends JFrame {

    private static final int DEFAULT_CELL_SIZE = 40;

    private static final int CANVAS_WIDTH = 500;
    private static final int CANVAS_HEIGHT = 500;
    private static final Dimension canvasDimension = new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT);

    private static final double DEFAULT_SCALE = 1.0;
    private static final double SCALE_STEP = 0.1;
    private static final int DEFAULT_TRANSLATE = 0;

    private static final String DEFAULT_TRANSLATE_VALUE = "0";
    private static final String DEFAULT_SCALE_VALUE = "1";

    private JPanel panelMain;
    private JLabel canvas;
    private JPanel panelControl;
    private JScrollPane scrollPanePoints;
    private JTable tablePoints;
    private JButton buttonDrawPolygon;
    private JPanel panelInfo;
    private JTextField textFieldArea;
    private JTextField textFieldPerimeter;
    private JCheckBox checkBoxDrawRectangle;
    private JButton buttonLoadFromFile;
    private JPanel panelScale;
    private JSpinner spinnerScaleX;
    private JSpinner spinnerScaleY;
    private JRadioButton radioButtonTopLeft;
    private JRadioButton radioButtonTopCenter;
    private JRadioButton radioButtonTopRight;
    private JRadioButton radioButtonCenterLeft;
    private JRadioButton radioButtonBottomLeft;
    private JRadioButton radioButtonCenterCenter;
    private JRadioButton radioButtonCenterRight;
    private JRadioButton radioButtonBottomCenter;
    private JRadioButton radioButtonBottomRight;
    private JCheckBox checkBoxScale;
    private JPanel panelTranslate;
    private JSpinner spinnerTranslateX;
    private JSpinner spinnerTranslateY;
    private JTextField textFieldTranslateXInfo;
    private JTextField textFieldScaleXInfo;
    private JTextField textFieldTranslateYInfo;
    private JTextField textFieldScaleYInfo;
    private JButton buttonAcceptChanges;
    private JCheckBox checkBoxFlipHorizontally;
    private JCheckBox checkBoxFlipVertically;
    private JButton buttonSave;
    private JButton buttonSaveAsSvg;

    private JFileChooser fileChooserOpen;
    private JFileChooser fileChooserSave;

    private BufferedImage img;
    private Point[] points;
    private Polygon polygon;

    public FrameMain() {
        Locale.setDefault(Locale.ROOT);
        this.setTitle("Task 1 - 19");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panelMain);
        this.pack();

        setTextFieldChangesInfoDefaultValue();

        canvas.setPreferredSize(canvasDimension);

        spinnerScaleX.setModel(getSpinnerScaleModel());
        spinnerScaleY.setModel(getSpinnerScaleModel());

        JTableUtils.initJTableForArray(tablePoints, DEFAULT_CELL_SIZE, true, false, true, false);
        JTableUtils.resizeJTable(tablePoints, 1, 2);

        fileChooserOpen = new JFileChooser();
        fileChooserSave = new JFileChooser();
        fileChooserOpen.setCurrentDirectory(new File("."));
        fileChooserSave.setCurrentDirectory(new File("."));
        FileFilter filter = new FileNameExtensionFilter("Polygon files", "poly");
        FileFilter filterSvg = new FileNameExtensionFilter("SVG", "svg");
        fileChooserOpen.addChoosableFileFilter(filter);
        fileChooserSave.addChoosableFileFilter(filter);
        fileChooserSave.addChoosableFileFilter(filterSvg);

        fileChooserSave.setAcceptAllFileFilterUsed(false);
        fileChooserSave.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooserSave.setApproveButtonText("Save");

        ButtonGroup transformOriginRadioButtons = new ButtonGroup();

        transformOriginRadioButtons.add(radioButtonTopLeft);
        radioButtonTopLeft.addActionListener(e -> polygon.setTransformOrigin(TransformOrigin.TOP, TransformOrigin.LEFT));

        transformOriginRadioButtons.add(radioButtonTopCenter);
        radioButtonTopCenter.addActionListener(e -> polygon.setTransformOrigin(TransformOrigin.TOP, TransformOrigin.CENTER));

        transformOriginRadioButtons.add(radioButtonTopRight);
        radioButtonTopRight.addActionListener(e -> polygon.setTransformOrigin(TransformOrigin.TOP, TransformOrigin.RIGHT));

        transformOriginRadioButtons.add(radioButtonCenterLeft);
        radioButtonCenterLeft.addActionListener(e -> polygon.setTransformOrigin(TransformOrigin.CENTER, TransformOrigin.LEFT));

        transformOriginRadioButtons.add(radioButtonCenterCenter);
        radioButtonCenterCenter.addActionListener(e -> polygon.setTransformOrigin());
        radioButtonCenterCenter.setSelected(true);

        transformOriginRadioButtons.add(radioButtonCenterRight);
        radioButtonCenterRight.addActionListener(e -> polygon.setTransformOrigin(TransformOrigin.CENTER, TransformOrigin.RIGHT));

        transformOriginRadioButtons.add(radioButtonBottomLeft);
        radioButtonBottomLeft.addActionListener(e -> polygon.setTransformOrigin(TransformOrigin.BOTTOM, TransformOrigin.LEFT));

        transformOriginRadioButtons.add(radioButtonBottomCenter);
        radioButtonBottomCenter.addActionListener(e -> polygon.setTransformOrigin(TransformOrigin.BOTTOM, TransformOrigin.CENTER));

        transformOriginRadioButtons.add(radioButtonBottomRight);
        radioButtonBottomRight.addActionListener(e -> polygon.setTransformOrigin(TransformOrigin.BOTTOM, TransformOrigin.RIGHT));

        this.pack();

        buttonDrawPolygon.addActionListener(e -> {
            try {
                enableButtons();
                setTextFieldChangesInfoDefaultValue();
                setControlElementsDefaultValue();
                parsePolygon();
                updateView();
            } catch (Exception exception) {
                SwingUtils.showErrorMessageBox(exception);
            }
        });

        buttonAcceptChanges.addActionListener(e -> {
            try {
                updateView();
                setControlElementsDefaultValue();
            } catch (Exception exception) {
                SwingUtils.showErrorMessageBox(exception);
            }
        });

        buttonLoadFromFile.addActionListener(e -> {
            try {
                if (fileChooserOpen.showOpenDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
                    double[][] arr = ArrayUtils.readDoubleArray2FromFile(fileChooserOpen.getSelectedFile().getPath());
                    JTableUtils.writeArrayToJTable(tablePoints, arr);

                    setControlElementsDefaultValue();
                    parsePolygon();
                    enableButtons();
                    updateView();
                }
            } catch (Exception exception) {
                SwingUtils.showErrorMessageBox(exception);
            }
        });

        ChangeListener spinnersScaleListener = e -> {
            if (!checkBoxScale.isSelected()) {
                return;
            }

            JSpinner spinner = (JSpinner) e.getSource();
            double value = (Double) spinner.getValue();

            spinnerScaleX.setValue(value);
            spinnerScaleY.setValue(value);
        };

        spinnerScaleX.addChangeListener(spinnersScaleListener);
        spinnerScaleY.addChangeListener(spinnersScaleListener);
        checkBoxScale.addChangeListener(e -> {
            if (!checkBoxScale.isSelected()) {
                return;
            }

            double scaleX = (Double) spinnerScaleX.getValue();
            double scaleY = (Double) spinnerScaleY.getValue();
            double value = Math.max(scaleX, scaleY);

            spinnerScaleX.setValue(value);
            spinnerScaleY.setValue(value);
        });

        buttonSave.addActionListener(e -> {
            try {
                if (fileChooserSave.showSaveDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooserSave.getSelectedFile();
                    polygon.saveAsTxt(file);
                }
            } catch (Exception exception) {
                SwingUtils.showErrorMessageBox(exception);
            }
        });
        buttonSaveAsSvg.addActionListener(e -> {
            try {
                if (fileChooserSave.showSaveDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooserSave.getSelectedFile();
                    polygon.saveAsSvgTo(file);
                }
            } catch (Exception exception) {
                SwingUtils.showErrorMessageBox(exception);
            }
        });
    }

    private void enableButtons() {
        buttonSave.setEnabled(true);
        buttonSaveAsSvg.setEnabled(true);
        buttonAcceptChanges.setEnabled(true);
    }

    private void setTextFieldChangesInfoDefaultValue() {
        textFieldScaleXInfo.setText(DEFAULT_SCALE_VALUE);
        textFieldScaleYInfo.setText(DEFAULT_SCALE_VALUE);
        textFieldTranslateXInfo.setText(DEFAULT_TRANSLATE_VALUE);
        textFieldTranslateYInfo.setText(DEFAULT_TRANSLATE_VALUE);
    }

    private SpinnerNumberModel getSpinnerScaleModel() {
        return new SpinnerNumberModel(DEFAULT_SCALE, 0.1, Integer.MAX_VALUE, SCALE_STEP);
    }

    private void setControlElementsDefaultValue() {
        spinnerScaleX.setValue(DEFAULT_SCALE);
        checkBoxFlipHorizontally.setSelected(false);
        spinnerScaleY.setValue(DEFAULT_SCALE);
        checkBoxFlipVertically.setSelected(false);
        spinnerTranslateX.setValue(DEFAULT_TRANSLATE);
        spinnerTranslateY.setValue(DEFAULT_TRANSLATE);
    }

    private void parsePolygon() throws ParseException {
        double[][] pointsCoordinates = JTableUtils.readDoubleMatrixFromJTable(tablePoints);
        points = new Point[pointsCoordinates.length];

        for (int i = 0; i < pointsCoordinates.length; i++) {
            double x = pointsCoordinates[i][0], y = pointsCoordinates[i][1];
            points[i] = new Point(x, y);
        }

        polygon = new Polygon(points);
    }

    private void updateInfoLabels() {
        textFieldArea.setText(String.format("%.3f", polygon.getArea()));
        textFieldPerimeter.setText(String.format("%.3f", polygon.getPerimeter()));

        Double scaleX = (Double) spinnerScaleX.getValue();
        double currentValueScaleX = Double.parseDouble(textFieldScaleXInfo.getText()) * scaleX;
        textFieldScaleXInfo.setText(String.format("%.3f", currentValueScaleX));

        Double scaleY = (Double) spinnerScaleY.getValue();
        double currentValueScaleY = Double.parseDouble(textFieldScaleYInfo.getText()) * scaleY;
        textFieldScaleYInfo.setText(String.format("%.3f", currentValueScaleY));

        Integer translateX = (Integer) spinnerTranslateX.getValue();
        int currentValueTranslateX = Integer.parseInt(textFieldTranslateXInfo.getText()) + translateX;
        textFieldTranslateXInfo.setText(String.format("%d", currentValueTranslateX));

        Integer translateY = (Integer) spinnerTranslateY.getValue();
        int currentValueTranslateY = Integer.parseInt(textFieldTranslateYInfo.getText()) + translateY;
        textFieldTranslateYInfo.setText(String.format("%d", currentValueTranslateY));

        this.pack();
    }

    private void drawPolygon() {
        img = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_BGR);
        Graphics2D g2D = img.createGraphics();
        g2D.setColor(Color.WHITE);
        g2D.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        Double scaleX = (Double) spinnerScaleX.getValue();
        if (checkBoxFlipHorizontally.isSelected()) {
            scaleX = -scaleX;
        }
        Double scaleY = (Double) spinnerScaleY.getValue();
        if (checkBoxFlipVertically.isSelected()) {
            scaleY = -scaleY;
        }

        Integer translateX = (Integer) spinnerTranslateX.getValue();
        Integer translateY = (Integer) spinnerTranslateY.getValue();

        polygon.matrix(scaleX, scaleY, translateX, translateY);

        if (checkBoxDrawRectangle.isSelected()) {
            g2D.setColor(Color.RED);
            Polygon circumscribedRectangle = polygon.getCircumscribedRectangle();
            circumscribedRectangle.draw(g2D);
        }
        g2D.setColor(Color.BLACK);
        polygon.draw(g2D);

        canvas.setIcon(new ImageIcon(img));
    }

    private void updateView() {
        drawPolygon();
        updateInfoLabels();
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
        panelMain.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 3, new Insets(20, 20, 20, 20), -1, -1));
        canvas = new JLabel();
        canvas.setText("");
        panelMain.add(canvas, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelInfo = new JPanel();
        panelInfo.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 2, new Insets(10, 10, 10, 10), -1, -1));
        panelMain.add(panelInfo, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        label1.setText("Площадь:");
        panelInfo.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Периметр:");
        panelInfo.add(label2, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldArea = new JTextField();
        textFieldArea.setEditable(false);
        panelInfo.add(textFieldArea, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        textFieldPerimeter = new JTextField();
        textFieldPerimeter.setEditable(false);
        panelInfo.add(textFieldPerimeter, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("(Если многоугольник содержит пересекающиеся линии, то площадь может отображаться некорректно)");
        panelInfo.add(label3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(100, 500), null, null, 0, false));
        scrollPanePoints = new JScrollPane();
        panel1.add(scrollPanePoints, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tablePoints = new JTable();
        scrollPanePoints.setViewportView(tablePoints);
        buttonLoadFromFile = new JButton();
        buttonLoadFromFile.setText("Загрузить из файла");
        panel1.add(buttonLoadFromFile, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonDrawPolygon = new JButton();
        buttonDrawPolygon.setText("Нарисовать новый многоугольник");
        panel1.add(buttonDrawPolygon, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonSave = new JButton();
        buttonSave.setEnabled(false);
        buttonSave.setText("Сохранить многоугольник (.poly)");
        panel1.add(buttonSave, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonSaveAsSvg = new JButton();
        buttonSaveAsSvg.setEnabled(false);
        buttonSaveAsSvg.setText("Сохранить как SVG (.svg)");
        panel1.add(buttonSaveAsSvg, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelControl = new JPanel();
        panelControl.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 1, new Insets(10, 10, 10, 10), -1, -1));
        panelMain.add(panelControl, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelControl.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        panelScale = new JPanel();
        panelScale.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 4, new Insets(5, 5, 5, 5), -1, -1));
        panelControl.add(panelScale, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelScale.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label4 = new JLabel();
        label4.setText("Масштабирование");
        panelScale.add(label4, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Ось X:");
        panelScale.add(label5, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Ось Y:");
        panelScale.add(label6, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spinnerScaleX = new JSpinner();
        panelScale.add(spinnerScaleX, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spinnerScaleY = new JSpinner();
        panelScale.add(spinnerScaleY, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBoxScale = new JCheckBox();
        checkBoxScale.setText("Пропорциональное");
        panelScale.add(checkBoxScale, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldScaleXInfo = new JTextField();
        textFieldScaleXInfo.setEditable(false);
        panelScale.add(textFieldScaleXInfo, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Текущий масштаб по оси X:");
        panelScale.add(label7, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldScaleYInfo = new JTextField();
        textFieldScaleYInfo.setEditable(false);
        panelScale.add(textFieldScaleYInfo, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Текущий масштаб по оси Y:");
        panelScale.add(label8, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBoxFlipHorizontally = new JCheckBox();
        checkBoxFlipHorizontally.setText("Отразить по горизонтали");
        panelScale.add(checkBoxFlipHorizontally, new com.intellij.uiDesigner.core.GridConstraints(2, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBoxFlipVertically = new JCheckBox();
        checkBoxFlipVertically.setText("Отразить по вертикали");
        panelScale.add(checkBoxFlipVertically, new com.intellij.uiDesigner.core.GridConstraints(4, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(5, 5, 5, 5), -1, -1));
        panelControl.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label9 = new JLabel();
        label9.setText("Выбор опорной точки трансформации");
        panel2.add(label9, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        radioButtonTopLeft = new JRadioButton();
        radioButtonTopLeft.setText("");
        panel3.add(radioButtonTopLeft, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radioButtonTopCenter = new JRadioButton();
        radioButtonTopCenter.setText("");
        panel3.add(radioButtonTopCenter, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radioButtonTopRight = new JRadioButton();
        radioButtonTopRight.setText("");
        panel3.add(radioButtonTopRight, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radioButtonCenterLeft = new JRadioButton();
        radioButtonCenterLeft.setText("");
        panel3.add(radioButtonCenterLeft, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radioButtonBottomLeft = new JRadioButton();
        radioButtonBottomLeft.setText("");
        panel3.add(radioButtonBottomLeft, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radioButtonCenterCenter = new JRadioButton();
        radioButtonCenterCenter.setText("");
        panel3.add(radioButtonCenterCenter, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radioButtonCenterRight = new JRadioButton();
        radioButtonCenterRight.setText("");
        panel3.add(radioButtonCenterRight, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radioButtonBottomCenter = new JRadioButton();
        radioButtonBottomCenter.setText("");
        panel3.add(radioButtonBottomCenter, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radioButtonBottomRight = new JRadioButton();
        radioButtonBottomRight.setText("");
        panel3.add(radioButtonBottomRight, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelTranslate = new JPanel();
        panelTranslate.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 3, new Insets(5, 5, 5, 5), -1, -1));
        panelControl.add(panelTranslate, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelTranslate.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label10 = new JLabel();
        label10.setText("Перемещение");
        panelTranslate.add(label10, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Ось X:");
        panelTranslate.add(label11, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("Ось Y:");
        panelTranslate.add(label12, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spinnerTranslateX = new JSpinner();
        panelTranslate.add(spinnerTranslateX, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spinnerTranslateY = new JSpinner();
        panelTranslate.add(spinnerTranslateY, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("Текущее смещение по оси X");
        panelTranslate.add(label13, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldTranslateXInfo = new JTextField();
        textFieldTranslateXInfo.setEditable(false);
        panelTranslate.add(textFieldTranslateXInfo, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        textFieldTranslateYInfo = new JTextField();
        textFieldTranslateYInfo.setEditable(false);
        panelTranslate.add(textFieldTranslateYInfo, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setText("Текущее смещение по оси Y");
        panelTranslate.add(label14, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonAcceptChanges = new JButton();
        buttonAcceptChanges.setEnabled(false);
        buttonAcceptChanges.setText("Применить изменения");
        panelControl.add(buttonAcceptChanges, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBoxDrawRectangle = new JCheckBox();
        checkBoxDrawRectangle.setText("Рисовать описанный прямоугольник");
        panelControl.add(checkBoxDrawRectangle, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }

}
