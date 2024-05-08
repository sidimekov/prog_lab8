package commandManagers.commands;

import commandManagers.RouteManager;
import entity.Route;
import enums.ReadModes;
import network.MessageRequest;
import network.Response;

import java.util.PriorityQueue;

public class ShowCommand extends Command {
    private static String USAGE = "show";
    private static String DESC = "вывести в стандартный поток вывода все элементы коллекции в строковом представлении";

    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        PriorityQueue<Route> collection = rm.getCollection();
        String response = RouteManager.returnCollection(collection);
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
