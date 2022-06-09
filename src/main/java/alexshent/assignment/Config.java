package alexshent.assignment;

/**
 * Configuration data
 */
public class Config {
    private Config() {

    }

    public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String URL = "jdbc:mysql://172.21.0.2:3306/app_db";
    public static final String USER = "db_user";
    public static final String PASSWORD = "1";
}
