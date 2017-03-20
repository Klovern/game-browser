import java.io.*;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client extends Thread {
    protected Socket client;
    protected BufferedReader in;
    protected Boolean keepInLoop;
    protected Boolean logged_on = false;


    public Client(String hostName, int ip) {
        System.out.println("Client initilized!");
        keepInLoop = true;
        try {
            this.client = new Socket(hostName, ip);
            this.in = new BufferedReader(new InputStreamReader(
                    this.client.getInputStream()));
            this.client.setKeepAlive(true);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void run() {
        // will listen to response from server.



        while (keepInLoop) {
            System.out.println("Client is listening! ");
            System.out.println(this.client);

            try {

                Scanner menuscanner = new Scanner(System.in);
                int choice = menuscanner.nextInt();

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

                OutputStream outToServer = this.client.getOutputStream();
                DataOutputStream out = new DataOutputStream(outToServer);

                InputStream inFromServer = client.getInputStream();
                DataInputStream in = new DataInputStream(inFromServer);

                Scanner scan = new Scanner(System.in);
                String str = scan.next();

                try {
                    out.writeUTF(str);
                    out.flush();
                    System.out.println(in.readUTF());
                } catch (Exception e){
                    e.printStackTrace();
                }

                /*
                Stresstest
                while(true) {
                    out.writeUTF(str);
                    out.flush();
                    System.out.println(in.readUTF());
                    try {
                        TimeUnit.SECONDS.sleep((long)0.5);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                */

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


    /*   try {
                    this.client.setKeepAlive(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    this.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/