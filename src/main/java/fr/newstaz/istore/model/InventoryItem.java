package fr.newstaz.istore.model;

/**
 * InventoryItem class to manage the inventory items
 *
 * @version 1.0
 */
public class InventoryItem {

    /**
     * The id of the item
     *
     * @see #getId()
     */
    private final int id;

    /**
     * The name of the item
     *
     * @see #getName()
     */
    private final String name;

    /**
     * The price of the item
     *
     * @see #getPrice()
     */
    private final int price;

    /**
     * The quantity of the item
     *
     * @see #getQuantity()
     * @see #setQuantity(int)
     */
    private int quantity;

    /**
     * Constructor with id, name, price and quantity
     *
     * @param id       the id of the item
     * @param name     the name of the item
     * @param price    the price of the item
     * @param quantity the quantity of the item
     */
    public InventoryItem(int id, String name, int price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * Constructor with id, name and price
     *
     * @param id    the id of the item
     * @param name  the name of the item
     * @param price the price of the item
     */
    public InventoryItem(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    /**
     * Get the id of the item
     *
     * @return the id of the item
     */
    public int getId() {
        return id;
    }

    /**
     * Get the name of the item
     *
     * @return the name of the item
     */
    public String getName() {
        return name;
    }

    /**
     * Get the price of the item
     *
     * @return the price of the item
     */
    public int getPrice() {
        return price;
    }

    /**
     * Get the quantity of the item
     *
     * @return the quantity of the item
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Set the quantity of the item
     *
     * @param quantity the quantity of the item
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
