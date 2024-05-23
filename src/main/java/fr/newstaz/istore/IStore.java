package fr.newstaz.istore;

import fr.newstaz.istore.controller.Controller;
import fr.newstaz.istore.database.Database;
import fr.newstaz.istore.repository.Repository;
import fr.newstaz.istore.ui.MainFrame;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.sql.SQLException;

/**
 * IStore class to manage the application
 *
 * @version 1.0
 */
public class IStore {

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        }

        System.out.println("MySQL JDBC Driver Registered!");

        AppConfig appConfig = new AppConfig();

        String url = appConfig.getDatabaseUrl();
        String username = appConfig.getDatabaseUsername();
        String password = appConfig.getDatabasePassword();

        Database database;
        try {
            database = new Database(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String passwordHash = BCrypt.hashpw("admin", BCrypt.gensalt());
        String createAdmin = "INSERT INTO users (email, password, role, is_verified) VALUES ('admin', '"+passwordHash+"', 'ADMIN', true) ON DUPLICATE KEY UPDATE email = 'admin', password = '"+passwordHash+"', role = 'ADMIN', is_verified = true";

        database.execute(() -> {
            try (var statement = database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY AUTO_INCREMENT, email VARCHAR(255) UNIQUE NOT NULL, password VARCHAR(255) NOT NULL, role VARCHAR(255), is_verified BOOLEAN)")) {
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try (var statement = database.getConnection().prepareStatement(createAdmin)) {
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        Repository repository = new Repository(database);

        Controller controller = new Controller(repository);

        SwingUtilities.invokeLater(() -> {
            new MainFrame(controller);
        });
    }
}
