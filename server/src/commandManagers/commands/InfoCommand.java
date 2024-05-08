package commandManagers.commands;

import commandManagers.RouteManager;
import entity.Route;
import enums.ReadModes;
import network.Response;

import java.util.PriorityQueue;

public class InfoCommand extends Command {
    private static String USAGE = "info";
    private static String DESC = "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";

    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        PriorityQueue<Route> collection = rm.getCollection();
        String response = "";
        response = response + String.format("Тип коллекции: %s\n", collection.getClass().getName());
        response = response + String.format("Дата создания: %s\n", rm.getInitializationDate());
        response = response + String.format("Количество элементов: %s\n", collection.size());

        return new Response(response);
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
