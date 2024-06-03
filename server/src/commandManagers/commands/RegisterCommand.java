package commandManagers.commands;

import database.DatabaseManager;
import enums.ReadModes;
import enums.ResponseStatus;
import network.Response;
import network.Server;
import network.User;

import java.io.Serial;

public class RegisterCommand extends Command{
    @Serial
    private static final long serialVersionUID = -4587848113556183126L;

    private String USAGE = "register <логин> <пароль> <подтверждение пароля>";
    private String DESC = "регистрация нового пользователя, автоматический вход после регистрации";


    @Override
    public Response execute(ReadModes readMode, String[] args) {

        if (readMode.equals(ReadModes.FILE)) return new Response("Невозможно выполнить команду из файла", ResponseStatus.CLIENT_ERROR);

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

                    Response response = new Response("Регистрация успешна", ResponseStatus.OK);

                    Server.getLogger().info("Зарегистрирован и авторизован пользователь " + user.getLogin());

                    response.setUser(user);
                    return response;
                } else {
                    return new Response("Указанный логин уже зарегистрирован", ResponseStatus.CLIENT_ERROR);
                }
            } else {
                return new Response("Пароли не совпадают!", ResponseStatus.CLIENT_ERROR);
            }

        } else {
            return new Response(String.format("Некорректные аргументы, использование: %s\n", USAGE), ResponseStatus.CLIENT_ERROR);
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
