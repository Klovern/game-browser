import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by Oskar on 2017-02-27.
 */
public class MainClient {

    public static void main(String[] args) throws Exception{
        int port = 1200;

        // Connects localhost to the server
        RunMultipleClients(port, 0);


    }

    public static void RunMultipleClients(int port, int numberOfClients) throws Exception{
        int n = 0;
        do {
            try {
                Client client = new Client(InetAddress.getLocalHost().getHostName(), port);

            } catch (IOException e) {
                System.exit(-1);
            }
            n++;
        }while( n <= numberOfClients );
    }
}
