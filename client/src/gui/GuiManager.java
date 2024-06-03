package gui;

import gui.forms.MainForm;
import gui.forms.SignInForm;
import gui.forms.SignUpForm;

import javax.swing.*;

public class GuiManager {
    private static GuiManager instance;
    private final JFrame frame;
    private SignInForm signInForm;
    private SignUpForm signUpForm;
    private MainForm mainForm;

    private GuiManager(JFrame frame) {
        this.frame = frame;
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(1024, 512);
        this.frame.setLocationRelativeTo(null);
        instance = this;
    }

    public static GuiManager getInstance() {
        if (instance == null) {
            instance = new GuiManager(new JFrame());
        }
        return instance;
    }

    public void run() {
        signInForm = new SignInForm();
        signUpForm = new SignUpForm();
        mainForm = new MainForm();
        openSignInPanel();
    }

    public void openSignInPanel() {
        frame.setContentPane(signInForm.getSignInPanel());
        frame.setVisible(true);
    }
    public void openSignInPanel(String message) {
        signInForm.clearMessageLabel();
        signInForm.setMessageLabel(message);
        openSignInPanel();
//        signInForm.setAuthMessageLabel(null);
    }

    public void openSingUpPanel() {
        frame.setContentPane(signUpForm.getAuthSignUpPanel());
        frame.setVisible(true);
    }

    public void openMainPanel() {
        frame.setContentPane(mainForm.getMainPanel());
        frame.setSize(1024, 512);
        frame.setVisible(true);
    }
}
