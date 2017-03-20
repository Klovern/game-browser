import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by s4d on 2/27/2017.
 */
public class MainServer {

    public static void main(String[] args) {
        int port = 1300;
        // Starts the server at port 1200
        new Server(port);
    }
}