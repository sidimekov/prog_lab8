package gui;

import entity.Route;
import gui.forms.*;
import network.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.*;

public class GuiManager {
    private static GuiManager instance;
    private final MainFrame mainFrame;
    private final VisualizationForm visualizationForm;
    private SignInPane signInForm;
    private SignUpPane signUpForm;
    private MainPane mainPanel;
    private Client client = Client.getInstance();
    public static final String FONT_HTML_STRING = "<html><font name=\"Century Gothic\" size=\"4\"/>";

    private GuiManager(MainFrame frame, VisualizationForm visualizationForm) {
        this.mainFrame = frame;
        this.visualizationForm = visualizationForm;
        instance = this;
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public static GuiManager getInstance() {
        if (instance == null) {
            instance = new GuiManager(new MainFrame(), new VisualizationForm());
        }
        return instance;
    }

    public void run() {
        signInForm = new SignInPane();
        signUpForm = new SignUpPane();
        mainPanel = new MainPane();
        openSignInPanel();
    }

    public void updateData() {
        mainPanel.updateTableData();
        visualizationForm.reload();
    }

    public void openSignInPanel() {
        signInForm.updateLanguage();
        mainFrame.setContentPane(signInForm.getSignInPanel());
        mainFrame.pack();
        mainFrame.setSize(1024, 512);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
    }
    public void openSignInPanel(String message) {
        signInForm.clearMessageLabel();
        signInForm.setMessageLabel(message);
        mainFrame.setSize(1024, 512);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        openSignInPanel();
//        signInForm.setAuthMessageLabel(null);
    }

    public void openSignUpPanel() {
        signUpForm.updateLanguage();
//        frame.add()
        mainFrame.setPreferredSize(new Dimension(1024, 512));
        mainFrame.setContentPane(signUpForm.getSignUpPanel());
        mainFrame.pack();
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
    }

    public void openMainPanel() {
        mainPanel.updateLanguage();
        mainFrame.setContentPane(mainPanel.getMainPanel());

        mainPanel.updateTableData();
        mainPanel.updateUserLabel(client.getUser().getLogin());

        mainFrame.setPreferredSize(new Dimension(1440, 720));
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(true);
//        frame.pack();
        mainFrame.setVisible(true);
    }

    public void openVisualization() {
        visualizationForm.open();
        visualizationForm.setVisible(true);
    }

    public void closeVisualization() {
        visualizationForm.dispatchEvent(new WindowEvent(visualizationForm, WindowEvent.WINDOW_CLOSING));
    }

    public MainPane getMainPanel() {
        return mainPanel;
    }

    public void openAddDialog() {
        AddDialog addDialog = new AddDialog(mainFrame);

        addDialog.setPreferredSize(new Dimension(1000, 500));
        addDialog.pack();
        addDialog.setLocationRelativeTo(null);

        addDialog.setVisible(true);
    }
    public void openOtherCommandsDialog() {
        OtherCommandsDialog otherCommandsDialog = new OtherCommandsDialog(mainFrame);

        otherCommandsDialog.setPreferredSize(new Dimension(800, 400));
        otherCommandsDialog.pack();
        otherCommandsDialog.setLocationRelativeTo(null);

        otherCommandsDialog.setVisible(true);
    }

    public void openFilterDialog() {
        FilterDialog filterDialog = new FilterDialog(mainFrame);

        filterDialog.setPreferredSize(new Dimension(600, 600));
        filterDialog.pack();
        filterDialog.setLocationRelativeTo(null);

        filterDialog.setVisible(true);
    }
    public void openLanguageDialog(Frame frame) {
        LanguageDialog languageDialog = new LanguageDialog(frame);

        languageDialog.setPreferredSize(new Dimension(500, 200));
        languageDialog.pack();
        languageDialog.setLocationRelativeTo(null);

        languageDialog.setVisible(true);
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

        int result = fileChooser.showOpenDialog(mainFrame);

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
        SpecifyRouteDialog specifyRoute = new SpecifyRouteDialog(mainFrame, route);

        specifyRoute.setVisible(true);

        return specifyRoute.getSpecifiedRoute();
    }

    public Font getDefaultFont(int size) {
        return new Font("Century Gothic",Font.PLAIN, size);
    }
    public Font getDefaultFont() {
        return getDefaultFont(16);
    }

    public void updateLanguage() {
        if (mainFrame.getContentPane() == mainPanel.getMainPanel()) {
            mainPanel.updateLanguage();
        } else if (mainFrame.getContentPane() == signInForm.getSignInPanel()) {
            signInForm.updateLanguage();
        } else if (mainFrame.getContentPane() == signUpForm.getSignUpPanel()) {
            signUpForm.updateLanguage();
        }
    }
}
