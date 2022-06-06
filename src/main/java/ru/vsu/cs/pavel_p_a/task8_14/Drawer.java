package ru.vsu.cs.pavel_p_a.task8_14;

import javax.swing.*;
import java.awt.*;

public class Drawer extends JFrame {
    public Drawer() {
        this.setTitle("Test drawing");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DrawerComponent dc = new DrawerComponent();
        dc.setPreferredSize(new Dimension(200, 200));
        this.add(dc);
        this.pack();
    }
}
