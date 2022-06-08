package ru.vsu.cs.pavel_p_a.graph_redactor;

import ru.vsu.cs.course1.graph.Graph;
import ru.vsu.cs.pavel_p_a.task8_14.Solution;

import javax.swing.*;

public class MainForm extends JFrame {

    private GraphRedactor graphRedactorA;
    private GraphRedactor graphRedactorB;

    private JPanel panel1;
    private JButton areGraphsIsomericButton;
    private JLabel areGraphsIsomericLabel;
    private JPanel controlPanel;

    public MainForm () {
        this.setTitle("Graph redactor");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
        graphRedactorA = new GraphRedactor();
//        graphRedactor.setPreferredSize(new Dimension(500, 500));
        panel1.add(graphRedactorA);

        graphRedactorB = new GraphRedactor();
        panel1.add(graphRedactorB);

        controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));
        panel1.add(controlPanel);

        areGraphsIsomericLabel = new JLabel();
        controlPanel.add(areGraphsIsomericLabel);

        areGraphsIsomericButton = new JButton("Проверить графы на изоморфность");
        areGraphsIsomericButton.addActionListener(e -> checkIsomeric());
        controlPanel.add(areGraphsIsomericButton);

        this.add(panel1);
        this.pack();
    }

    private void checkIsomeric() {
        Graph graphA = graphRedactorA.getGraph();
        System.out.println(graphA.vertexCount());
        Graph graphB = graphRedactorB.getGraph();
        System.out.println(graphB.vertexCount());

        boolean result = Solution.areGraphsIsomeric(graphA, graphB);
        String labelText = result ? "Графы изоморфны" : "Графы неизоморфны";
        areGraphsIsomericLabel.setText(labelText);
    }
}
