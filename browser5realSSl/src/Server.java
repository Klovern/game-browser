import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.net.Socket;

import javax.net.SocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;

public class Server extends Thread {
    private ServerSocket serverSocket;
    protected List<ClientHandler> clients;
    protected List<GameRoom> gameRooms;
    protected   SSLServerSocketFactory ssf;
    String command;
    String serverMessage;

    protected Boolean logged_on = false;

    DataInputStream in;
    DataOutputStream out;

    protected class Trad extends Thread {
        Socket client;
        public void run(Socket newClient) {
            // allt från Server:s run() häri!
        }
    }


    public Server(int port) {
        try {
            this.ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            this.serverSocket = ssf.createServerSocket(port);
            System.out.println("New server initialized!");
            clients = Collections
                    .synchronizedList(new ArrayList<ClientHandler>());
            this.gameRooms = Collections
                    .synchronizedList(new ArrayList<GameRoom>());
            while(true) {
                Socket client = serverSocket.accept();
                Thread newThis = new Server(client, clients, gameRooms);
                newThis.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Socket client;
    public Server(Socket newClient, List<ClientHandler> newClients, List<GameRoom> newGameRooms) {
        client = newClient; clients = newClients; gameRooms = newGameRooms;
        DataInputStream in; DataOutputStream out;
    }

    public void run() {
        ClientHandler newClient = new ClientHandler(client);


        // Daniels Kåd Börjar Här
        login login = new login();
        register register = new register();

        try {
            command = null;
//                new SendMessage(clients);
            System.out.println("Just connected to " + client.getRemoteSocketAddress());

            command = in.readUTF().toString();
            System.out.println("Command from client: " + command);

            System.out.println("I'm tryin' here!");
            while(!logged_on)
                if (command.substring(0,6).contains("/logon")){

                    String username = "Harold";
                    String password = "Smith";
                    login.logon(username, password);
                    out.writeUTF("Logging on... ");
                    logged_on = true;
                    break;
                } else if (command.substring(0,9).contains("/register")){
                    String username = "Harold";
                    String password = "Smith";
                    register.registerUser(username, password);
                    break;
                }

        } catch (IOException e){
            e.printStackTrace();
        }
        //Daniels Kod Slutar Här
        
        clients.add(newClient);

        try {
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }

        HandlerUserInput(newClient);

        /* User user = logon();  Register / login - Dan
        while(!user == null){
            HandlerUserInput(newClient);
        }
       */
    }

    public void HandlerUserInput(ClientHandler newClient){
        while (true) {
            newClient.isBusy = false;
            try {

                command = in.readUTF();
                System.out.println("Command from client: " + command);

                if (command.matches("/refresh")) {
                    if (gameRooms.isEmpty()) {
                        out.writeUTF("No games found!");
                    } else {
                        serverMessage = "";
                        for (GameRoom game : gameRooms) {
                            if (game.isFull == false) {
                                serverMessage = serverMessage + game.roomName + " Connected users : " + game.connectedClients.size() + "  Host: " +game.host + "\n";
                            }
                        }
                        out.writeUTF(serverMessage);
                    }
                } else if (command.matches("/create")) {
                    for (GameRoom game : gameRooms) {
                        if (game.host.equals(newClient)) {
                            out.writeUTF("You have already an active room!");
                            newClient.isBusy = true;
                        }
                    }
                    if (!newClient.isBusy) {
                        gameRooms.add(new GameRoom("test", 2, newClient));
                        out.writeUTF("A new gameroom has been created. . . \n Searching for client to connect. . . " +command);
                        newClient.isBusy = true;
                    }

                } else if (command.contains("/join")) {
                    for(GameRoom gm : gameRooms){
                        if (gm.roomName.matches(command.substring(6))){
                            if(newClient.isBusy){
                                out.writeUTF("You are already connected to a room, write /leave to leave");
                            }
                            if(!gm.connectedClients.contains(newClient)) {
                                newClient.isBusy = true;
                                gm.connectedClients.add(newClient);
                                out.writeUTF("Connecting to room. " +command);
                            }
                        }
                        else{
                            out.writeUTF("No rooms with the specified name was found.");
                        }

                    }

                }

                else if (command.contains("/leave")) {
                    for(GameRoom gm : gameRooms){
                        if (gm.connectedClients.contains(newClient)){


                        }
                        else{
                            out.writeUTF("No rooms with the specified name was found.");
                        }

                    }

                }

                else if (command.matches("/message")) {
                    for(GameRoom gm : gameRooms){
                        if (gm.connectedClients.contains(newClient)){
                            SendMessage sm = new SendMessage(gm.connectedClients, command);
                           // out.writeUTF("Message to clients:  " +command);
                        }
                    }

                }
                else{
                    out.writeUTF("Could not find any matching commands for: " +command);
                }
                command = null;

            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

    }
}