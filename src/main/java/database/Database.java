package database;

/**
 * Database class to be extended for access to the risiko-database.db
 *
 * @author jorohr
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import static general.Parameter.*;


public class Database {
	
    protected Connection connection;
    protected Properties properties;
    
    public Database() {
        this.connect(resourcesdir + dbName);
        properties = new Properties();
        properties.setProperty("PRAGMA foreign_keys", "ON");
    }

    private void connect(String file) {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + file);
            connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Database was opened"); 
    }
    
    protected void disconnect(){
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Database was closed");
    }   
}
