package commandManagers.commands;

import database.DatabaseManager;
import enums.ReadModes;
import network.Response;
import network.User;

import java.io.Serial;

public class RegisterCommand extends Command{
    @Serial
    private static final long serialVersionUID = -4587848113556183126L;

    private String USAGE = "register <логин> <пароль> <подтверждение пароля>";
    private String DESC = "регистрация нового пользователя, автоматический вход после регистрации";


    @Override
    public Response execute(ReadModes readMode, String[] args) {

        if (args.length == 3) {
            String login = args[0];
            String password = args[1];
            String checkPassword = args[2];

            DatabaseManager dbManager = new DatabaseManager();

            if (password.equals(checkPassword)) {

                User user = new User(login, password);

                if (dbManager.checkRegister(login)) {

                    long userId = dbManager.addUser(user);
                    user.setId(userId);

                    Response response = new Response("Регистрация успешна");
                    response.setUser(user);
                    return response;
                } else {
                    return new Response("Указанный логин уже зарегистрирован");
                }
            } else {
                return new Response("Пароли не совпадают!");
            }

        } else {
            return new Response(String.format("Некорректные аргументы, использование: %s\n", USAGE));
        }
    }

    @Override
    public String getUsage() {
        return super.getUsage();
    }

    @Override
    public String getDesc() {
        return super.getDesc();
    }
}
