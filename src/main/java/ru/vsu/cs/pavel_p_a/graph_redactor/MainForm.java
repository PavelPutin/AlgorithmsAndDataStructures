package ru.vsu.cs.pavel_p_a.graph_redactor;

import javax.swing.*;
import java.awt.*;

public class MainForm extends JFrame {

    public MainForm () {
        this.setTitle("Graph redactor");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        GraphRedactor graphRedactor = new GraphRedactor();
        graphRedactor.setPreferredSize(new Dimension(500, 500));
        this.add(graphRedactor);
        this.pack();
    }

}
