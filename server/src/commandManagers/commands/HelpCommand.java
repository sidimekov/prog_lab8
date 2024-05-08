package commandManagers.commands;

import commandManagers.CommandInvoker;
import enums.ReadModes;
import network.Response;

import java.io.Serial;
import java.util.Map;

public class HelpCommand extends Command {
    @Serial
    private static final long serialVersionUID = 3861448579965821620L;
    private static String USAGE = "help";
    private static String DESC = "вывести справку по доступным командам";

    @Override
    public Response execute(ReadModes readMode, String[] args) {
        Map<String, Command> commands = CommandInvoker.getInstance().getCommands();

        StringBuilder response = new StringBuilder();
        response.append("exit - Выход из программы\n");

        for (Command command : commands.values()) {
            response.append(String.format("%s - %s\n", command.getUsage(), command.getDesc()));
        }

        return new Response(response.toString());
    }


    @Override
    public String getDesc() {
        return DESC;
    }

    @Override
    public String getUsage() {
        return USAGE;
    }
}
