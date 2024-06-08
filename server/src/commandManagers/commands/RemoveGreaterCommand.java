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

public class RemoveGreaterCommand extends Command {
    public static final String USAGE = "remove_greater ИЛИ remove_greater <элемент в формате .json>";
    public static final String DESC = "удалить из коллекции все элементы, превышающие заданный";

    private String jsonContent;
    private Route routeToCompare;

    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        Route inpElement = null;
        if (args.length == 0) {
            switch (readMode) {
                case CONSOLE -> {
                    try {
                        BufferedReader reader = InputManager.getConsoleReader();
                        inpElement = RouteManager.buildNew(reader); // если с консоли
                    } catch (IOException e) {
                        return new Response(e.getMessage(), ResponseStatus.SERVER_ERROR);
                    }
                }
                case FILE -> {
                    return new Response(String.format("Ошибка в использовании аргументов. Использование: %s", USAGE), ResponseStatus.CLIENT_ERROR);
                }
                case APP -> inpElement = routeToCompare;
            }
        } else {
            switch (readMode) {
                case FILE -> {
                    if (jsonContent != null) {
                        try {
                            inpElement = JSONManager.readElement(jsonContent);
                            jsonContent = null;
                        } catch (FailedValidationException | FailedJSONReadException e) {
                            return new Response(e.getMessage(), ResponseStatus.CLIENT_ERROR);
                        }
                    } else {
                        return new Response("Файл не найден / был пуст", ResponseStatus.CLIENT_ERROR);
                    }
                }
                case APP, CONSOLE -> {
                    return new Response(String.format("Ошибка в использовании аргументов. Использование: %s", USAGE), ResponseStatus.CLIENT_ERROR);
                }
            }
        }

        rm.removeGreater(inpElement, sender.getId());

        return new Response("Все ваши элементы, превосходящие введённый, удалены", ResponseStatus.OK);
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

    public Route getRouteToCompare() {
        return routeToCompare;
    }

    public void setRouteToCompare(Route routeToCompare) {
        this.routeToCompare = routeToCompare;
    }
}
