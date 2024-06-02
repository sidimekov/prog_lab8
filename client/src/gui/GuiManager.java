package gui;

import java.util.ResourceBundle;

public class GuiManager {
    private static GuiManager instance;
    private static ResourceBundle enProperties = ResourceBundle.getBundle("../resources/en_EN.properties");

    private GuiManager() {
        instance = this;
    }

    public static GuiManager getInstance() {
        if (instance == null) {
            instance = new GuiManager();
        }
        return instance;
    }

    public void run() {

    }
}
