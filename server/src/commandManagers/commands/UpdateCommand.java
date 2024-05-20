package commandManagers.commands;

import commandManagers.RouteManager;
import entity.Route;
import enums.ReadModes;
import exceptions.FailedJSONReadException;
import exceptions.FailedValidationException;
import exceptions.NoAccessToObjectException;
import input.JSONManager;
import network.Response;
import util.InputManager;

import java.io.BufferedReader;
import java.io.IOException;

public class UpdateCommand extends Command {

    private static String USAGE = "update ИЛИ update <элемент в формате .json>";
    private static String DESC = "обновить значение элемента коллекции, id которого равен заданному";

    private String jsonContent;

    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        long updated;
        if (args.length == 0) {
            // если нет аргументов, то нужно построить из консоли, значит если файл то бан

            if (readMode == ReadModes.CONSOLE) {
                try {
                    BufferedReader reader = InputManager.getConsoleReader();
                    Route element = RouteManager.buildNew(reader, true);
                    updated = rm.update(element, sender.getId()); // если с консоли, уже отвалидировано
                } catch (IOException e) {
                    return new Response(e.getMessage());
                } catch (NoAccessToObjectException e) {
                    return new Response("Нет доступа к этому элементу");
                }

                if (updated == -1) {
                    return new Response("Во время обновления произошла ошибка. Возможно не найден указанный id элемента");
                } else {
                    return new Response("Элемент обновлен");
                }

            } else {
                return new Response(String.format("Некорректные аргументы, использование: %s\n", USAGE));
            }
        } else {
            // из файла .json
            if (jsonContent != null) {
                try {
                    Route element = JSONManager.readElement(jsonContent);
                    jsonContent = null;
                    updated = rm.update(element, sender.getId());
                } catch (FailedValidationException | FailedJSONReadException e) {
                    return new Response(e.getMessage());
                } catch (NoAccessToObjectException e) {
                    return new Response("Нет доступа к этому элементу");
                }
            } else {
                return new Response("Файл не найден / был пуст");
            }

            if (updated == -1) {
                return new Response("Во время обновления произошла ошибка. Возможно не найден указанный id элемента");
            } else {
                return new Response("Элемент обновлен");
            }
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
