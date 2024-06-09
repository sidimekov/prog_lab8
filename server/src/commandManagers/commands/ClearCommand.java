package commandManagers.commands;

import commandManagers.RouteManager;
import enums.ReadModes;
import enums.ResponseStatus;
import network.Response;
import network.User;

import java.io.Serial;

public class ClearCommand extends Command {
    @Serial
    private static final long serialVersionUID = 5028559730072584091L;
    public static final String USAGE = "clear";
    public static final String DESC = "очистить коллекцию";

    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        boolean cleared = rm.clearUserObjects(sender.getId());
        if (cleared) {
            return new Response("Коллекция очищена", ResponseStatus.OK);
        } else {
            return new Response("Произошла ошибка при очищении вашей коллекции", ResponseStatus.SERVER_ERROR);
        }
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
