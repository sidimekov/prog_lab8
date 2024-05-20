package commandManagers;

import enums.ReadModes;
import network.*;
import util.InputManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class CommandInvoker {
    private static CommandInvoker instance;

    private final int MAX_RECONNECTION_ATTEMPTS = 10;
    private int reconnectionTimeout;

    private CommandInvoker() {
    }

    public static CommandInvoker getInstance() {
        if (instance == null) {
            instance = new CommandInvoker();
        }
        return instance;
    }

    public void listenCommands() {
        try {
            BufferedReader reader = InputManager.getConsoleReader();
            while (true) {
                String line = reader.readLine();
                runCommand(line, ReadModes.CONSOLE);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void runCommand(String line, ReadModes readMode) {
        String[] tokens = line.split(" ");
        String cmdName = tokens[0].toLowerCase();
        String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
        if (!cmdName.isEmpty()) {

            if (cmdName.equals("exit")) {
                System.out.println("Пока, вырубаюсь...");
                System.exit(0);
            }

            Client client = Client.getInstance();
            CommandRequest request = new CommandRequest(cmdName, args);
            request.setUser(Client.getInstance().getUser());
            request.setReadMode(readMode);

            if (args.length > 0) {
                File file = InputManager.validPath(args[0]);

                if (file != null) {
                    request.setFilePath(file.getAbsolutePath());
                }
            }

            InetSocketAddress serverSocketAddr = client.serverSocketAddr;

            for (int i = 0; i < MAX_RECONNECTION_ATTEMPTS; i++) {
                Response response;

                response = client.sendRequest(request, serverSocketAddr.getAddress(), serverSocketAddr.getPort());
//                System.out.println(response);

                try {
                    switch (cmdName) {
                        case "register", "login" -> {
                            User user = response.getUser();

                            if (user != null) {
                                client.setUser(user);
                            }
                        }
                        case "logout" -> {
                            client.setUser(null);
                            System.out.println("Вы вышли из аккаунта. Авторизирутейсь (login <логин> <пароль>) или зарегистрируйтесь (register <логин> <пароль> <подтверждение пароля>)");
                            return;
                        }
                    }


                    while (response.hasResponseRequest()) {
//                System.out.println(response);

                        if (response.getMessage() != null) {
                            System.out.println(response.getMessage());
                        }
                        // Если сервер при посылке ответа, послал запрос (например передать элемент)

                        Request req = response.getResponseRequest();
                        switch (req.getType()) {
                            case BUILD -> {
                                BuildRequest buildRequest = (BuildRequest) req;
                                handleRequest(buildRequest);
                            }
                            case FILE -> {
                                FileRequest fileRequest = (FileRequest) req;
                                handleRequest(fileRequest);
                            }
                            case MESSAGE -> System.out.println(((MessageRequest) req).getMessage());
                        }

                        response = client.listenResponse(serverSocketAddr.getAddress(), serverSocketAddr.getPort());
                    }

                    System.out.println(response.getMessage());

                    break;

                } catch (NullPointerException e) {
                    reconnectionTimeout = (i + 1) * 5000;
                    System.out.printf("Не удалось подключиться к серверу! Повторная попытка через %s секунд\n", reconnectionTimeout / 1000);
                    System.out.println(e.getMessage());
                    try {
                        Thread.sleep(reconnectionTimeout);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
//                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Пустая команда!");
        }
    }

    public Response listenConsole(String message) {
        try {
            BufferedReader reader = InputManager.getConsoleReader();
//            System.out.println(message);
            String line = reader.readLine();
            return new Response(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleRequest(Request request) {
        Client client = Client.getInstance();
        InetSocketAddress serverSocketAddr = client.serverSocketAddr;

        switch (request.getType()) {
            case BUILD -> {
                BuildRequest buildRequest = (BuildRequest) request;

                Response buildResponse = listenConsole(buildRequest.getMessage());
//                System.out.println(buildRequest.getMessage());

                client.sendResponse(buildResponse, serverSocketAddr.getAddress(), serverSocketAddr.getPort());
            }
            case FILE -> {
                Response response;

                FileRequest fileRequest = (FileRequest) request;

                String path = fileRequest.getFilePath();

                if (InputManager.validPath(path) != null) {

                    if (path.startsWith("\"") && path.endsWith("\"")) {
                        path = path.substring(1, path.length() - 1);
                    }

                    String fileContent = null;

                    try {
                        fileContent = Files.readString(Path.of(path), StandardCharsets.UTF_8);
//                    System.out.println(fileContent);
                    } catch (IOException e) {
                        System.out.printf("Ошибка при отправке содержимого файла: %s", e.getMessage());
//                    return;
                    }

                    response = new Response(fileContent);
                } else {
                    response = new Response("");
                }

                client.sendResponse(response, serverSocketAddr.getAddress(), serverSocketAddr.getPort());
//                client.sendResponse(new Response(path), serverSocketAddr.getAddress(), serverSocketAddr.getPort());
            }
        }


    }
}

