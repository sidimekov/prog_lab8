package gui.forms;

import commandManagers.CommandInvoker;
import enums.ReadModes;
import gui.GuiManager;
import network.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignInForm extends JPanel{
    GuiManager guiManager = GuiManager.getInstance();
    public SignInForm() {
        authSignUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiManager.openSingUpPanel();
            }
        });
        authSignInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = authLoginField.getText();
                String password = new String(authPasswordField.getPassword());
                Response response = CommandInvoker.getInstance().runCommand(String.format("login %s %s", login, password), ReadModes.CONSOLE);
                switch (response.getStatus()) {
                    case OK -> guiManager.openMainPanel();
                    case CLIENT_ERROR -> {
                        authMessageLabel.setForeground(Color.RED);
                        authMessageLabel.setText(response.getMessage());
                    }
                    case SERVER_ERROR -> {
                        authMessageLabel.setForeground(Color.RED);
                        authMessageLabel.setText("An error occurred with the server connection! Try again later");
                    }
                }
            }
        });
    }
    private JPanel authSignInPanel;
    private JLabel authHelloLabel;
    private JTextField authLoginField;
    private JLabel authLoginLabel;
    private JButton authSignInButton;
    private JPasswordField authPasswordField;
    private JPanel authRightPanel;
    private JPanel authLeftPanel;
    private JLabel authSignUpLabel;
    private JButton authSignUpButton;
    private JLabel authMessageLabel;

    public JPanel getSignInPanel() {
        return authSignInPanel;
    }

    public JLabel getMessageLabel() {
        return authMessageLabel;
    }
    public void setMessageLabel(String message) {
        this.authMessageLabel.setText(message);
    }
    public void clearMessageLabel() {
        this.authMessageLabel.setText("");
        this.authMessageLabel.setForeground(Color.BLACK);
    }
}
