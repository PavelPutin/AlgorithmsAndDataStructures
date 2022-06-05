package ru.vsu.cs.course1.tree.demo;

import java.awt.EventQueue;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.UIManager;
import ru.vsu.cs.util.SwingUtils;


public class Program {

    /**
     * Основная функция программы
     *
     * @param args Параметры командной строки
     * @throws Exception Любое исключение
     */
    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.ROOT);

        //SwingUtils.setLookAndFeelByName("Windows");
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        //SwingUtils.setDefaultFont(null, 20);
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        SwingUtils.setDefaultFont("Arial", 20);

        EventQueue.invokeLater(() -> {
            try {
                JFrame frameMain = new TreeDemoFrame();
                frameMain.setVisible(true);
                frameMain.setExtendedState(MAXIMIZED_BOTH);
            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }
        });
    }
}
