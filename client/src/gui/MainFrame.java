package gui;

import javax.swing.*;

public class MainFrame extends JFrame {
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 512;

    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
    }
}
