package fr.newstaz.istore.dao;

import fr.newstaz.istore.database.Database;
import fr.newstaz.istore.model.Inventory;
import fr.newstaz.istore.model.InventoryItem;
import fr.newstaz.istore.repository.InventoryRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * InventoryDAO class to manage the inventory DAO
 *
 * @version 1.0
 * @see Inventory
 * @see InventoryRepository
 */
public class InventoryDAO implements InventoryRepository {

    /**
     * Database instance
     *
     * @see Database
     */
    private final Database database;

    /**
     * InventoryDAO constructor
     *
     * @param database the database
     */
    public InventoryDAO(Database database) {
        this.database = database;
        createTable();
    }

    /**
     * Create an inventory
     *
     * @param inventory the inventory to create
     */
    @Override
    public void createInventory(Inventory inventory) {
        Connection connection = database.getConnection();
        database.execute(() -> {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO inventory (id, store_id) VALUES (?, ?)")) {
                statement.setInt(1, inventory.getId());
                statement.setInt(2, inventory.getStoreId());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            for (InventoryItem item : inventory.getItems()) {
                try {
                    createInventoryItem(connection, inventory.getId(), item);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Get an inventory by id
     *
     * @param id the id of the inventory
     * @return the inventory
     */
    @Override
    public Inventory getInventory(int id) {
        Connection connection = database.getConnection();
        Inventory inventory = null;

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM inventory " +
                        "LEFT JOIN inventory_items ON inventory.id = inventory_items.inventory_id " +
                        "WHERE inventory.store_id = ?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                inventory = new Inventory(resultSet.getInt("inventory.id"), resultSet.getInt("inventory.store_id"));

                do {
                    InventoryItem item = new InventoryItem(
                            resultSet.getInt("inventory_items.id"),
                            resultSet.getString("inventory_items.name"),
                            resultSet.getInt("inventory_items.price")
                    );
                    item.setQuantity(resultSet.getInt("inventory_items.quantity"));
                    inventory.addItem(item);
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return inventory;
    }

    /**
     * Update an inventory
     *
     * @param inventory the inventory to update
     */
    @Override
    public void updateInventory(Inventory inventory) {
        Connection connection = database.getConnection();
        database.execute(() -> {
            for (InventoryItem item : inventory.getItems()) {
                try {
                    updateInventoryItem(connection, inventory.getId(), item);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Delete an inventory
     *
     * @param inventory the inventory to delete
     */
    @Override
    public void deleteInventory(Inventory inventory) {
        Connection connection = database.getConnection();
        database.execute(() -> {
            try (PreparedStatement deleteItemsStatement = connection.prepareStatement("DELETE FROM inventory_items WHERE inventory_id = ?")) {
                deleteItemsStatement.setInt(1, inventory.getId());
                deleteItemsStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try (PreparedStatement deleteInventoryStatement = connection.prepareStatement("DELETE FROM inventory WHERE id = ?")) {
                deleteInventoryStatement.setInt(1, inventory.getId());
                deleteInventoryStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Add an item to an inventory
     *
     * @param inventory the inventory
     * @param item      the item to add
     */
    @Override
    public void addItemToInventory(Inventory inventory, InventoryItem item) {
        Connection connection = database.getConnection();
        database.execute(() -> {
            try {
                createInventoryItem(connection, inventory.getId(), item);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Update an item in an inventory
     *
     * @param inventory the inventory
     * @param item      the item to update
     */
    @Override
    public void updateItemInInventory(Inventory inventory, InventoryItem item) {
        Connection connection = database.getConnection();
        database.execute(() -> {
            try {
                updateInventoryItem(connection, inventory.getId(), item);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public void deleteItemFromInventory(Inventory inventory, InventoryItem item) {
        Connection connection = database.getConnection();
        database.execute(() -> {
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM inventory_items WHERE inventory_id = ? AND id = ?")) {
                statement.setInt(1, inventory.getId());
                statement.setInt(2, item.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }

    /**
     * Create the inventory table
     */
    private void createTable() {
        Connection connection = database.getConnection();
        database.execute(() -> {
            try {
                connection.createStatement().executeUpdate(
                        "CREATE TABLE IF NOT EXISTS inventory (id INTEGER PRIMARY KEY AUTO_INCREMENT, store_id INTEGER)"
                );
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            try {
                connection.createStatement().executeUpdate(
                        "CREATE TABLE IF NOT EXISTS inventory_items (" +
                                "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                                "inventory_id INTEGER, " +
                                "name VARCHAR(255), " +
                                "price INTEGER, " +
                                "quantity INTEGER, " +
                                "FOREIGN KEY (inventory_id) REFERENCES inventory(id))"
                );
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Create an inventory item
     *
     * @param connection  the connection
     * @param inventoryId the inventory id
     * @param item        the item to create
     * @throws SQLException if a database access error occurs
     */
    private void createInventoryItem(Connection connection, int inventoryId, InventoryItem item) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO inventory_items (inventory_id, name, price, quantity) VALUES (?, ?, ?, ?)")) {
            statement.setInt(1, inventoryId);
            statement.setString(2, item.getName());
            statement.setInt(3, item.getPrice());
            statement.setInt(4, item.getQuantity());
            statement.executeUpdate();
        }
    }

    /**
     * Update an inventory item
     *
     * @param connection  the connection
     * @param inventoryId the inventory id
     * @param item        the item to update
     * @throws SQLException if a database access error occurs
     */
    private void updateInventoryItem(Connection connection, int inventoryId, InventoryItem item) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE inventory_items SET name = ?, price = ?, quantity = ? " +
                        "WHERE inventory_id = ? AND id = ?")) {
            statement.setString(1, item.getName());
            statement.setInt(2, item.getPrice());
            statement.setInt(3, item.getQuantity());
            statement.setInt(4, inventoryId);
            statement.setInt(5, item.getId());
            statement.executeUpdate();
        }
    }
}
