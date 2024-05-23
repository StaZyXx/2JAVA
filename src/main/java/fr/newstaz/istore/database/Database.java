package fr.newstaz.istore.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Database class to manage the database
 *
 * @version 1.0
 */
public class Database {

    /**
     * The connection to the database
     *
     * @see #getConnection()
     */
    private final Connection connection;

    /**
     * The executor to execute tasks (async)
     */
    private final Executor executor = new ScheduledThreadPoolExecutor(2);

    /**
     * Constructor with url, user and password
     *
     * @param url      the url of the database
     * @param user     the user of the database
     * @param password the password of the database
     * @throws SQLException if the connection to the database fails
     */
    public Database(String url, String user, String password) throws SQLException {
        this.connection = DriverManager.getConnection(url, user, password);
    }

    /**
     * Get the connection to the database
     *
     * @return the connection to the database
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Execute a runnable task
     *
     * @param runnable the task to execute
     */
    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }
}
