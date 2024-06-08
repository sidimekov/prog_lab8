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

public class AddIfMinCommand extends Command {
    @Serial
    private static final long serialVersionUID = -2899451559481014307L;
    public static final String USAGE = "add_if_min ИЛИ add_if_min <элемент в формате .json>";
    public static final String DESC = "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции";

    private String jsonContent;
    private Route routeToAdd;

    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        Route minElement = rm.getMinElement(sender.getId());
        Route element = null;
        if (args.length == 0) {
            switch (readMode) {
                case CONSOLE -> {
                    try {
                        BufferedReader reader = InputManager.getConsoleReader();
                        element = RouteManager.buildNew(reader); // если с консоли
                    } catch (IOException e) {
                        return new Response(String.format("Ошибка при добавлении в коллекцию: %s\n", e.getMessage()), ResponseStatus.SERVER_ERROR);
                    }
                }
                case FILE -> {
                    return new Response(String.format("Ошибка в использовании аргументов. Использование: %s", USAGE), ResponseStatus.CLIENT_ERROR);
                }
                case APP -> {
                    if (routeToAdd != null) {
                        element = routeToAdd;
                        routeToAdd = null;
                    } else {
                        return new Response("Не удалось получить объект для добавления", ResponseStatus.CLIENT_ERROR);
                    }
                }
            }
        } else {
            if (readMode == ReadModes.FILE) {
                if (jsonContent != null) {
                    try {
                        element = JSONManager.readElement(jsonContent);
                        jsonContent = null;
                    } catch (FailedValidationException | FailedJSONReadException e) {
                        return new Response(String.format("Ошибка при добавлении в коллекцию: %s\n", e.getMessage()), ResponseStatus.SERVER_ERROR);
                    }
                } else {
                    return new Response("Файл не найден / был пуст", ResponseStatus.CLIENT_ERROR);
                }
            } else {
                return new Response(String.format("Ошибка в использовании аргументов. Использование: %s", USAGE), ResponseStatus.CLIENT_ERROR);
            }
        }


        if (minElement == null || element.compareTo(minElement) < 0) {
            rm.addElement(element, sender.getId(), true);
            return new Response("Минимальный элемент добавлен в коллекцию",ResponseStatus.OK);
        } else {
            return new Response("Указанный элемент не будет самым минимальным", ResponseStatus.CLIENT_ERROR);
        }
    }

    public String getJsonContent() {
        return jsonContent;
    }

    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }

    @Override
    public String getDesc() {
        return DESC;
    }

    @Override
    public String getUsage() {
        return USAGE;
    }

    public Route getRouteToAdd() {
        return routeToAdd;
    }

    public void setRouteToAdd(Route routeToAdd) {
        this.routeToAdd = routeToAdd;
    }
}
