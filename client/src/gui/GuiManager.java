package gui;

import entity.Route;
import gui.forms.*;
import network.Client;
import network.User;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

public class GuiManager {
    private static GuiManager instance;
    private final MainFrame mainFrame;
    private final VisualizationForm visualizationForm;
    private SignInPane signInForm;
    private SignUpPane signUpForm;
    private MainPane mainPanel;
    private ResourceBundle resourceBundle;
    private Locale locale;
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

    public void openSingUpPanel() {
//        frame.add()
        mainFrame.setContentPane(signUpForm.getSignUpPanel());
        mainFrame.setSize(1024, 512);
        mainFrame.pack();
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
    }

    public void openMainPanel() {
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

    public MainPane getMainPanel() {
        return mainPanel;
    }

    public void openAddDialog() {
        AddDialog addDialog = new AddDialog(mainFrame);

        addDialog.setPreferredSize(new Dimension(800, 500));
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

    public Color randomColor() {
        Random rand = new Random();
        float r = 0.1f + rand.nextFloat() * 0.8f;
        float g = 0.1f + rand.nextFloat() * 0.8f;
        float b = 0.1f + rand.nextFloat() * 0.8f;
        return new Color(r,g,b);
    }

}
