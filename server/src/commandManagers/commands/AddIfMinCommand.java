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
import java.io.Serial;

public class AddIfMinCommand extends Command {
    @Serial
    private static final long serialVersionUID = -2899451559481014307L;
    public static final String USAGE = "add_if_min ИЛИ add_if_min <элемент в формате .json>";
    public static final String DESC = "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции";

    private String jsonContent;
    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        Route minElement = rm.getMinElement();
        Route element;
        if (args.length == 0) {

            // если нет аргументов, то нужно построить из консоли, значит если файл то бан
            if (readMode == ReadModes.CONSOLE) {
                try {
                    BufferedReader reader = InputManager.getConsoleReader();
                    element = RouteManager.buildNew(reader); // если с консоли
                } catch (IOException e) {
                    return new Response(String.format("Ошибка при добавлении в коллекцию: %s\n", e.getMessage()));
                }
            } else {
                return new Response(String.format("Ошибка в использовании аргументов. Использование: %s", USAGE));
            }
        } else {
            if (jsonContent != null) {
                try {
                    element = JSONManager.readElement(jsonContent);
                    jsonContent = null;
                } catch (FailedValidationException | FailedJSONReadException e) {
                    return new Response(String.format("Ошибка при добавлении в коллекцию: %s\n", e.getMessage()));
                }
            } else {
                return new Response("Файл не найден / был пуст");
            }
        }


        if (minElement == null || element.compareTo(minElement) < 0) {
            rm.addElement(element, true);
            return new Response("Минимальный элемент добавлен в коллекцию");
        } else {
            return new Response("Указанный элемент не будет самым минимальным");
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
}
