import network.Server;
import java.nio.channels.SocketChannel;

public class Main {


    public static void main(String[] args) {
        Server server = Server.getInstance();

        server.run(8000);
    }
}