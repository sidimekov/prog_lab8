package input;

import com.google.gson.*;
import commandManagers.RouteManager;
import entity.Route;
import exceptions.FailedJSONReadException;
import util.IdManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.PriorityQueue;

public class JSONManager {
    public static Route readElement(String jsonContent) throws FailedJSONReadException {
        Gson gson = new Gson();
        Route element = gson.fromJson(new StringReader(jsonContent), Route.class);
        JsonObject jsonObject = gson.fromJson(new StringReader(jsonContent), JsonObject.class);
        // если id изначально не было, то поменять тот 0, который дал gson, на автоматический
        JsonElement id = jsonObject.get("id");
        if (id == null) {
            element.setId(IdManager.getId());
        }
        return element;
    }

    @Deprecated
    public static String elementToJson(Route element) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(element);
        return json;
    }

    @Deprecated
    public static String collectionToJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        PriorityQueue<Route> collection = RouteManager.getInstance().getCollection();
        String json = gson.toJson(collection);
        return json;
    }

    @Deprecated
    public static PriorityQueue<Route> readCollection(String jsonContent) throws RuntimeException {
        Gson gson = new Gson();

        PriorityQueue<Route> collection = new PriorityQueue<>();
        JsonArray elements = gson.fromJson(new StringReader(jsonContent), JsonArray.class);

        if (elements == null) {
            return collection;
        } else {

            // сначала считываем те, у которых указан id, чтоб потом тем, кто без id нормально сгенерить
            for (int i = 0; i < elements.size(); i++) {
                JsonObject element = elements.get(i).getAsJsonObject();
                JsonElement id = element.get("id");
                Route route;
                if (id != null) {
                    route = gson.fromJson(element, Route.class);
                    route.setId(IdManager.getId());
                    collection.add(route);
                }
            }
            for (int i = 0; i < elements.size(); i++) {
                JsonObject element = elements.get(i).getAsJsonObject();
                JsonElement id = element.get("id");
                Route route;
                if (id == null) {
                    route = gson.fromJson(element, Route.class);
                    route.setId(IdManager.getId());
                    collection.add(route);
                }
            }
            return collection;
        }
    }

    @Deprecated
    public static PriorityQueue<Route> readServerCollection(String path) {

        String jsonContent = null;
        try {
            jsonContent = Files.readString(Paths.get(path), StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            System.out.printf("Ошибка при загрузке коллекции из файла. Не найден файл, %s\n", e.getMessage());
            return null;
        } catch (IOException e) {
            System.out.printf("Ошибка при загрузке коллекции из файла: %s\n", e.getStackTrace());
            return null;
        }

        return readCollection(jsonContent);
    }
}
