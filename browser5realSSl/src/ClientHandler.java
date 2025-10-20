import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientHandler {
    protected Socket client;
    protected boolean isBusy;
    protected PrintWriter out;
    protected User user;

    /*
    public void setPassword(String password) {
        this.Password = password;
    }

    public void setUsername(String username) {
        this.Username = username;
    }
    */

    public ClientHandler(Socket client) {
        this.client = client;
        try {
            this.out = new PrintWriter(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setUser(User u){
        this.user = u;
    }
}


