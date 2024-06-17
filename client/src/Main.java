import util.CommandInvoker;
import gui.GuiManager;


public class Main {

    public static void main(String[] args) {

        CommandInvoker.getInstance();

        GuiManager guiManager = GuiManager.getInstance();
        guiManager.run();

    }
}