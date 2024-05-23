package fr.newstaz.istore.dao;

import fr.newstaz.istore.database.Database;
import fr.newstaz.istore.model.Store;
import fr.newstaz.istore.model.User;
import fr.newstaz.istore.repository.InventoryRepository;
import fr.newstaz.istore.repository.StoreRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * StoreDAO class to manage the store DAO
 *
 * @version 1.0
 * @see StoreRepository
 * @see Store
 */
public class StoreDAO implements StoreRepository {

    /**
     * Database instance
     *
     * @see Database
     */
    private final Database database;

    /**
     * InventoryRepository instance
     *
     * @see InventoryRepository
     */
    private final InventoryRepository inventoryRepository;

    public StoreDAO(Database database, InventoryRepository inventoryRepository) {
        this.database = database;
        this.inventoryRepository = inventoryRepository;
        createTable();
    }

    /**
     * Create a store
     *
     * @param store the store to create
     * @return the created store
     */
    @Override
    public Store createStore(Store store) {
        Connection connection = database.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO stores (name) VALUES (?)")) {
            statement.setString(1, store.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return getStore(store.getName());
    }

    /**
     * Get a store by name
     *
     * @param name the name of the store
     * @return the store
     */
    @Override
    public Store getStore(String name) {
        Connection connection = database.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM stores WHERE name = ?")) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Store(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Delete a store and its employees
     *
     * @param store the store to delete
     */
    @Override
    public void deleteStore(Store store) {
        Connection connection = database.getConnection();
        database.execute(() -> {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM stores WHERE id = ?")) {
                statement.setInt(1, store.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM stores_employee WHERE store_id = ?")) {
                statement.setInt(1, store.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Get all stores
     *
     * @return the list of stores
     */
    @Override
    public List<Store> getAllStores() {
        Connection connection = database.getConnection();
        List<Store> stores = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM stores")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Store store = new Store(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );
                store.setInventory(inventoryRepository.getInventory(store.getId()));
                store.setEmployees(getEmployees(store));
                stores.add(store);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stores;
    }

    /**
     * Add an employee to a store
     *
     * @param store the store
     * @param user  the user to add
     */
    @Override
    public void addEmployee(Store store, User user) {
        Connection connection = database.getConnection();
        database.execute(() -> {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO stores_employee (store_id, employee_id) VALUES (?, ?)")) {
                statement.setInt(1, store.getId());
                statement.setInt(2, user.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Remove an employee from a store
     *
     * @param store the store
     * @param user  the user to remove
     */
    @Override
    public void removeEmployee(Store store, User user) {
        Connection connection = database.getConnection();
        database.execute(() -> {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM stores_employee WHERE store_id = ? AND employee_id = ?")) {
                statement.setInt(1, store.getId());
                statement.setInt(2, user.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     *
     * @param store the store
     * @return the list of all employees
     */
    @Override
    public List<User> getEmployeesPermissions(Store store) {
        Connection connection = database.getConnection();
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT u.* FROM users u " +
                        "LEFT JOIN users_permission up ON u.id = up.user_id " +
                        "WHERE up.store_id = ?")) {
            statement.setInt(1, store.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        User.Role.valueOf(resultSet.getString("role")),
                        resultSet.getBoolean("is_verified")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    /**
     * Add an employee to a store
     *
     * @param store the store
     * @param user  the user to add
     */
    @Override
    public void addEmployeePermission(Store store, User user) {
        Connection connection = database.getConnection();
        database.execute(() -> {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO users_permission (store_id, user_id) VALUES (?, ?)")) {
                statement.setInt(1, store.getId());
                statement.setInt(2, user.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });

    }

    /**
     * Get all employees of a store
     *
     * @param store the store
     * @return the list of all employees
     */
    @Override
    public void removeEmployeePermission(Store store, User user) {
        Connection connection = database.getConnection();
        database.execute(() -> {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM users_permission WHERE store_id = ? AND user_id = ?")) {
                statement.setInt(1, store.getId());
                statement.setInt(2, user.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Check if an employee is already added to a store
     *
     * @param user  the user
     * @param store the store
     * @return true if the employee is already added, false otherwise
     */
    @Override
    public boolean isEmployeeAlreadyAdded(User user, Store store) {
        Connection connection = database.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM stores_employee WHERE store_id = ? AND employee_id = ?")) {
            statement.setInt(1, store.getId());
            statement.setInt(2, user.getId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * Get the employees of a store
     *
     * @param store the store
     * @return the list of employees
     */
    @Override
    public List<User> getEmployees(Store store) {
        Connection connection = database.getConnection();
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT u.* FROM users u " +
                        "LEFT JOIN stores_employee se ON u.id = se.employee_id " +
                        "WHERE se.store_id = ?")) {
            statement.setInt(1, store.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        User.Role.valueOf(resultSet.getString("role")),
                        resultSet.getBoolean("is_verified")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    /**
     * Create the stores and stores_employee tables
     */
    public void createTable() {
        Connection connection = database.getConnection();
        database.execute(() -> {
            try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS stores (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) UNIQUE NOT NULL)"
            )) {
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS stores_employee (id INT PRIMARY KEY AUTO_INCREMENT, store_id INT NOT NULL, employee_id INT NOT NULL, FOREIGN KEY (store_id) REFERENCES stores(id), FOREIGN KEY (employee_id) REFERENCES employees(id))"
            )) {
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS users_permission (id INT PRIMARY KEY AUTO_INCREMENT, store_id INT NOT NULL, user_id INT NOT NULL, FOREIGN KEY (store_id) REFERENCES stores(id), FOREIGN KEY (user_id) REFERENCES users(id))"
            )) {
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);

            }
        });
    }

}