package gui.forms;

import commandManagers.CommandInvoker;
import enums.ReadModes;
import enums.ResponseStatus;
import gui.GuiManager;
import network.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class SignUpForm {
    GuiManager guiManager = GuiManager.getInstance();
    public SignUpForm() {
        authSignInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiManager.openSignInPanel();
            }
        });
        authSignUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = authLoginField.getText();
                char[] password = authPasswordField.getPassword();
                char[] passwordConfirm = authPasswordConfirmField.getPassword();
                Response response = CommandInvoker.getInstance().runCommand(String.format("register %s %s %s", login, new String(password), new String(passwordConfirm)), ReadModes.CONSOLE);
                System.out.println(response);
                switch (response.getStatus()) {
                    case OK -> {
                        guiManager.openSignInPanel("Signed Up! Now you sign in");
                    }
                    case CLIENT_ERROR -> {
                        authMessageLabel.setForeground(Color.RED);
                        authMessageLabel.setText(response.getMessage());
                    }
                    case SERVER_ERROR -> {
                        authMessageLabel.setForeground(Color.RED);
                        authMessageLabel.setText("Произошла ошибка при соединении с сервером, попробуйте позже");
                    }
                }
            }
        });
    }
    private JPanel authRightPanel;
    private JPanel authLeftPanel;
    private JLabel authHelloLabel;
    private JLabel authLoginLabel;
    private JTextField authLoginField;
    private JPasswordField authPasswordField;
    private JButton authSignUpButton;
    private JButton authSignInButton;
    private JLabel authSignUpLabel;
    private JPanel authSignUpPanel;
    private JPasswordField authPasswordConfirmField;
    private JLabel authMessageLabel;

    public JPanel getAuthSignUpPanel() {
        return authSignUpPanel;
    }
}
