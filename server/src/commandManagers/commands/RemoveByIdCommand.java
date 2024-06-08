package commandManagers.commands;

import commandManagers.RouteManager;
import enums.ReadModes;
import enums.ResponseStatus;
import exceptions.NoAccessToObjectException;
import network.Response;
import network.Server;

public class RemoveByIdCommand extends Command {
    public static final String USAGE = "remove_by_id <id>";
    public static final String DESC = "удалить элемент из коллекции по его id";

    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        boolean removed;
        if (args.length == 1) {
            long id;
            try {
                id = Long.parseLong(args[0]);
            } catch (NumberFormatException e) {
                return new Response(String.format("Некорректные аргументы, используйте: %s\n", USAGE), ResponseStatus.CLIENT_ERROR);
            }
            if (rm.hasElement(id)) {
                try {
                    removed = rm.removeElement(id, sender.getId());
                } catch (NoAccessToObjectException e) {
                    Server.getLogger().warning(e.getMessage());
                    return new Response("Нет доступа к объекту с таким id", ResponseStatus.CLIENT_ERROR);
                }
            } else {
                return new Response("Нет элемента с таким id", ResponseStatus.CLIENT_ERROR);
            }
        } else {
            return new Response(String.format("Некорректные аргументы, используйте: %s\n", USAGE), ResponseStatus.CLIENT_ERROR);
        }
        if (removed) {
            return new Response("Элемент удалён", ResponseStatus.OK);
        } else {
            return new Response("Не удалось удалить элемент", ResponseStatus.SERVER_ERROR);
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
