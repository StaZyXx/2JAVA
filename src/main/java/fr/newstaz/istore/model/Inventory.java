package fr.newstaz.istore.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Inventory class to manage the inventory
 *
 * @version 1.0
 */
public class Inventory {

    /**
     * The id of the inventory
     *
     * @see #getId()
     */
    private final int id;

    /**
     * The id of the store
     *
     * @see #getStoreId()
     */
    private final int storeId;

    /**
     * The list of items
     *
     * @see #getItems()
     * @see #addItem(InventoryItem)
     * @see #removeItem(InventoryItem)
     */
    private final List<InventoryItem> items = new ArrayList<>();

    /**
     * Constructor with id and store id
     *
     * @param id      the id of the inventory
     * @param storeId the id of the store
     */
    public Inventory(int id, int storeId) {
        this.id = id;
        this.storeId = storeId;
    }

    /**
     * Constructor with store id
     *
     * @param storeId the id of the store
     */
    public Inventory(int storeId) {
        this.id = 0;
        this.storeId = storeId;
    }

    /**
     * Get the id of the inventory
     *
     * @return the id of the inventory
     */
    public int getId() {
        return id;
    }

    /**
     * Get the id of the store
     *
     * @return the id of the store
     */
    public int getStoreId() {
        return storeId;
    }

    /**
     * Get the list of items
     *
     * @return the list of items
     */
    public List<InventoryItem> getItems() {
        return items;
    }

    /**
     * Add an item to the inventory
     *
     * @param item the item to add
     */
    public void addItem(InventoryItem item) {
        items.add(item);
    }

    /**
     * Remove an item from the inventory
     *
     * @param item the item to remove
     */
    public void removeItem(InventoryItem item) {
        items.remove(item);
    }
}
