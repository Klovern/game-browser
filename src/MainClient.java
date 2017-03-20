import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by Oskar on 2017-02-27.
 */
public class MainClient {

    public static void main(String[] args){
        int port = 1300;

        // Connects localhost to the server
        try {
            Client client = new Client(InetAddress.getLocalHost().getHostName(), port);

        }
        catch (IOException e){
            System.exit(-1);
        }

    }
}
