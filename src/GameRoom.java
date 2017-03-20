import javax.print.DocFlavor;
import java.net.ServerSocket;
import java.util.List;

public class GameRoom {
    private ServerSocket serverSocket;
    protected ClientHandler host;
    protected Game game;
    protected String roomName;
    protected int maxUsers;
    protected int connectedClients;
    protected boolean isFull;
    protected GameRoom roomList;



    public GameRoom(String roomName, int maxUsers, ClientHandler host){
        this.isFull = false;
        this.connectedClients = 1;
        this.roomName = roomName;
        this.maxUsers = maxUsers;
        this.host = host;
    }

    public boolean CheckIfFull(){
        return false;
    }
}
