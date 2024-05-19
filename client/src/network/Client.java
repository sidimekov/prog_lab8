package network;

import java.io.*;
import java.net.*;

public class Client {
    private static Client client;
    private DatagramSocket dsClient;
    private User user;
    public final InetSocketAddress serverSocketAddr = new InetSocketAddress("localhost", 8000);

    private Client() {
        client = this;

        try {
            dsClient = new DatagramSocket();
        } catch (IOException e) {
            System.out.println("Ошибка при формировании сокетов клиента");
        }
    }

    public static Client getInstance() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    /**
     * Отправка запроса по указанным адресу и порту с помощью сетевого канала
     * Затем возвращает Response - ответ от сервера
     * @param request - Запрос
     * @param serverAddr - Адрес сервера для подключения
     * @param serverPort - Порт сервера для подключения
     *
     * @return response - Ответ от сервера
     */
    public Response sendRequest(Request request, InetAddress serverAddr, int serverPort) {

        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            oos.writeObject(request);

            oos.flush();
            oos.close();

            baos.close();
            byte[] byteResponse = baos.toByteArray();

            DatagramPacket dpRequest = new DatagramPacket(byteResponse, byteResponse.length, serverAddr, serverPort);

            dsClient.send(dpRequest);

        } catch (FileNotFoundException e) {
            System.out.printf("Файл %s не найден, ошибка: %s\n", request.getFilePath(), e.getMessage());
        } catch (IOException e) {
            System.out.printf("Ошибка ввода/вывода при посылке запроса при создании канала сокетов по адресу %s:%s. Ошибка: %s\n", serverAddr, serverPort, e.getMessage());
//            e.printStackTrace();
        }

        Response response = null;

        try {

            byte[] bytesResponse = new byte[4096];
            DatagramPacket dpClient = new DatagramPacket(bytesResponse, bytesResponse.length);

            dsClient.receive(dpClient);

            byte[] data = dpClient.getData();

            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);

            response = (Response) ois.readObject();

            bais.close();
            ois.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.printf("Ошибка ввода/вывода при получении ответа с сервера по адресу %s:%s : %s\n", serverAddr, serverPort, e.getMessage());
        } catch (ClassNotFoundException | ClassCastException e) {
//            e.printStackTrace();
            System.out.printf("Ошибка при формировании ответа с сервера по адресу %s:%s : %s\n", serverAddr, serverPort, e.getMessage());
        }

        return response;
    }

    public void sendResponse(Response response, InetAddress serverAddr, int serverPort) {

        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            oos.writeObject(response);

            oos.flush();
            oos.close();

            baos.close();
            byte[] byteResponse = baos.toByteArray();

            DatagramPacket dpResponse = new DatagramPacket(byteResponse, byteResponse.length, serverAddr, serverPort);
            dsClient.send(dpResponse);

        } catch (IOException e) {
            System.out.printf("Ошибка ввода/вывода при посылке ответа при создании канала сокетов по адресу %s:%s\n", serverAddr, serverPort);
        }
    }

    public Response listenResponse(InetAddress serverAddr, int serverPort) {
//        System.out.println("Слушаю ответ");

        Response response = null;

        try {

            byte[] bytesResponse = new byte[4096];
            DatagramPacket dpClient = new DatagramPacket(bytesResponse, bytesResponse.length);

            dsClient.receive(dpClient);

            byte[] data = dpClient.getData();

            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);

            response = (Response) ois.readObject();

            bais.close();
            ois.close();

        } catch (IOException e) {
            System.out.printf("Ошибка ввода/вывода при получении ответа с сервера по адресу %s:%s : %s\n", serverAddr, serverPort, e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException | ClassCastException e) {
//            e.printStackTrace();
            System.out.printf("Ошибка при формировании ответа с сервера по адресу %s:%s : %s\n", serverAddr, serverPort, e.getMessage());
        }

        return response;

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
