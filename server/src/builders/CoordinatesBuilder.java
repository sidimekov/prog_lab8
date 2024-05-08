package builders;

import entity.Coordinates;
import network.BuildRequest;
import network.Response;
import network.Server;

import java.io.BufferedReader;
import java.io.IOException;

public class CoordinatesBuilder {
    public static Coordinates build(BufferedReader reader) throws IOException {

        Response serverResponse;
        Response clientResponse;

        Server server = Server.getInstance();

//        serverResponse = new Response("Настройка координат...");
//        server.sendResponse(serverResponse);

        double x;
        while (true) {
            BuildRequest buildRequest = new BuildRequest("Введите x (double, макс. 790) > ");

            clientResponse = server.sendResponse(new Response(buildRequest));

            try {
                x = Double.parseDouble(clientResponse.getMessage());
            } catch (NumberFormatException e) {
                continue;
            }
            if (Coordinates.checkX(x)) break;
        }

        Integer y = null;
        do {
            BuildRequest buildRequest = new BuildRequest("Введите y (Integer, не null, больше -858) > ");

            clientResponse = server.sendResponse(new Response(buildRequest));

            try {
                y = Integer.parseInt(clientResponse.getMessage());
            } catch (NumberFormatException e) {
                continue;
            }
        } while (!Coordinates.checkY(y));

//        serverResponse = new Response("Координаты настроены");
//        server.sendResponse(serverResponse);

        return new Coordinates(x, y);
    }
}
