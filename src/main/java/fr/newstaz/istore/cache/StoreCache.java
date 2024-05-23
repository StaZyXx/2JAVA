package fr.newstaz.istore.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import fr.newstaz.istore.dao.StoreDAO;
import fr.newstaz.istore.database.Database;
import fr.newstaz.istore.model.Store;
import fr.newstaz.istore.model.User;
import fr.newstaz.istore.repository.InventoryRepository;
import fr.newstaz.istore.repository.StoreRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * StoreCache class to manage the store cache
 *
 * @version 1.0
 * @see Store
 * @see StoreRepository
 */
public class StoreCache implements StoreRepository {

    /**
     * Cache of the stores (default expiration time: 10 minutes)
     *
     * @see Cache
     */
    private final Cache<String, List<Store>> stores;

    /**
     * StoreDAO instance
     *
     * @see StoreDAO
     */
    private final StoreDAO storeDAO;

    /**
     * StoreCache constructor
     *
     * @param database            the database
     * @param inventoryRepository the inventory repository
     */
    public StoreCache(Database database, InventoryRepository inventoryRepository) {
        this.stores = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
        this.storeDAO = new StoreDAO(database, inventoryRepository);
    }

    /**
     * Create a store
     * Cache is invalidated after the creation
     *
     * @param store the store to create
     * @return the created store
     */
    @Override
    public Store createStore(Store store) {
        Store newStore = storeDAO.createStore(store);

        stores.invalidateAll();

        return newStore;
    }

    /**
     * Get a store by name (from the cache if possible)
     *
     * @param name the name of the store
     * @return the store
     */
    @Override
    public Store getStore(String name) {
        List<Store> stores = this.stores.getIfPresent("stores");

        if (stores == null) {
            stores = storeDAO.getAllStores();

            this.stores.put("stores", stores);
        }

        return stores.stream().filter(store -> store.getName().equals(name)).findFirst().orElse(null);
    }

    /**
     * Delete a store
     * Cache is invalidated after the deletion
     *
     * @param store the store to delete
     */
    @Override
    public void deleteStore(Store store) {
        storeDAO.deleteStore(store);

        stores.invalidateAll();
    }

    /**
     * Get all the stores (from the cache if possible)
     *
     * @return the list of stores
     */
    @Override
    public List<Store> getAllStores() {
        List<Store> stores = this.stores.getIfPresent("stores");
        if (stores == null) {
            stores = storeDAO.getAllStores();
            this.stores.put("stores", stores);
        }
        return stores;
    }

    /**
     * Add an employee to a store
     * Cache is invalidated after the addition
     *
     * @param store the store
     * @param user  the user to add
     */
    @Override
    public void addEmployee(Store store, User user) {
        storeDAO.addEmployee(store, user);
        stores.invalidateAll();
    }

    /**
     * Check if an employee is already added to a store (from the cache if possible)
     *
     * @param user  the user
     * @param store the store
     * @return true if the employee is already added, false otherwise
     */
    @Override
    public boolean isEmployeeAlreadyAdded(User user, Store store) {
        List<Store> storesCache = stores.getIfPresent("stores");
        if (storesCache == null) {
            storesCache = storeDAO.getAllStores();
            stores.put("stores", storesCache);
        }
        return storesCache.stream().anyMatch(s -> s.getId() == store.getId() && s.getEmployees().stream().anyMatch(e -> e.getId() == user.getId()));
    }

    /**
     * Get the employees of a store
     *
     * @param store the store
     * @return the list of employees
     */
    @Override
    public List<User> getEmployees(Store store) {
        return storeDAO.getEmployees(store);
    }

    /**
     * Remove an employee from a store
     * Cache is invalidated after the removal
     *
     * @param store the store
     * @param user  the user to remove
     */
    @Override
    public void removeEmployee(Store store, User user) {
        storeDAO.removeEmployee(store, user);

        stores.invalidateAll();
    }

    @Override
    public List<User> getEmployeesPermissions(Store store) {
        return storeDAO.getEmployeesPermissions(store);
    }

    @Override
    public void addEmployeePermission(Store store, User user) {
        storeDAO.addEmployeePermission(store, user);

    }

    @Override
    public void removeEmployeePermission(Store store, User user) {
        storeDAO.removeEmployeePermission(store, user);
    }
}
