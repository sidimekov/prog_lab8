import commandManagers.CommandInvoker;

public class Main {

    public static void main(String[] args) {

        System.out.println("Авторизирутейсь (login <логин> <пароль>) или зарегистрируйтесь (register <логин> <пароль> <подтверждение пароля>)");

        CommandInvoker invoker = CommandInvoker.getInstance();
        invoker.listenCommands();
    }
}