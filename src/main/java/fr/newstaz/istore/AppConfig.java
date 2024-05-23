package fr.newstaz.istore;

import java.io.InputStream;
import java.util.Properties;

/**
 * AppConfig class to manage the application configuration
 *
 * @version 1.0
 */
public class AppConfig {

    private static final String CONFIG_FILE = "config.properties";

    /**
     * The properties of the configuration
     *
     * @see #getDatabaseUrl()
     * @see #getDatabaseUsername()
     * @see #getDatabasePassword()
     */
    private Properties properties;

    /**
     * Constructor to load the configuration
     */
    public AppConfig() {
        loadConfig();
    }

    /**
     * Load the configuration
     */
    private void loadConfig() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + CONFIG_FILE);
                return;
            }
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the database url
     *
     * @return the database url
     */
    public String getDatabaseUrl() {
        return properties.getProperty("database.url");
    }

    /**
     * Get the database username
     *
     * @return the database username
     */
    public String getDatabaseUsername() {
        return properties.getProperty("database.username");
    }

    /**
     * Get the database password
     *
     * @return the database password
     */
    public String getDatabasePassword() {
        return properties.getProperty("database.password");
    }
}
