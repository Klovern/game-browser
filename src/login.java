import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import static java.lang.System.out;

/**
 * Created by Daniel on 2017-03-08.
 */
public class login extends Encryption{

    protected Socket client;
    private String username;
    private String password;

    public void logon() throws IOException {

        ClientHandler handler = new ClientHandler(client);


        System.out.println("Write your Name");
        username = System.console().readLine();
        System.out.println(username);
        System.out.println("Write your Password");
        password = System.console().readLine();


        // variables for encryption & encryption
        String key = "Bar12345Bar12345"; // 128 bit key
        String initVector = "RandomInitVector"; // 16 bytes IV
        String cryptedString = encrypt(key, initVector, password);

        System.out.println(password);
        System.out.println("Crypted password: "+cryptedString);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/hub_demo", "root", "43g_Sdff*SDFFdrf3sd");
            PreparedStatement pst = conn.prepareStatement("Select username,password from userdetails where username=? and password=?");
            pst.setString(1, username);
            pst.setString(2, cryptedString);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                out.println("Correct login credentials");
            }
            else {
                out.println("Incorrect login credentials");
            }

        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        handler.SetPassword(password);
        handler.SetUsername(username);
    }
}


