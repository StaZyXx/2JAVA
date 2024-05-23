package fr.newstaz.istore.repository;

import fr.newstaz.istore.model.Inventory;
import fr.newstaz.istore.model.InventoryItem;

/**
 * InventoryRepository interface to manage the inventory repository
 *
 * @version 1.0
 * @see Inventory
 */
public interface InventoryRepository {

    /**
     * Create an inventory
     *
     * @param inventory the inventory to create
     */
    void createInventory(Inventory inventory);

    /**
     * Get an inventory by id
     *
     * @param id the id of the inventory
     * @return the inventory
     */
    Inventory getInventory(int id);

    /**
     * Update an inventory
     *
     * @param inventory the inventory to update
     */
    void updateInventory(Inventory inventory);

    /**
     * Delete an inventory
     *
     * @param inventory the inventory to delete
     */
    void deleteInventory(Inventory inventory);

    /**
     * Add an item to an inventory
     *
     * @param inventory the inventory
     * @param item      the item to add
     */
    void addItemToInventory(Inventory inventory, InventoryItem item);

    /**
     * Update an item in an inventory
     *
     * @param inventory the inventory
     * @param item      the item to update
     */
    void updateItemInInventory(Inventory inventory, InventoryItem item);

    /**
     * Delete an item from an inventory
     *
     * @param inventory the inventory
     * @param item      the item to delete
     */
    void deleteItemFromInventory(Inventory inventory, InventoryItem item);

}
