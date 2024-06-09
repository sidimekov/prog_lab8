package gui;

import entity.Route;
import gui.forms.*;
import network.Client;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class GuiManager {
    private static GuiManager instance;
    private final JFrame frame;
    private SignInPane signInForm;
    private SignUpPane signUpForm;
    private MainPane mainPanel;
    private ResourceBundle resourceBundle;
    private Locale locale;
    private Client client = Client.getInstance();
    public static final String FONT_HTML_STRING = "<html><font name=\"Century Gothic\" size=\"4\"/>";

    private GuiManager(JFrame frame) {
        this.frame = frame;
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(1024, 512);
        this.frame.setLocationRelativeTo(null);
        instance = this;
    }

    public JFrame getFrame() {
        return frame;
    }

    public static GuiManager getInstance() {
        if (instance == null) {
            instance = new GuiManager(new JFrame());
        }
        instance.updateLocale(null);
        return instance;
    }

    public void run() {
        signInForm = new SignInPane();
        signUpForm = new SignUpPane();
        mainPanel = new MainPane();
        openSignInPanel();
    }

    public void openSignInPanel() {
        frame.setContentPane(signInForm.getSignInPanel());
        frame.pack();
        frame.setSize(1024, 512);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    public void openSignInPanel(String message) {
        signInForm.clearMessageLabel();
        signInForm.setMessageLabel(message);
        frame.setSize(1024, 512);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        openSignInPanel();
//        signInForm.setAuthMessageLabel(null);
    }

    public void openSingUpPanel() {
        JPanel signUpPanel = signUpForm.getSignUpPanel();
//        frame.add()
        frame.setContentPane(signUpPanel);
        frame.setSize(1024, 512);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public void openMainPanel() {
        frame.setContentPane(mainPanel.getMainPanel());

        mainPanel.updateTableData();
        mainPanel.updateUserLabel(client.getUser().getLogin());

        frame.setPreferredSize(new Dimension(1440, 720));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
//        frame.pack();
        frame.setVisible(true);
    }

    public MainPane getMainPanel() {
        return mainPanel;
    }

    public void openAddDialog() {
        AddDialog addDialog = new AddDialog(frame);

        addDialog.setPreferredSize(new Dimension(800, 500));
        addDialog.pack();
        addDialog.setLocationRelativeTo(null);

        addDialog.setVisible(true);
    }
    public void openOtherCommandsDialog() {
        OtherCommandsDialog otherCommandsDialog = new OtherCommandsDialog(frame);

        otherCommandsDialog.setPreferredSize(new Dimension(800, 400));
        otherCommandsDialog.pack();
        otherCommandsDialog.setLocationRelativeTo(null);

        otherCommandsDialog.setVisible(true);
    }

    public File chooseFile() {
        return chooseFile(null);
    }

    public File chooseFile(File chosenFile) {
//        ChooseFileDialog chooseFile = new ChooseFileDialog(frame);
//
//        chooseFile.setSize(720,720);
//        chooseFile.setModal(true);
//        chooseFile.pack();
//        chooseFile.setLocationRelativeTo(null);
//
//        chooseFile.setVisible(true);
//
//        return chooseFile.getSelectedFile();

        JFileChooser fileChooser;
        if (chosenFile != null) {
            fileChooser = new JFileChooser(chosenFile.getPath());
        } else {
            fileChooser = new JFileChooser();
        }

        int result = fileChooser.showOpenDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    public Route specifyRouteDialog() {
        return specifyRouteDialog(null);
    }
    public Route specifyRouteDialog(Route route) {
        SpecifyRouteDialog specifyRoute = new SpecifyRouteDialog(frame, route);

        specifyRoute.setVisible(true);

        return specifyRoute.getSpecifiedRoute();
    }

    public Font getDefaultFont(int size) {
        return new Font("Century Gothic",Font.PLAIN, size);
    }
    public Font getDefaultFont() {
        return getDefaultFont(16);
    }

    public Locale getLocale() {
        return locale;
    }

    public void updateLocale(Locale locale) {

        ResourceBundle.clearCache();
        if (locale != null) {
            resourceBundle = ResourceBundle.getBundle("resources.gui", locale);
        } else {
            // получить английский дефолтный из gui.properties
            resourceBundle = ResourceBundle.getBundle("resources.gui",
                    new ResourceBundle.Control() {
                        @Override
                        public List<Locale> getCandidateLocales(String name,
                                                                Locale locale) {
                            return Collections.singletonList(Locale.ROOT);
                        }
                    });
            return;
        }

        Locale.setDefault(locale);
        this.locale = locale;

    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }


}
