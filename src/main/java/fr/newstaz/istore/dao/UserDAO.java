package fr.newstaz.istore.dao;

import fr.newstaz.istore.database.Database;
import fr.newstaz.istore.model.User;
import fr.newstaz.istore.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO class to manage the user DAO
 *
 * @version 1.0
 * @see UserRepository
 * @see User
 */
public class UserDAO implements UserRepository {

    /**
     * Database instance
     *
     * @see Database
     */
    private final Database database;

    /**
     * UserDAO constructor
     *
     * @param database the database
     */
    public UserDAO(Database database) {
        this.database = database;
        createUserTable();
    }

    /**
     * Create a user
     *
     * @param user the user to create
     */
    @Override
    public void createUser(User user) {
        Connection connection = database.getConnection();

        database.execute(() -> {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO users (email, password, role, is_verified) VALUES (?, ?, ?, ?)")) {
                statement.setString(1, user.getEmail());
                statement.setString(2, user.getPassword());
                statement.setString(3, user.getRole().name());
                statement.setBoolean(4, user.isVerified());

                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Get a user by id
     *
     * @param id the id of the user
     * @return the user
     */
    @Override
    public User getUser(int id) {
        Connection connection = database.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        User.Role.valueOf(resultSet.getString("role") == null ? "USER" : resultSet.getString("role")),
                        resultSet.getBoolean("is_verified")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Get a user by login
     *
     * @param login the login of the user
     * @return the user
     */
    @Override
    public User getUser(String login) {
        Connection connection = database.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email = ?")) {
            statement.setString(1, login);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        User.Role.valueOf(resultSet.getString("role") == null ? "USER" : resultSet.getString("role")),
                        resultSet.getBoolean("is_verified")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Get a user by id
     *
     * @param id the id of the user
     */
    @Override
    public User getUserById(int id) {
        Connection connection = database.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        User.Role.valueOf(resultSet.getString("role") == null ? "USER" : resultSet.getString("role")),
                        resultSet.getBoolean("is_verified")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Update a user
     *
     * @param user the user to update
     */
    @Override
    public void updateUser(User user) {
        Connection connection = database.getConnection();

        database.execute(() -> {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE users SET email = ?, password = ?, role = ?, is_verified = ? WHERE id = ?")) {
                statement.setString(1, user.getEmail());
                statement.setString(2, user.getPassword());
                statement.setString(3, user.getRole().name());
                statement.setBoolean(4, user.isVerified());
                statement.setInt(5, user.getId());

                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Delete a user
     *
     * @param user the user to delete
     */
    @Override
    public void deleteUser(User user) {
        Connection connection = database.getConnection();

        database.execute(() -> {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id = ?")) {
                statement.setInt(1, user.getId());

                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Get all users
     *
     * @return the list of all users
     */
    @Override
    public List<User> getAllUsers() {
        Connection connection = database.getConnection();
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users")) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        User.Role.valueOf(resultSet.getString("role") == null ? "USER" : resultSet.getString("role")),
                        resultSet.getBoolean("is_verified")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    /**
     * Create the user table
     */
    private void createUserTable() {
        Connection connection = database.getConnection();

        database.execute(() -> {
            try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY AUTO_INCREMENT, email VARCHAR(255), password VARCHAR(255), role VARCHAR(255), is_verified BOOLEAN)")) {
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
