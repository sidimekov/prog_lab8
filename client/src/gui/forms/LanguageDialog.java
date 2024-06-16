package gui.forms;

import gui.GuiManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

public class LanguageDialog extends JDialog {
    private JPanel contentPane;
    private JButton russianButton;
    private JLabel langLabel;
    private JButton estonianButton;
    private JButton ukrainianButton;
    private JButton spanishButton;

    public LanguageDialog(Frame frame) {
        super(frame);
        setContentPane(contentPane);
        setModal(true);

        GuiManager guiManager = GuiManager.getInstance();

        russianButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiManager.updateLocale(Locale.of("ru", "RU"));
                dispose();
            }
        });
        estonianButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiManager.updateLocale(Locale.of("et", "EE"));
                dispose();
            }
        });
        ukrainianButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiManager.updateLocale(Locale.of("uk", "UA"));
                dispose();
            }
        });
        spanishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiManager.updateLocale(Locale.of("es", "HN"));
                dispose();
            }
        });

    }
}
