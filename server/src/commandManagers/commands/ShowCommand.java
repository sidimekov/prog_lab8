package commandManagers.commands;

import commandManagers.RouteManager;
import entity.Route;
import enums.ReadModes;
import enums.ResponseStatus;
import network.MessageRequest;
import network.Response;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;

public class ShowCommand extends Command {
    private static String USAGE = "show";
    private static String DESC = "вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    private static final String[] COLUMN_NAMES = {"id",
            "route_name",
            "creation_date",
            "coordinates_x",
            "coordinates_y",
            "from_x",
            "from_y",
            "from_z",
            "to_name",
            "to_x",
            "to_y",
            "to_z",
            "distance"};

    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        PriorityQueue<Route> collection = rm.getDBCollection();
        if (readMode != ReadModes.APP) {
            String response = RouteManager.returnCollection(collection);
            return new Response(response);
        } else {
            TableModel model = new DefaultTableModel(rm.getTableData(), COLUMN_NAMES);
            return new Response(model, ResponseStatus.OK);
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
