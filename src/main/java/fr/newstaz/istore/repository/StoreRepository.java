package fr.newstaz.istore.repository;

import fr.newstaz.istore.model.Store;
import fr.newstaz.istore.model.User;

import java.util.List;

/**
 * StoreRepository interface to manage the store repository
 *
 * @version 1.0
 * @see Store
 */
public interface StoreRepository {

    /**
     * Create a store
     *
     * @param store the store to create
     * @return the created store
     */
    Store createStore(Store store);

    /**
     * Get a store by name
     *
     * @param name the name of the store
     * @return the store
     */
    Store getStore(String name);

    /**
     * Update a store
     *
     * @param store the store to update
     */
    void deleteStore(Store store);

    /**
     * Get all stores
     *
     * @return the list of all stores
     */
    List<Store> getAllStores();

    /**
     * Add an employee to a store
     *
     * @param store the store
     * @param user  the user to add
     */
    void addEmployee(Store store, User user);

    /**
     * Check if an employee is already added to a store
     *
     * @param user  the user
     * @param store the store
     * @return true if the employee is already added, false otherwise
     */
    boolean isEmployeeAlreadyAdded(User user, Store store);

    /**
     * Get all employees of a store
     *
     * @param store the store
     * @return the list of all employees
     */
    List<User> getEmployees(Store store);

    /**
     * Remove an employee from a store
     *
     * @param store the store
     * @param user  the user to remove
     */
    void removeEmployee(Store store, User user);


    /**
     * Get all employees of a store
     *
     * @param store the store
     * @return the list of all employees
     */
    List<User> getEmployeesPermissions(Store store);

    /**
     * Add an employee to a store
     *
     * @param store the store
     * @param user  the user to add
     */
    void addEmployeePermission(Store store, User user);

    /**
     * Remove an employee from a store
     *
     * @param store the store
     * @param user  the user to remove
     */
    void removeEmployeePermission(Store store, User user);

}