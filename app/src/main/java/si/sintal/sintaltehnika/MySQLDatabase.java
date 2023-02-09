package si.sintal.sintaltehnika;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLDatabase {

    private static String driver = "com.mysql.jdbc.Driver";
    private static String connection = "jdbc:mysql://tehnika.sintal.si:3306"; //'milon' is your database name
    private static String user = "sintal87_tehnika";                  //'root' is username
    private static String password = "64AyUev83zg78s4";        //'pass' is password


    private static Connection con = null;
    private static Statement state = null;
    private static ResultSet result;
    private static PreparedStatement pstate;

    public static void mysqlConnect(){
        try{
            Class.forName(driver);
            con = DriverManager.getConnection(connection, user, password);
            System.out.println("Successfully connected to database.");
        }
        catch(ClassNotFoundException e){
                System.err.println("Couldn't load driver.");
        }
        catch(SQLException e){
            System.err.println("Couldn't connect to database.");
        }
    }


    public static void closeConnection(){
        try{
            if(!con.isClosed()){
                con.close();
                System.out.println("Database closed successfully.");
            }
        }
        catch(NullPointerException e){
            System.err.println("Couldn't load driver.");
        }
        catch(SQLException e){
            System.err.println("Couldn't close database.");
        }
    }

}
