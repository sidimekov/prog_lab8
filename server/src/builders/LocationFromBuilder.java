package builders;

import entity.LocationFrom;
import network.BuildRequest;
import network.Response;
import network.Server;

import java.io.BufferedReader;
import java.io.IOException;

public class LocationFromBuilder {
    public static LocationFrom build(BufferedReader reader) throws IOException {

        Response serverResponse;
        Response clientResponse;

        Server server = Server.getInstance();

//        serverResponse = new Response("Настройка изначальной локации...");
//        server.sendResponse(serverResponse);

        int x;
        while (true) {
            BuildRequest buildRequest = new BuildRequest("Введите x (int) > ");

            clientResponse = server.sendResponse(new Response(buildRequest));

            try {
                x = Integer.parseInt(clientResponse.getMessage());
            } catch (NumberFormatException e) {
                continue;
            }
            if (LocationFrom.checkX(x)) break;
        }

        Integer y = null;
        do {
            BuildRequest buildRequest = new BuildRequest("Введите y (Integer, не null) > ");

            clientResponse = server.sendResponse(new Response(buildRequest));

            try {
                y = Integer.parseInt(clientResponse.getMessage());
            } catch (NumberFormatException e) {
                continue;
            }
        } while (!LocationFrom.checkY(y));

        float z;
        while (true) {
            BuildRequest buildRequest = new BuildRequest("Введите z (float) > ");

            clientResponse = server.sendResponse(new Response(buildRequest));

            try {
                z = Float.parseFloat(clientResponse.getMessage());
            } catch (NumberFormatException e) {
                continue;
            }
            if (LocationFrom.checkZ(z)) break;
        }

//        serverResponse = new Response("Изначальная локация настроена");
//        server.sendResponse(serverResponse);

        return new LocationFrom(x, y, z);
    }
}
