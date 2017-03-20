import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientHandler {
    protected Socket client;
    protected PrintWriter out;
    protected String Username;
    protected String Password;

    public void SetPassword(String password) {this.Password = password;}
    public void SetUsername(String username) {this.Username = username;}



    public ClientHandler(Socket client) {
        this.client = client;
        try {
            this.out = new PrintWriter(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
