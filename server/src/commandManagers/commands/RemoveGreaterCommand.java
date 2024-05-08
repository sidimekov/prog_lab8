package commandManagers.commands;

import commandManagers.RouteManager;
import entity.Route;
import enums.ReadModes;
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
    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        Route inpElement;
        if (args.length == 0) {
            try {
                BufferedReader reader = InputManager.getConsoleReader();
                inpElement = RouteManager.buildNew(reader); // если с консоли
            } catch (IOException e) {
                return new Response(e.getMessage());
            }
        } else {
            if (jsonContent != null) {
                try {
                    inpElement = JSONManager.readElement(jsonContent);
                    jsonContent = null;
                } catch (FailedValidationException | FailedJSONReadException e) {
                    return new Response(e.getMessage());
                }
            } else {
                return new Response("Файл не найден / был пуст");
            }
        }

        rm.getCollection()
                .stream()
                .filter(element -> (element.compareTo(inpElement) > 0))
                .forEach(element -> rm.removeElement(element.getId()));
        return new Response("Все элементы, превосходящие введённый, удалены");
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
}
