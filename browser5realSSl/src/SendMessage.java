import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.List;

public class SendMessage extends Thread {
    protected List<ClientHandler> clients;
    protected String userInput;
    protected BufferedReader console;

    public SendMessage(List<ClientHandler> clients, String message) {
        System.out.println("message sent");
        this.clients = clients;
        this.userInput = null;


        System.out.println("message sent");

        try {
            for (ClientHandler client : clients) {
                DataOutputStream out = new DataOutputStream(client.client.getOutputStream());
                out.writeUTF(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
