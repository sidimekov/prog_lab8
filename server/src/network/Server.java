package network;

import commandManagers.CommandInvoker;
import commandManagers.RouteManager;
import commandManagers.commands.*;
import enums.ReadModes;
import enums.RequestTypes;
import multithreading.ThreadManager;
import util.InputManager;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class Server {
    private static Server server;
    private DatagramSocket dsServer;
    private InetAddress clientAddr;
    private int clientPort;
    private static final Logger logger = Logger.getLogger("Server");

    private Server() {
        server = this;
    }

    public static Server getInstance() {
        if (server == null) {
            server = new Server();
        }
        return server;
    }

    public void openServerSocket(int serverPort) throws IOException {
        dsServer = new DatagramSocket(serverPort);
    }

    public void run(int serverPort) {

        RouteManager.initialize();

//        Scanner credentials = null;
//        String login, passwd;
//        try {
//            credentials = new Scanner(new FileReader("credentials"));
//        } catch (FileNotFoundException e) {
//            logger.severe("Не найден файл для подключения к БД");
//            System.exit(0);
//        }
//
//        login = credentials.nextLine().trim();
//        passwd = credentials.nextLine().trim();
//
//        String jdbcURL = "jdbc:postgresql://pg:5432/studs";


        try {
            openServerSocket(serverPort);
        } catch (IOException e) {
            System.out.printf("Ошибка при открытии сокета на порту: %s\n", serverPort);
            return;
        }

        logger.info(String.format("Сервер запущен на порту: %s\n", serverPort));

        BufferedReader reader = InputManager.getConsoleReader();

        while (true) {

            // Executor Services
            ExecutorService requestGetter = ThreadManager.getRequestExecutor();
            ForkJoinPool requestExecutor = ThreadManager.getExecuteRequestExecutor();
            ExecutorService responseSender = ThreadManager.getSendResponseExecutor();

            // Получение запроса
            Request request;

            Callable<Request> callRequest = this::listenRequest;

            Future<Request> requestFuture = requestGetter.submit(callRequest);

            try {
                request = requestFuture.get();
            } catch (InterruptedException e) {
                logger.severe("Поток чтения запросов прерван");
                continue;
            } catch (ExecutionException e) {
                logger.severe(String.format("В потоке чтения запросов возникла ошибка: %s\n", e.getMessage()));
                continue;
            }

            // Обработка запроса и формирование ответа
            Response response;

            Callable<Response> callResponse = () -> makeResponse(request);

//            Future<Response> responseFuture = requestExecutor.invoke(callResponse);
            Future<Response> responseFuture = requestExecutor.submit(callResponse);

            try {
                response = responseFuture.get();
            } catch (InterruptedException e) {
                logger.severe("Поток обработки запросов прерван");
                continue;
            } catch (ExecutionException e) {
                logger.severe(String.format("В потоке обработки запросов возникла ошибка: %s\n", e.getMessage()));
                e.printStackTrace();
                continue;
            }

            // Посылка запроса
            Runnable sendResponse = () -> sendResponse(response);

            responseSender.execute(sendResponse);

        }
    }

    /**
     * Ожидает запросы от клиента
     * Получение запроса с помощью сетевого канала
     *
     * @return response - Возвращаемый ответ от сервера
     */
    private Request listenRequest() {
        Request request = null;

        try {

            byte[] bytesRequest = new byte[4096];
            DatagramPacket dpRequest = new DatagramPacket(bytesRequest, bytesRequest.length);

            dsServer.receive(dpRequest);

            byte[] dataRequest = dpRequest.getData();

            clientAddr = dpRequest.getAddress();
            clientPort = dpRequest.getPort();

            ByteArrayInputStream bais = new ByteArrayInputStream(dataRequest);
            ObjectInputStream ois = new ObjectInputStream(bais);

            request = (Request) ois.readObject();

            bais.close();
            ois.close();

            String msg = request.toString();
            logger.info(String.format("Получен запрос от %s:%s : %s\n", clientAddr, clientPort, msg));

        } catch (IOException e) {
            logger.severe(String.format("Ошибка ввода/вывода при получении запросов: %s\n", e.getMessage()));
        } catch (ClassNotFoundException | ClassCastException e) {
            logger.severe(String.format("Ошибка при формировании запроса от клиента по адресу %s:%s\n", clientAddr, clientPort));
//            e.printStackTrace();
        }

        String fileContent = null;

        if (request != null && request.getFilePath() != null) {

            // посылка запроса контента файла, если путь был указан
            fileContent = getFileContent(request);

            request.setFileContent(fileContent);

        }

        return request;
    }

    public String getFileContent(Request request) {
        String fileContent;

        // создание запроса FileRequest, путь указывается который был послан серверу как аргумент
        FileRequest fileRequest = new FileRequest();
        fileRequest.setFilePath(request.getFilePath());

        // формирование ответа с запросом FileRequest и его отправка
        Response serverResponse = new Response(fileRequest);

        // получение того, что в файле клиента
        Response clientFileContent = sendResponse(serverResponse);

        fileContent = clientFileContent.getMessage();

        return fileContent;
    }

    /**
     * Формирует ответ на запрос, обрабатывая его
     *
     * @param request - запрос, для которого нужно сформировать ответ
     * @return нужный ответ на запрос
     */
    private Response makeResponse(Request request) {

        String fileContent = request.getFileContent();
        Response response = null;

        try {
            if (request.getType() == RequestTypes.COMMAND) {
                if (fileContent == null) {
                    response = handleRequest((CommandRequest) request);
                } else {
                    response = handleRequest((CommandRequest) request, fileContent);
                }
            }
        } catch (NullPointerException e) {
            logger.severe(String.format("null: %s\n", e.getMessage()));
//            e.printStackTrace();
        }

        return response;
    }


    /**
     * Ожидает ответ от клиента, на отправленный сервером запрос (запросы от сервера - buildRequest или fileRequest)
     *
     * @return response - полученный ответ
     */
    public Response listenResponse() {
        Response response = null;

        try {
            byte[] bytesRequest = new byte[4096];
            DatagramPacket dpResponse = new DatagramPacket(bytesRequest, bytesRequest.length);

            dsServer.receive(dpResponse);

            byte[] dataResponse = dpResponse.getData();

            clientAddr = dpResponse.getAddress();
            clientPort = dpResponse.getPort();

            ByteArrayInputStream bais = new ByteArrayInputStream(dataResponse);
            ObjectInputStream ois = new ObjectInputStream(bais);

            response = (Response) ois.readObject();

            bais.close();
            ois.close();

            String msg = response.getMessage();
            logger.info(String.format("Получен ответ от %s:%s : %s...\n", clientAddr, clientPort, msg.length() > 16 ? msg.substring(0, 16) : msg));

        } catch (IOException e) {
            logger.severe(String.format("Ошибка ввода/вывода при получении запросов: %s\n", e.getMessage()));
//            e.printStackTrace();
        } catch (ClassNotFoundException | ClassCastException e) {
            logger.severe(String.format("Ошибка при формировании запроса от клиента по адресу %s:%s. Ошибка: %s\n", clientAddr, clientPort, e.getMessage()));
        }

        return response;
    }

    /**
     * Обработать командный запрос, указанный в параметрах и вернуть ответ
     *
     * @param request - Запрос для обработки
     * @return response - Ответ после запроса
     */
    public Response handleRequest(CommandRequest request, String fileContent) {
        Response response;

//        System.out.println("fileContent: " + fileContent);
        String cmdName = request.getCommand();
        String[] args = request.getArgs();
        ReadModes readMode = request.getReadMode();

        CommandInvoker cmdInvoker = CommandInvoker.getInstance();
        Command command = cmdInvoker.getCommand(cmdName);

        if (request.getUser() == null && !cmdName.equals("register") && !cmdName.equals("login")) {
            response = new Response("Авторизируйтесь!");
            if (request.getReadMode() != ReadModes.FILE) logger.warning("Пользователь не авторизован...");
            return response;
        }

        if (command == null) {
            response = new Response("Указанной команды не существует!");
            if (request.getReadMode() != ReadModes.FILE) logger.warning("Такой команды не существует, отправка ответа клиенту...");
            return response;
        }

        User user = request.getUser();
        if (user != null) {
            if (request.getReadMode() != ReadModes.FILE) logger.info(String.format("Пользователь: %s", user));
            command.setSender(user);
        }

        if (request.getFilePath() != null && fileContent != null) {
            switch (cmdName) {
                case "execute_script" -> {
                    ExecuteScriptCommand exe = (ExecuteScriptCommand) command;

                    exe.setScript(fileContent);
                }
                case "add" -> {
                    AddCommand add = (AddCommand) command;

                    add.setJsonContent(fileContent);
                }
                case "add_if_min" -> {
                    AddIfMinCommand add = (AddIfMinCommand) command;

                    add.setJsonContent(fileContent);
                }
                case "remove_greater" -> {
                    RemoveGreaterCommand removeGreater = (RemoveGreaterCommand) command;

                    removeGreater.setJsonContent(fileContent);
                }
                case "update" -> {
                    UpdateCommand update = (UpdateCommand) command;

                    update.setJsonContent(fileContent);
                }
            }
        }

        response = cmdInvoker.runCommand(command, args, readMode);
        if (request.getReadMode() != ReadModes.FILE) logger.info(String.format("Команда %s выполнена, отправка ответа клиенту...\n", cmdName));

        return response;
    }

    public Response handleRequest(CommandRequest request) {
        return handleRequest(request, null);
    }

    /**
     * Отправка ответа на запрос клиенту
     *
     * @param response - отправляемый ответ
     */
    public Response sendResponse(Response response) {

        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

//            System.out.println(response);
            oos.writeObject(response);

            oos.flush();
            oos.close();

            baos.close();
            byte[] byteResponse = baos.toByteArray();

            DatagramPacket dpServer = new DatagramPacket(byteResponse, byteResponse.length, clientAddr, clientPort);

            dsServer.send(dpServer);

            if (response == null) return null;

            if (response.hasResponseRequest()) {
                logger.info(String.format("Отправлен ответ с запросом типа %s клиенту по адресу %s:%s\n\n", response.getResponseRequest().getType().toString(), clientAddr, clientPort));
            } else {
                logger.info(String.format("Отправлен ответ клиенту по адресу %s:%s\n\n", clientAddr, clientPort));
//                System.out.println(response);
            }
//            System.out.printf("Запрос: %s\n", response.getMessage());

        } catch (IOException e) {
            logger.severe(String.format("Ошибка ввода/вывода при посылке ответа клиенту по адресу %s:%s : %s\n", clientAddr, clientPort, e.getMessage()));
            return null;
        }

        if (response.hasResponseRequest()) {
            // Наш посланный ответ содержит запрос, поэтому нужно выждать ответ

            return listenResponse();

        } else {
            return null;
        }
    }

    public static Logger getLogger() {
        return logger;
    }

}
