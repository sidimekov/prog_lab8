package gui.forms;

import gui.GuiManager;

import javax.swing.*;
import java.awt.*;

public class MainForm extends JPanel {
    GuiManager guiManager = GuiManager.getInstance();

    private JPanel mainPanel;
    private JPanel upperPanel;
    private JPanel lowerPanel;
    private JTable table;
    private JButton addButton;
    private JButton removeButton;
    private JButton cmdsButton;
    private JButton visualButton;
    private JLabel nameLabel;
    public MainForm() {

        mainPanel = new JPanel(new BorderLayout(20,20));
        mainPanel.setPreferredSize(new Dimension(1440, 720));

        upperPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        upperPanel.setBounds(0,100,1440, 100);
        mainPanel.setPreferredSize(new Dimension(1440, 120));
        upperPanel.setBackground(new Color(0, 100, 120));

        lowerPanel = new JPanel(new GridLayout(1, 5));
        lowerPanel.setLayout(new GridLayout());
        lowerPanel.setBorder(BorderFactory.createEmptyBorder(40,100,40,100));
        lowerPanel.setBackground(new Color(0, 110, 140));
//        lowerPanel.setBounds(0,620,1440,1);
        mainPanel.setPreferredSize(new Dimension(1440, 120));

        table = new JTable(new String[][]{{"1","2","3"},{"4","5","6"},{"7","8","9"}}, new String[]{"1","2","3"});

        mainPanel.add(upperPanel, BorderLayout.NORTH);
        mainPanel.add(table, BorderLayout.CENTER);
        mainPanel.add(lowerPanel, BorderLayout.SOUTH);

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
