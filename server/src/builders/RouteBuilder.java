package builders;

import entity.Coordinates;
import entity.LocationFrom;
import entity.LocationTo;
import entity.Route;
import network.BuildRequest;
import network.Response;
import network.Server;

import java.io.BufferedReader;
import java.io.IOException;

public class RouteBuilder {
    public static Route build(BufferedReader reader, boolean withId) throws IOException {

        Response serverResponse;
        Response clientResponse;

        Server server = Server.getInstance();

//        serverResponse = new Response("Создание маршрута...");
//        server.sendResponse(serverResponse);


        long id = 0;
        if (withId) {
            while (true) {
                BuildRequest buildRequest = new BuildRequest("Введите id маршрута (long, больше 0) > ");
                clientResponse = server.sendResponse(new Response(buildRequest));
                String responseString = clientResponse.getMessage();

                try {
                    id = Long.parseLong(responseString);
                } catch (NumberFormatException e) {
                    continue;
                }
                if (id > 0) break;
            }
        }

        String name;
        do {
            BuildRequest buildRequest = new BuildRequest("Введите имя маршрута (String, не null, строка не может быть пустой) > ");

            clientResponse = server.sendResponse(new Response(buildRequest));

            name = clientResponse.getMessage();

        } while (!Route.checkName(name));

        Coordinates coordinates = CoordinatesBuilder.build(reader);

        LocationFrom locFrom = LocationFromBuilder.build(reader);

        LocationTo locTo = LocationToBuilder.build(reader);

        double distance;
        while (true) {
            BuildRequest buildRequest = new BuildRequest("Введите дистанцию маршрута (double, больше 1) > ");

            clientResponse = server.sendResponse(new Response(buildRequest));

            try {
                distance = Double.parseDouble(clientResponse.getMessage());
            } catch (NumberFormatException e) {
                continue;
            }
            if (Route.checkDistance(distance)) break;
        }

//        serverResponse = new Response("Маршрут настроен");
//        server.sendResponse(serverResponse);

        Route route;
        if (withId) {
            route = new Route(id, name, coordinates, locFrom, locTo, distance);
        } else {
            route = new Route(name, coordinates, locFrom, locTo, distance);
        }
        return route;
    }
}
