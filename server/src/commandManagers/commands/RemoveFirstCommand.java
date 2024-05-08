package commandManagers.commands;

import commandManagers.RouteManager;
import enums.ReadModes;
import network.Response;

public class RemoveFirstCommand extends Command {
    public static final String USAGE = "remove_first";
    public static final String DESC = "удалить первый элемент из коллекции";

    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        if (rm.getCollection().size() > 0) {
            rm.getCollection().remove();
            return new Response("Первый элемент коллекции удалён");
        } else {
            return new Response("Коллекция уже пуста!");
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
