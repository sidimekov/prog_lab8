import commandManagers.CommandInvoker;
import gui.GuiManager;


public class Main {

    public static void main(String[] args) {

        System.out.println("Авторизирутейсь (login <логин> <пароль>) или зарегистрируйтесь (register <логин> <пароль> <подтверждение пароля>)");

        GuiManager guiManager = GuiManager.getInstance();
        guiManager.run();

        CommandInvoker invoker = CommandInvoker.getInstance();
        invoker.listenCommands();
    }
}