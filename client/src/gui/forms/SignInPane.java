package gui.forms;

import util.CommandInvoker;
import enums.ReadModes;
import gui.GuiManager;
import network.Response;
import util.LocalizationManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SignInPane extends JPanel  {
    GuiManager guiManager = GuiManager.getInstance();
    private JPanel signInPanel;
    private JPanel rightPanel;
    private JPanel leftPanel;
    private JLabel helloLabel;
    private JLabel loginLabel;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton signInButton;
    private JLabel signUpLabel;
    private JButton signUpButton;
    private JLabel messageLabel;
    private JButton languageButton;

    public SignInPane() {

        signInPanel = new JPanel(new GridLayout(1,2,0,0));
        signInPanel.setBounds(0, 0, 1024, 512);

        rightPanel = new JPanel(new GridLayout());
        rightPanel.setBounds(512,0,512, 512);
        rightPanel.setBackground(new Color(0,180,170));

        try {
            BufferedImage pelmeni = ImageIO.read(new File("client/src/resources/pelmeni.png"));
            JLabel pelmeniLabel = new JLabel(new ImageIcon(pelmeni));
            rightPanel.add(pelmeniLabel);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

//        leftPanel = new JPanel(new GridLayout(10, 1, 10, 10));
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(40,100,40,100));
        leftPanel.setBounds(0,0,512,512);
        leftPanel.setBounds(0,0,512,512);

        helloLabel = new JLabel(LocalizationManager.getString("authHello"));
        helloLabel.setFont(guiManager.getDefaultFont(32));
        leftPanel.add(helloLabel);

        loginLabel = new JLabel(LocalizationManager.getString("signInLabel"));
        loginLabel.setFont(guiManager.getDefaultFont(20));
        leftPanel.add(loginLabel);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        loginField = new JTextField();
        loginField.setMaximumSize(new Dimension(500, 40));
        loginField.setFont(guiManager.getDefaultFont());
        leftPanel.add(loginField);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(500, 40));
        leftPanel.add(passwordField);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        signInButton = new JButton(LocalizationManager.getString("signInButton"));
        signInButton.setFont(guiManager.getDefaultFont());
        leftPanel.add(signInButton);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        messageLabel = new JLabel();
        messageLabel.setFont(guiManager.getDefaultFont());
        leftPanel.add(messageLabel);

        signUpLabel = new JLabel(LocalizationManager.getString("signInFirstTime"));
        signUpLabel.setFont(guiManager.getDefaultFont());
        leftPanel.add(signUpLabel);

        signUpButton = new JButton(LocalizationManager.getString("signUpButton"));
        signUpButton.setFont(guiManager.getDefaultFont());
        leftPanel.add(signUpButton);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        languageButton = new JButton();
        languageButton.setHorizontalAlignment(SwingConstants.CENTER);
        languageButton.setIcon(new ImageIcon("client/src/resources/language.png"));
        languageButton.setOpaque(false);
        languageButton.setContentAreaFilled(false);
        languageButton.setBorderPainted(false);
        leftPanel.add(languageButton);

        signInPanel.add(leftPanel);
        signInPanel.add(rightPanel);

        updateLanguage();

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiManager.openSignUpPanel();
            }
        });
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = loginField.getText();
                String password = new String(passwordField.getPassword());
                Response response = CommandInvoker.getInstance().runCommand(String.format("login %s %s", login, password), ReadModes.CONSOLE);
                switch (response.getStatus()) {
                    case OK -> {
                        guiManager.openMainPanel();
                        loginField.setText("");
                        passwordField.setText("");
                        messageLabel.setText("");
                    }
                    case CLIENT_ERROR -> {
                        messageLabel.setForeground(Color.RED);
                        messageLabel.setText(response.getMessage());
                    }
                    case SERVER_ERROR -> {
                        messageLabel.setForeground(Color.RED);
                        messageLabel.setText(LocalizationManager.getString("serverError"));
                    }
                }
            }
        });
        languageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiManager.openLanguageDialog(guiManager.getMainFrame());
            }
        });
    }

    public JPanel getSignInPanel() {
        return signInPanel;
    }

    public JLabel getMessageLabel() {
        return messageLabel;
    }

    public void setMessageLabel(String message) {
        this.messageLabel.setText(message);
    }

    public void clearMessageLabel() {
        this.messageLabel.setText("");
        this.messageLabel.setForeground(Color.BLACK);
    }

    public void updateLanguage() {
        helloLabel.setText(LocalizationManager.getString("authHello"));
        loginLabel.setText(LocalizationManager.getString("signInLabel"));
        signUpLabel.setText(LocalizationManager.getString("signInFirstTime"));
        signInButton.setText(LocalizationManager.getString("signInButton"));;
        signUpButton.setText(LocalizationManager.getString("signUpButton"));;
    }
}
