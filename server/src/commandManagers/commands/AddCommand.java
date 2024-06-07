package commandManagers.commands;

import commandManagers.RouteManager;
import entity.Route;
import enums.ReadModes;
import enums.ResponseStatus;
import exceptions.FailedJSONReadException;
import exceptions.FailedValidationException;
import input.JSONManager;
import network.Response;
import util.InputManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serial;

public class AddCommand extends Command {
    @Serial
    private static final long serialVersionUID = -2914911397015071577L;
    private String USAGE = "add (только в консоли) ИЛИ add <элемент в формате .json>";
    private String DESC = "добавить новый элемент в коллекцию";

    private String jsonContent;
    private Route routeToAdd;
//    private long userId;

    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        if (args.length == 0) {
            // если нет аргументов, то нужно построить из консоли, значит если файл то бан
            switch (readMode) {
                case CONSOLE -> {
                    try {
                        BufferedReader reader = InputManager.getConsoleReader();

                        Route element = RouteManager.buildNew(reader); // если с консоли

                        rm.addElement(element, sender.getId(), true);

                    } catch (IOException e) {
                        return new Response(String.format("Ошибка при добавлении в коллекцию: %s\n", e.getMessage()), ResponseStatus.SERVER_ERROR);
                    }
                }
                case APP -> {
                    if (routeToAdd != null) {
                        RouteManager.getInstance().addElement(routeToAdd, sender.getId());
                        routeToAdd = null;
                    } else {
                        return new Response("Не удалось получить объект для добавления", ResponseStatus.CLIENT_ERROR);
                    }
                }
                case FILE -> {
                    return new Response(String.format("Ошибка в использовании аргументов. Использование: %s", USAGE), ResponseStatus.CLIENT_ERROR);
                }
            }
        } else {
            if (readMode == ReadModes.FILE) {
                // из файла .json
                if (jsonContent != null) {
                    try {
                        Route element = JSONManager.readElement(jsonContent);
                        jsonContent = null;
                        RouteManager.getInstance().addElement(element, sender.getId());
                    } catch (FailedValidationException | FailedJSONReadException e) {
//                    e.printStackTrace();
                        return new Response(String.format("Ошибка при добавлении в коллекцию: %s\n", e.getMessage()), ResponseStatus.SERVER_ERROR);
                    }
                } else {
                    return new Response("Файл не найден / был пуст", ResponseStatus.CLIENT_ERROR);
                }
            }
        }
        return new Response("Добавлен элемент в коллекцию", ResponseStatus.OK);
    }

    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }

    public String getJsonContent() {
        return jsonContent;
    }

    public Route getRouteToAdd() {
        return routeToAdd;
    }

    public void setRouteToAdd(Route routeToAdd) {
        this.routeToAdd = routeToAdd;
    }

    @Override
    public String getUsage() {
        return USAGE;
    }

    @Override
    public String getDesc() {
        return DESC;
    }
}
