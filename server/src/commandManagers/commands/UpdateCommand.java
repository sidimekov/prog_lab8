package commandManagers.commands;

import commandManagers.RouteManager;
import entity.Route;
import enums.ReadModes;
import enums.ResponseStatus;
import exceptions.FailedJSONReadException;
import exceptions.FailedValidationException;
import exceptions.NoAccessToObjectException;
import input.JSONManager;
import network.Response;
import network.Server;
import util.InputManager;

import java.io.BufferedReader;
import java.io.IOException;

public class UpdateCommand extends Command {

    private static String USAGE = "update ИЛИ update <элемент в формате .json>";
    private static String DESC = "обновить значение элемента коллекции, id которого равен заданному";

    private String jsonContent;
    private Route routeToUpdate;

    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        long updated;
        if (args.length == 0) {
            // если нет аргументов, то нужно построить из консоли, значит если файл то бан

            switch (readMode) {
                case CONSOLE -> {
                    try {
                        BufferedReader reader = InputManager.getConsoleReader();
                        Route element = RouteManager.buildNew(reader, true);
                        updated = rm.update(element, sender.getId()); // если с консоли, уже отвалидировано
                    } catch (IOException e) {
                        Server.getLogger().warning(e.getMessage());
                        return new Response(e.getMessage(), ResponseStatus.SERVER_ERROR);
                    } catch (NoAccessToObjectException e) {
                        return new Response("Нет доступа к этому элементу", ResponseStatus.CLIENT_ERROR);
                    }

                    if (updated == -1) {
                        return new Response("Элемент не удалось обновить. Возможно не найден указанный id элемента", ResponseStatus.SERVER_ERROR);
                    } else {
                        return new Response("Элемент обновлен", ResponseStatus.OK);
                    }
                }
                case APP -> {
                    Route element = routeToUpdate;
                    try {
                        updated = rm.update(element, sender.getId());
                    } catch (NoAccessToObjectException e) {
                        return new Response("Нет доступа к этому элементу", ResponseStatus.CLIENT_ERROR);
                    }

                    if (updated == -1) {
                        return new Response("Элемент не удалось обновить. Возможно не найден указанный id элемента", ResponseStatus.CLIENT_ERROR);
                    } else {
                        return new Response("Элемент обновлен", ResponseStatus.OK);
                    }
                }
                case FILE -> {
                    return new Response(String.format("Некорректные аргументы, использование: %s\n", USAGE), ResponseStatus.CLIENT_ERROR);
                }
            }
        } else {
            // из файла .json
            if (jsonContent != null) {
                try {
                    Route element = JSONManager.readElement(jsonContent);
                    jsonContent = null;
                    updated = rm.update(element, sender.getId());
                } catch (FailedValidationException | FailedJSONReadException e) {
                    return new Response(e.getMessage(), ResponseStatus.SERVER_ERROR);
                } catch (NoAccessToObjectException e) {
                    return new Response("Нет доступа к этому элементу", ResponseStatus.CLIENT_ERROR);
                }
            } else {
                return new Response("Файл не найден / был пуст", ResponseStatus.CLIENT_ERROR);
            }

            if (updated == -1) {
                return new Response("Во время обновления произошла ошибка. Возможно не найден указанный id элемента", ResponseStatus.CLIENT_ERROR);
            } else {
                return new Response("Элемент обновлен", ResponseStatus.OK);
            }
        }
        return new Response("not planned response", ResponseStatus.SERVER_ERROR);
    }

    public String getJsonContent() {
        return jsonContent;
    }

    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }

    public Route getRouteToUpdate() {
        return routeToUpdate;
    }

    public void setRouteToUpdate(Route routeToUpdate) {
        this.routeToUpdate = routeToUpdate;
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
