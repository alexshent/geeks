package alexshent.assignment;

import java.sql.*;

/**
 * Database connection class
 */
public class Database {

    /**
     * Connect to the database
     *
     * @return connection object
     */
    public static Connection getConnection() {
        try {
            Class.forName(Config.JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            return DriverManager.getConnection(Config.URL, Config.USER, Config.PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
