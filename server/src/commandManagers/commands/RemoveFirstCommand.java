package commandManagers.commands;

import commandManagers.RouteManager;
import enums.ReadModes;
import enums.ResponseStatus;
import exceptions.NoAccessToObjectException;
import network.Response;

public class RemoveFirstCommand extends Command {
    public static final String USAGE = "remove_first";
    public static final String DESC = "удалить первый элемент из коллекции";

    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        if (!rm.getDBCollection(sender.getId()).isEmpty()) {
            try {
                boolean removed = rm.removeFirst(sender.getId());
                if (removed) {
                    return new Response("Первый элемент коллекции удалён", ResponseStatus.OK);
                } else {
                    return new Response("Не удалось удалить первый элемент", ResponseStatus.CLIENT_ERROR);
                }
            } catch (NoAccessToObjectException e) {
                return new Response("Нет доступа к элементу", ResponseStatus.CLIENT_ERROR);
            }
        } else {
            return new Response("Уже нет элементов, которыми вы владеете!", ResponseStatus.CLIENT_ERROR);
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
