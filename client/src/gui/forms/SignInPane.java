package gui.forms;

import commandManagers.CommandInvoker;
import enums.ReadModes;
import gui.GuiManager;
import network.Response;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SignInPane extends JPanel {
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

        helloLabel = new JLabel(guiManager.getResourceBundle().getString("authHello"));
        helloLabel.setFont(guiManager.getDefaultFont(32));
        leftPanel.add(helloLabel);

        loginLabel = new JLabel(guiManager.getResourceBundle().getString("signInLabel"));
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

        signInButton = new JButton(guiManager.getResourceBundle().getString("signInButton"));
        signInButton.setFont(guiManager.getDefaultFont());
        leftPanel.add(signInButton);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        messageLabel = new JLabel();
        messageLabel.setFont(guiManager.getDefaultFont());
        leftPanel.add(messageLabel);

        signUpLabel = new JLabel(guiManager.getResourceBundle().getString("signInFirstTime"));
        signUpLabel.setFont(guiManager.getDefaultFont());
        leftPanel.add(signUpLabel);

        signUpButton = new JButton(guiManager.getResourceBundle().getString("signUpButton"));
        signUpButton.setFont(guiManager.getDefaultFont());
        leftPanel.add(signUpButton);

        signInPanel.add(leftPanel);
        signInPanel.add(rightPanel);

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiManager.openSingUpPanel();
            }
        });
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = loginField.getText();
                String password = new String(passwordField.getPassword());
                Response response = CommandInvoker.getInstance().runCommand(String.format("login %s %s", login, password), ReadModes.CONSOLE);
                switch (response.getStatus()) {
                    case OK -> guiManager.openMainPanel();
                    case CLIENT_ERROR -> {
                        messageLabel.setForeground(Color.RED);
                        messageLabel.setText(response.getMessage());
                    }
                    case SERVER_ERROR -> {
                        messageLabel.setForeground(Color.RED);
                        messageLabel.setText(guiManager.getResourceBundle().getString("serverError"));
                    }
                }
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
}
