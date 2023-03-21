/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thogakade.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author cmjd
 */
public class DBConnection {

    private static DBConnection dBConnection;
    private Connection connection;
    
       static final String JDBC_DRIVER = "org.h2.Driver";   
	   static final String DB_URL = "jdbc:h2:~/dark_download";  

    private DBConnection() throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        connection = DriverManager.getConnection(DB_URL, "admin", "admin");
    }

    private Connection getConnect() {
        return connection;
    }

    private static DBConnection getDBConnection() throws ClassNotFoundException, SQLException {
        if (dBConnection == null) {
            dBConnection = new DBConnection();
        }
        return dBConnection;
    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        return DBConnection.getDBConnection().getConnect();
    }
}
