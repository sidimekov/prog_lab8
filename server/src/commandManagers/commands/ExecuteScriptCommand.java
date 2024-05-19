package commandManagers.commands;

import commandManagers.CommandInvoker;
import enums.ReadModes;
import network.CommandRequest;
import network.Response;
import network.Server;
import util.InputManager;

import java.io.*;
import java.util.StringJoiner;

public class ExecuteScriptCommand extends Command {
    public static final String USAGE = "execute_script <имя файла>";
    public static final String DESC = "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.";

    private String script;
    @Override
    public Response execute(ReadModes readMode, String[] args) {
        Server server = Server.getInstance();
        if (args.length == 1) {
            if (script != null) {
                try (BufferedReader reader = new BufferedReader(new StringReader(script))) {
                    CommandInvoker invoker = CommandInvoker.getInstance();

                    StringJoiner totalResponse = new StringJoiner("\n");

                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (readMode == ReadModes.CONSOLE) {
                            invoker.clearScriptCounter();
                        } else {
                            invoker.scriptCount();
                        }
                        if (invoker.getScriptCounter() < invoker.SCRIPT_RECURSION_LIMIT) {
                            CommandRequest req = invoker.createRequest(line, ReadModes.FILE, sender);

                            Response response = server.handleRequest(req, req.getFileContent());

                            if (response != null) {
                                while (response.hasResponseRequest()) {
                                    response = server.listenResponse();
                                }
//                            System.out.println(cmdResponse.toString());
                                totalResponse.add(response.getMessage());
                            }
                        } else {
                            // чтоб не спамило:
                            if (invoker.getScriptCounter() == invoker.SCRIPT_RECURSION_LIMIT)
                                return new Response("Рекурсивный вызов скриптов!");
                            break;
                        }
                    }
                    return new Response(totalResponse.toString());
                } catch (IOException e) {
//                throw new RuntimeException(e);
                    return new Response("Не удалось считать данные из файла (возможно, файл не найден)");
                }
            } else {
                return new Response("Не удалось получить содержимое файла");
            }
        } else {
            return new Response(String.format("Неверное количество аргументов (got %s, expected 1)", args.length));
        }
    }

    @Override
    public String getDesc() {
        return DESC;
    }

    @Override
    public String getUsage() {
        return USAGE;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
}
