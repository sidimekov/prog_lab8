package commandManagers.commands;

import commandManagers.RouteManager;
import enums.ReadModes;
import enums.ResponseStatus;
import network.Response;
import network.Server;

public class RemoveAllByDistanceCommand extends Command {
    private final String USAGE = "remove_all_by_distance <дистанция(double)>";
    private final String DESC = "удалить из коллекции все элементы, значение поля distance которого эквивалентно заданному";


    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        if (args.length == 1) {
            double distance;
            try {
                distance = Double.parseDouble(args[0]);
                try {
                    rm.removeAllByDistance(distance, sender.getId());
                } catch (RuntimeException e) {
                    Server.getLogger().warning("Не удалось удалить объекты с указанной дистанцией");
                    return  new Response("Не удалось удалить объекты с указанной дистанцией", ResponseStatus.SERVER_ERROR);
                }
                return new Response(String.format("Все ваши элементы с дистанцией %s удалены\n", distance), ResponseStatus.OK);
            } catch (NumberFormatException e) {
                return new Response("Некорректная дистанция", ResponseStatus.CLIENT_ERROR);
            }
        } else {
            return new Response(String.format("Некорректные аргументы, использование: %s\n", USAGE), ResponseStatus.CLIENT_ERROR);
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
