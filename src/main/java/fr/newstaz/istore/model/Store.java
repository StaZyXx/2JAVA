package fr.newstaz.istore.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Store class to manage the store
 *
 * @version 1.0
 */
public class Store {

    /**
     * The id of the store
     *
     * @see #getId()
     */
    private int id;

    /**
     * The name of the store
     *
     * @see #getName()
     */
    private final String name;

    /**
     * The list of employees
     *
     * @see #getEmployees()
     * @see #setEmployees(List)
     * @see #addEmployee(User)
     * @see #removeEmployee(User)
     * @see #isEmployee(User)
     * @see #isEmployee(String)
     */
    private final List<User> employees = new ArrayList<>();

    /**
     * The inventory of the store
     *
     * @see #getInventory()
     * @see #setInventory(Inventory)
     */
    private Inventory inventory;

    /**
     * Constructor with name
     *
     * @param name the name of the store
     */
    public Store(String name) {
        this.name = name;
    }

    /**
     * Constructor with id and name
     *
     * @param id   the id of the store
     * @param name the name of the store
     */
    public Store(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Get the id of the store
     *
     * @return the id of the store
     */
    public int getId() {
        return id;
    }

    /**
     * Get the name of the store
     *
     * @return the name of the store
     */
    public String getName() {
        return name;
    }

    /**
     * Get the list of employees
     *
     * @return the list of employees
     */
    public List<User> getEmployees() {
        return employees;
    }

    /**
     * Set the list of employees
     *
     * @param employees the list of employees
     */
    public void setEmployees(List<User> employees) {
        this.employees.clear();
        this.employees.addAll(employees);
    }

    /**
     * Add an employee
     *
     * @param user the user to add
     */
    public void addEmployee(User user) {
        employees.add(user);
    }

    /**
     * Remove an employee
     *
     * @param user the user to remove
     */
    public void removeEmployee(User user) {
        employees.remove(user);
    }

    /**
     * Check if a user is an employee
     *
     * @param user the user
     * @return true if the user is an employee, false otherwise
     */
    public boolean isEmployee(User user) {
        return employees.contains(user);
    }

    /**
     * Check if a user is an employee
     *
     * @param email the email of the user
     * @return true if the user is an employee, false otherwise
     */
    public boolean isEmployee(String email) {
        return employees.stream().anyMatch(user -> user.getEmail().equals(email));
    }

    /**
     * Get the inventory of the store
     *
     * @return the inventory of the store
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Set the inventory of the store
     *
     * @param inventory the inventory of the store
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
