import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Wrapper;
import java.util.Scanner;

/**
 * Created by Daniel on 2017-02-01.
 */

public class register extends Encryption {
    // database URL
    static final String DB_URL = "jdbc:mysql://localhost/hub_demo";
    //  Database credentials
    static final String USER = "root";
    static final String PASS = "43g_Sdff*SDFFdrf3sd";
    protected static final String String = null;

    public void registerUser(String username,String password) {


        //ClientHandler handler = new ClientHandler(client);

        Wrapper conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try  //try block
        {
            //declare variables
            /*
            Scanner reader = new Scanner(System.in);
            System.out.println("Write your Name");
            String username = reader.nextLine();
            System.out.println("Write your Password");
            String password = reader.nextLine();
            System.out.println(username);
            System.out.println(password);
            */

            // variables for encryption & encryption
            String key = "Bar12345Bar12345"; // 128 bit key
            String initVector = "RandomInitVector"; // 16 bytes IV
            String cryptedString = encrypt(key, initVector, password);

            // check condition it field equals to blank throw error message
            if (username.equals("") || password.equals("")) {
                System.out.println("name or password or Role is wrong");
            } else  //else insert query is run properly
            {
                String IQuery = "INSERT INTO `hub_demo`.`userdetails`(`username`,`password`,`timestamp`) VALUES('" + username + "', '" + cryptedString + "',current_timestamp)";
                // System.out.println(IQuery); //print on console
                System.out.println("Connecting to a selected database...");

                //STEP 3: Open a connection
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                System.out.println("Connected database successfully...");

                ((Connection) conn).createStatement().execute(IQuery);//select the rows
                // define SMessage variable
                String SMessage = "Record added for " + username;

                // create dialog ox which is print message
                System.out.println(SMessage);
                //close connection
                ((java.sql.Connection) conn).close();
                //handler.setUsername(username);
                //handler.setPassword(password);
            }
        } catch (SQLException se) {
            //handle errors for JDBC
            se.printStackTrace();
        } catch (Exception a) //catch block
        {
            a.printStackTrace();
        }
    }
}
    /**
     * Launch the application.
     */