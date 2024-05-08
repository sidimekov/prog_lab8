package builders;

import entity.LocationTo;
import network.BuildRequest;
import network.Response;
import network.Server;

import java.io.BufferedReader;
import java.io.IOException;

public class LocationToBuilder {
    public static LocationTo build(BufferedReader reader) throws IOException {

        Response serverResponse;
        Response clientResponse;

        Server server = Server.getInstance();

//        serverResponse = new Response("Настройка окончательной локации...");
//        server.sendResponse(serverResponse);

        String name;
        do {
            BuildRequest buildRequest = new BuildRequest("Введите имя окончательной локации (String, не null, длина не больше 443) > ");

            clientResponse = server.sendResponse(new Response(buildRequest));

            name = clientResponse.getMessage();
        } while (!LocationTo.checkName(name));

        float x;
        while (true) {
            BuildRequest buildRequest = new BuildRequest("Введите x (float) > ");

            clientResponse = server.sendResponse(new Response(buildRequest));

            try {
                x = Float.parseFloat(clientResponse.getMessage());
            } catch (NumberFormatException e) {
                continue;
            }
            if (LocationTo.checkX(x)) break;
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
        } while (!LocationTo.checkY(y));

        long z;
        while (true) {
            BuildRequest buildRequest = new BuildRequest("Введите z (long) > ");

            clientResponse = server.sendResponse(new Response(buildRequest));

            try {
                z = Long.parseLong(clientResponse.getMessage());
            } catch (NumberFormatException e) {
                continue;
            }
            if (LocationTo.checkZ(z)) break;
        }

//        serverResponse = new Response("Окончательная локация настроена");
//        server.sendResponse(serverResponse);

        return new LocationTo(name, x, y, z);
    }
}
