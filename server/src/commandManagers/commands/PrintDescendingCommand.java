package commandManagers.commands;

import commandManagers.RouteManager;
import enums.ReadModes;
import network.Response;

public class PrintDescendingCommand extends Command {
    private final String USAGE = "print_descending";
    private final String DESC = "вывести элементы коллекции в порядке убывания";

    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        String response = rm.returnDescending(sender.getId());
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
