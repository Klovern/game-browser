import javax.print.DocFlavor;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameRoom {
    private ServerSocket serverSocket;
    protected ClientHandler host;
    protected Game game;
    protected String roomName;
    protected int maxUsers;
    protected List<ClientHandler> connectedClients;
    protected boolean isFull;
    protected GameRoom roomList;



    public GameRoom(String roomName, int maxUsers, ClientHandler host){
        this.isFull = false;
        this.roomName = roomName;
        this.maxUsers = maxUsers;
        this.host = host;
        this.connectedClients = Collections
                .synchronizedList(new ArrayList<ClientHandler>());
        this.connectedClients.add(host);
    }

    public boolean CheckIfFull(){
        return false;
    }
}
