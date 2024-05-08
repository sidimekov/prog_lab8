package commandManagers.commands;

import commandManagers.RouteManager;
import enums.ReadModes;
import network.Response;

import java.io.Serial;

public class ClearCommand extends Command {
    @Serial
    private static final long serialVersionUID = 5028559730072584091L;
    public static final String USAGE = "clear";
    public static final String DESC = "очистить коллекцию";

    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        rm.getCollection().clear();
        return new Response("Коллекция очищена");
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
