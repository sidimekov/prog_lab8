package gui.forms;

import gui.GuiManager;
import util.LocalizationManager;

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
                Locale ruLocale = Locale.of("ru", "RU");
                if (LocalizationManager.getLocale() != ruLocale) {
                    LocalizationManager.setLocale(ruLocale);
                    guiManager.updateLanguage();
                }
                dispose();
            }
        });
        estonianButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Locale locale = Locale.of("et", "EE");
                if (LocalizationManager.getLocale() != locale) {
                    LocalizationManager.setLocale(locale);
                    guiManager.updateLanguage();
                }
                dispose();
            }
        });
        ukrainianButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Locale locale = Locale.of("uk", "UA");
                if (LocalizationManager.getLocale() != locale) {
                    LocalizationManager.setLocale(locale);
                    guiManager.updateLanguage();
                }
                dispose();
            }
        });
        spanishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Locale locale = Locale.of("es", "HN");
                if (LocalizationManager.getLocale() != locale) {
                    LocalizationManager.setLocale(locale);
                    guiManager.updateLanguage();
                }
                dispose();
            }
        });

    }
}
