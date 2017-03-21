import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by s4d on 2/27/2017.
 */
public class Main {

    public static void main(String[] args) throws Exception{
        int port = 1200;
        // Starts the server at port 1200
        new Server(port);


        // Connects localhost to the server
        try {
            Client client = new Client(InetAddress.getLocalHost().getHostName(), port);
        }
        catch (IOException e){
            System.exit(-1);
        }
    }
}
