package co.wethinkcode.logisticsconnect.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Shared SQLite connection setup for every service. Each service is an
 * independent Maven project with no shared parent pom, so this class is
 * duplicated into each participating service's own source tree alongside
 * an `org.xerial:sqlite-jdbc` dependency in its pom:
 *
 * <pre>{@code
 * <dependency>
 *   <groupId>org.xerial</groupId>
 *   <artifactId>sqlite-jdbc</artifactId>
 *   <version>3.45.3.0</version>
 * </dependency>
 * }</pre>
 */
public class DatabaseConfig {

    private static final String DB_URL = "jdbc:sqlite:logisticsconnect.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("SQLite JDBC driver not found on classpath", e);
        }
    }

    private DatabaseConfig() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}