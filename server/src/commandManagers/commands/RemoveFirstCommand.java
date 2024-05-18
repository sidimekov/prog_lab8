package commandManagers.commands;

import commandManagers.RouteManager;
import enums.ReadModes;
import exceptions.NoAccessToObjectException;
import network.Response;

public class RemoveFirstCommand extends Command {
    public static final String USAGE = "remove_first";
    public static final String DESC = "удалить первый элемент из коллекции";

    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        if (!rm.getCollection().isEmpty()) {
            try {
                boolean removed = rm.removeFirst(sender.getId());
                if (removed) {
                    return new Response("Первый элемент коллекции удалён");
                } else {
                    return new Response("Не удалось удалить первый элемент, возможно у вас нет доступа к нему");
                }
            } catch (NoAccessToObjectException e) {
                return new Response("Нет доступа к элементу");
            }
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
