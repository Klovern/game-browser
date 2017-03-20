import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.net.Socket;
import java.util.Scanner;

public class Server extends Thread {
    private ServerSocket serverSocket;
    protected List<ClientHandler> clients;
    protected List<GameRoom> gameRooms;
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
            this.serverSocket = new ServerSocket(port);
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
        clients.add(newClient);

        try {
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }

        login login = new login();
        register register = new register();

        System.out.println("Just before Scanner.");
        Scanner menu_scanner = new Scanner(System.in);
        int choice = menu_scanner.nextInt();

        System.out.println("Enter a choice. \n 1. For register \n 2. For login");

        try {
            while(logged_on = false)
                switch (choice) {
                    case 1:
                        register.registerUser();
                        break;
                    case 2:
                        login.logon();
                        logged_on = true;
                        break;
                }
        } catch (IOException e){
            e.printStackTrace();
        }

        while (true) {
            Boolean hostIsBusy = false;
            try {
                command = null;
//                new SendMessage(clients);
                System.out.println("Just connected to " + client.getRemoteSocketAddress());

                command = in.readUTF().toString();
                System.out.println("Command from client: " + command);

                if (command.substring(0,4).contains("/ref")) {
                    if (gameRooms.isEmpty()) {
                        out.writeUTF("No games found!");
                    } else {
                        serverMessage = "";
                        for (GameRoom game : gameRooms) {
                            if (game.isFull == false) {
                                serverMessage = serverMessage + game.roomName + " Connected users : " + game.connectedClients + "\n";
                            }
                        }
                        out.writeUTF(serverMessage);
                    }
                } else if (command.substring(0, 4).equals("/cre")) {
                    for (GameRoom game : gameRooms) {
                        if (game.host.equals(newClient)) {
                            out.writeUTF("You have already an active room!");
                            hostIsBusy = true;
                        }
                    }
                    if (!hostIsBusy) {
                        gameRooms.add(new GameRoom("GameRoom_Name2", 2, newClient));
                        out.writeUTF("A new gameroom has been created. . . \n Searching for client to connect. . . ");
                        hostIsBusy = true;
                    }

                } else if (command.substring(0, 4).equals("/joi")) {
                    out.writeUTF("Connecting to the room!.");
                }
                else{
                    out.writeUTF("Could not find any matching commands for: "+command.substring(0,3));
                }
                command = null;
                serverMessage = null;
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}