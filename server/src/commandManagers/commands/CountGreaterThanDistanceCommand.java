package commandManagers.commands;

import commandManagers.RouteManager;
import enums.ReadModes;
import enums.ResponseStatus;
import network.Response;

public class CountGreaterThanDistanceCommand extends Command {
    private final String USAGE = "count_greater_than_distance <дистанция(double>";
    private final String DESC = "вывести количество элементов, значение поля distance которых больше заданного";

    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        if (args.length == 1) {
            double distance;
            try {
                distance = Double.parseDouble(args[0]);
                return new Response(String.format("Количество элементов с дистанцией выше введённой: %s\n", rm.countGreaterThanDistance(distance, sender.getId())), ResponseStatus.OK);
            } catch (NumberFormatException e) {
                return new Response(String.format("Некорректные аргументы: использование: %s\n", USAGE), ResponseStatus.CLIENT_ERROR);
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
