package gui.forms;

import commandManagers.CommandInvoker;
import enums.ReadModes;
import gui.GuiManager;
import network.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpPane extends JPanel {
    GuiManager guiManager = GuiManager.getInstance();
    private JPanel signUpPanel;
    private JPanel rightPanel;
    private JPanel leftPanel;
    private JLabel helloLabel;
    private JLabel registerLabel;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JPasswordField passwordConfirmField;
    private JButton signInButton;
    private JLabel signUpLabel;
    private JButton signUpButton;
    private JLabel messageLabel;

    public SignUpPane() {

        signUpPanel = new JPanel(new GridLayout(1,2,0,0));
        signUpPanel.setBounds(0, 0, 1024, 512);

        rightPanel = new JPanel(new GridLayout());
        rightPanel.setBounds(512,0,512, 512);
        rightPanel.setBackground(new Color(0,180,170));

//        leftPanel = new JPanel(new GridLayout(10, 1, 10, 10));
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(40,100,40,100));
        leftPanel.setBounds(0,0,512,512);
        leftPanel.setBounds(0,0,512,512);

        helloLabel = new JLabel(guiManager.getResourceBundle().getString("authHello"));
        helloLabel.setFont(guiManager.getDefaultFont(32));
        leftPanel.add(helloLabel);

        registerLabel = new JLabel(guiManager.getResourceBundle().getString("signUpLabel"));
        registerLabel.setFont(guiManager.getDefaultFont(16));
        leftPanel.add(registerLabel);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        loginField = new JTextField();
        loginField.setFont(guiManager.getDefaultFont());
        loginField.setMaximumSize(new Dimension(500, 40));
        leftPanel.add(loginField);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(500, 40));
        leftPanel.add(passwordField);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        passwordConfirmField = new JPasswordField();
        passwordConfirmField.setMaximumSize(new Dimension(500, 40));
        leftPanel.add(passwordConfirmField);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        signUpButton = new JButton(guiManager.getResourceBundle().getString("signUpButton"));
        signUpButton.setFont(guiManager.getDefaultFont());
        leftPanel.add(signUpButton);

        messageLabel = new JLabel();
        messageLabel.setFont(guiManager.getDefaultFont());
        leftPanel.add(messageLabel);

        signUpLabel = new JLabel(guiManager.getResourceBundle().getString("signUpSecondTime"));
        signUpLabel.setFont(guiManager.getDefaultFont());
        leftPanel.add(signUpLabel);

        signInButton = new JButton(guiManager.getResourceBundle().getString("signInButton"));
        signInButton.setFont(guiManager.getDefaultFont());
        leftPanel.add(signInButton);

        signUpPanel.add(leftPanel);
        signUpPanel.add(rightPanel);

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiManager.openSignInPanel();
            }
        });
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = loginField.getText();
                char[] password = passwordField.getPassword();
                char[] passwordConfirm = passwordConfirmField.getPassword();
                Response response = CommandInvoker.getInstance().runCommand(String.format("register %s %s %s", login, new String(password), new String(passwordConfirm)), ReadModes.CONSOLE);
                System.out.println(response);
                switch (response.getStatus()) {
                    case OK -> guiManager.openMainPanel();
                    case CLIENT_ERROR -> {
                        messageLabel.setForeground(Color.RED);
                        messageLabel.setText(response.getMessage());
                    }
                    case SERVER_ERROR -> {
                        messageLabel.setForeground(Color.RED);
                        messageLabel.setText("Произошла ошибка при соединении с сервером, попробуйте позже");
                    }
                }
            }
        });
    }

    public JPanel getSignUpPanel() {
        return signUpPanel;
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
