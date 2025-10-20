import javax.net.ssl.SSLServerSocketFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server extends Thread {
    private ServerSocket serverSocket;
    protected List<ClientHandler> clients;
    protected List<GameRoom> gameRooms;
    protected SSLServerSocketFactory ssf;
    protected User u;
    String command;
    String serverMessage;

    protected Boolean logged_on = false;
    protected Boolean nameBusy = false;

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
            while (true) {
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
        client = newClient;
        clients = newClients;
        gameRooms = newGameRooms;
        DataInputStream in;
        DataOutputStream out;
    }

    public void run() {
        //String loginCommand;

        try {
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClientHandler newClient = new ClientHandler(client);


        //sets the user that is returned in login
            while (!logged_on){
                try {
                    command = null;
                    command = in.readUTF();
                    login login = new login();
                    register register = new register();

//                new SendMessage(clients);
                    System.out.println("Just connected to " + client.getRemoteSocketAddress());
                    System.out.println("Command from client: " + command);


                    if (command.contains("/login")) {
                        String works = command;
                        String[] parts = works.split(" ");
                        String part1 = parts[1]; // Username
                        String part2 = parts[2]; // Password
                        String username = part1;
                        String password = part2;
                        System.out.print("This is username: " + username);
                        System.out.print("This is password: " + password);
                        User u = login.logon(username, password);
                        out.writeUTF("Logged on as... " + username + ".");
                        break;
                    } else if (command.contains("/register"))  {
                        String works = command;
                        String[] parts = works.split(" ");
                        String part1 = parts[1]; // Username
                        String part2 = parts[2]; // Password
                        String username = part1;
                        String password = part2;
                        register.registerUser(username, password);
                        out.writeUTF("Registerd on as... " + username + ".");
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        newClient.setUser(u);
        System.out.println(newClient.user);
        clients.add(newClient);



        HandlerUserInput(newClient);



        /* User user = logon();  Register / login - Dan
        while(!user == null){
            HandlerUserInput(newClient);
        }
       */
    }

    public void HandlerUserInput(ClientHandler newClient) {
        newClient.isBusy = false;
        while (true) {

            serverMessage = "";
            command = null;

            try {

                command = in.readUTF();
                System.out.println("Command from client: " + command);

                if (command.matches("/refresh")) {
                    if (gameRooms.isEmpty()) {
                        out.writeUTF("No games found!");
                    } else {

                        for (GameRoom game : gameRooms) {
                            if (game.isFull == false) {
                                serverMessage = serverMessage + game.roomName + " Connected users : " + game.connectedClients.size() + "  Host: " + game.host + "\n";
                            }
                        }
                        out.writeUTF(serverMessage);
                    }
                } else if (command.matches("/create")) {
                }
                for (GameRoom game : gameRooms) {
                    if (game.host.equals(newClient)) {
                        out.writeUTF("You have already an active room!");
                        newClient.isBusy = true;
                    }
                }
                if (!newClient.isBusy) {
                    try {
                        for (GameRoom gm: gameRooms) {
                            if(gm.roomName.matches(command.substring(8))){
                                nameBusy = true;
                                break;
                            }
                        }
                        if(!nameBusy) {
                            gameRooms.add(new GameRoom(command.substring(8), 4, newClient));
                            out.writeUTF("A new gameroom has been created. . . \n Searching for client to connect. . . ");
                            newClient.isBusy = true;
                        }
                        else {
                            out.writeUTF("Roomname already found in list.");
                        }
                    }catch (Exception e) {
                        out.writeUTF("No roomname.");
                    }

                }
                else if (command.contains("/join")) {
                    Boolean roomFound = true;
                    if (!gameRooms.isEmpty()) {
                        for (GameRoom gm : gameRooms) {
                            try {
                                if (gm.roomName.matches(command.substring(6))) {
                                    if (newClient.isBusy) {
                                        out.writeUTF("You are already connected to a room, write /leave to leave");
                                        break;
                                    } else if (gm.connectedClients.size() >= gm.maxUsers) {
                                        out.writeUTF("Room is full!");
                                        break;
                                    } else if (!gm.connectedClients.contains(newClient)) {
                                        newClient.isBusy = true;
                                        gm.connectedClients.add(newClient);
                                        out.writeUTF("Connecting to room: " + gm.roomName);
                                        break;
                                    }
                                }
                                else {
                                    roomFound = false;
                                }
                            }catch (Exception e){

                                break;
                            }

                        }

                    }else{
                        if (!roomFound){
                            out.writeUTF("Cannot find your requested room");
                        } else {
                            out.writeUTF("There were no rooms found in the list.");
                        }
                    }

                }
                else if (command.matches("/leave")) {
                    try {
                        if (!gameRooms.isEmpty() && newClient.isBusy) {
                            for (GameRoom gm : gameRooms) {
                                if (gm.connectedClients.contains(newClient)) {
                                    if (gm.host.equals(newClient)) {
                                        for (ClientHandler g : gm.connectedClients) {
                                            g.isBusy = false;
                                        }
                                        gameRooms.remove(gm);}
                                    gm.connectedClients.remove(newClient);
                                    newClient.isBusy = false;
                                    out.writeUTF("You left: " + gm.roomName);
                                    break;
                                }
                            }
                        }
                        else{
                            out.writeUTF("Roomlist is empty or no room to leave.");
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }


                } else if (command.matches("/help")) {
                    out.writeUTF("/create [roomname] \t /join [roomname] \t /leave \t /refresh");
                }
                else{
                    out.writeUTF("Could not find any matching commands for: " +command);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}