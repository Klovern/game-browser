import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client extends Thread {
    protected Socket client;
    protected BufferedReader in;
    protected Boolean keepInLoop;
    protected DataOutputStream streamOut;
    protected DataInputStream streamIn;
    SocketFactory sf;


    public Client(String hostName, int ip) throws Exception{
        System.out.println("Client initilized! You login by typing /login Username Password eller regristerar dig med /register Username Password ett tomma kommandon buggar ur. ");
        keepInLoop = true;
        try {
            this.sf = SSLSocketFactory.getDefault();
            this.client = sf.createSocket(hostName, ip);
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
        try {
            OutputStream outToServer = this.client.getOutputStream();
            streamOut = new DataOutputStream(outToServer);

            InputStream inFromServer = client.getInputStream();
            streamIn = new DataInputStream(inFromServer);

        }catch (IOException e){
            e.printStackTrace();
        }

        while (keepInLoop) {
            try {
                keepInLoop = SendUserInput(streamOut, streamIn);
               // keepInLoop = ServerTest(streamOut,streamIn, 20, (long)2000);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public boolean SendUserInput(DataOutputStream out, DataInputStream in) throws IOException{


        //System.out.print("Test message: " +in.readUTF());

        Scanner scan = new Scanner(System.in);
        String str = scan.nextLine();

        try {
            out.writeUTF(str);
            out.flush();
            System.out.println(in.readUTF());
        } catch (Exception e){
            e.printStackTrace();
        }
        return true;

    }

    public boolean ServerTest(DataOutputStream out, DataInputStream in   , int amountOfRequests, long sleepTime) throws IOException{
        ArrayList<String> lst = new ArrayList<>();

        lst.add("/create");
        lst.add("/refresh");
        lst.add("/join test");

        int n  = 0;

        do {
            int i = (int)(0 + (Math.random() * (3 - 0)));
            out.writeUTF(lst.get(i));
            out.flush();
            System.out.println(in.readUTF());
            try {
                TimeUnit.SECONDS.sleep(sleepTime);
            }catch (Exception e){
                e.printStackTrace();
            }

            n++;

        } while(n <= amountOfRequests);

        System.out.println(n +" requests sent.");
        Scanner scan = new Scanner(System.in);
        String str = scan.next();
        return false;

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
