package commandManagers.commands;

import database.DatabaseManager;
import enums.ReadModes;
import network.Response;
import network.User;

import java.io.Serial;

public class LoginCommand extends Command {
    @Serial
    private static final long serialVersionUID = 7292514316253682659L;

    private String USAGE = "login <логин> <пароль>";
    private String DESC = "авторизация пользователя";


    @Override
    public Response execute(ReadModes readMode, String[] args) {

        if (readMode.equals(ReadModes.FILE)) return new Response("Невозможно выполнить команду из файла");

        Response response;

        if (args.length == 2) {

            String login = args[0];
            String password = args[1];

            DatabaseManager dbManager = new DatabaseManager();

            User user = new User(login, password);

            if (dbManager.checkUser(user) != -1) {
                long userId = dbManager.checkUser(user);
//                System.out.println(userId);
                user.setId(userId);
                response = new Response("Авторизация успешна");
                response.setUser(user);
                return response;
            } else {
                return new Response("Неверный логин или пароль");
            }

        } else {
            return new Response(String.format("Некорректные аргументы, использование: %s\n", USAGE));
        }
    }

    @Override
    public String getUsage() {
        return USAGE;
    }

    @Override
    public String getDesc() {
        return DESC;
    }
}
