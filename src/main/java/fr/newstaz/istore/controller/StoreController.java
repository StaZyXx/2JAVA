package fr.newstaz.istore.controller;

import fr.newstaz.istore.model.Inventory;
import fr.newstaz.istore.model.InventoryItem;
import fr.newstaz.istore.model.Store;
import fr.newstaz.istore.model.User;
import fr.newstaz.istore.repository.Repository;
import fr.newstaz.istore.response.StoreResponse;

import java.util.List;
import java.util.function.Predicate;

/**
 * StoreController class to manage the store controller
 *
 * @version 1.0
 * @see Repository
 * @see Store
 */
public class StoreController {

    /**
     * Repository instance
     *
     * @see Repository
     */
    private final Repository repository;

    /**
     * StoreController constructor
     *
     * @param repository the repository
     */
    public StoreController(Repository repository) {
        this.repository = repository;
    }

    /**
     * Create a store
     *
     * @param name the name of the store
     * @return the create store response
     */
    public StoreResponse.CreateStoreResponse createStore(String name) {
        Store store = new Store(name);
        if (repository.getStoreRepository().getStore(name) != null) {
            return new StoreResponse.CreateStoreResponse(false, "Store already exists");
        }

        Store newStore = repository.getStoreRepository().createStore(store);
        repository.getInventoryRepository().createInventory(new Inventory(newStore.getId()));
        return new StoreResponse.CreateStoreResponse(true, "Store created");
    }

    /**
     * Delete a store
     *
     * @param store the store to delete
     * @return true if the store is deleted, false otherwise
     */
    public boolean deleteStore(Store store) {
        if (repository.getStoreRepository().getStore(store.getName()) == null) {
            return false;
        }
        repository.getStoreRepository().deleteStore(store);
        repository.getInventoryRepository().deleteInventory(store.getInventory());
        return true;
    }

    /**
     * Get a store by name
     *
     * @param name the name of the store
     * @return the store
     */
    public Store getStore(String name) {
        return repository.getStoreRepository().getStore(name);
    }

    /**
     * Get all stores
     *
     * @param predicate the predicate to filter the stores
     * @return the stores
     */
    public List<Store> getAllStore(Predicate<Store> predicate) {
        return repository.getStoreRepository().getAllStores().stream().filter(predicate).toList();
    }

    /**
     * Get all stores
     *
     * @return the stores
     */
    public List<Store> getAllStores() {
        return repository.getStoreRepository().getAllStores();
    }

    /**
     * Get all employees of a store
     *
     * @param store the store
     * @return the employees
     */
    public List<User> getEmployees(Store store) {
        return repository.getStoreRepository().getEmployees(store);
    }

    /**
     * Search stores
     *
     * @param text the text to search
     * @return the stores
     */
    public List<Store> searchStores(String text) {
        return getAllStore(user -> user.getName().toLowerCase().contains(text.toLowerCase()));
    }

    /**
     * Add an employee to a store
     *
     * @param store    the store
     * @param username the username of the user
     * @return the add employee response
     */
    public StoreResponse.AddEmployeeResponse addEmployee(Store store, String username) {
        if (repository.getUserRepository().getUser(username) == null) {
            return new StoreResponse.AddEmployeeResponse(false, "User not found");
        }
        if (repository.getStoreRepository().isEmployeeAlreadyAdded(repository.getUserRepository().getUser(username), store)) {
            return new StoreResponse.AddEmployeeResponse(false, "User already added");

        }
        repository.getStoreRepository().addEmployee(store, repository.getUserRepository().getUser(username));
        return new StoreResponse.AddEmployeeResponse(true, "User added");
    }

    /**
     * Remove an employee from a store
     *
     * @param store the store
     * @param user  the user
     * @return the remove employee response
     */
    public StoreResponse.RemoveEmployeeResponse removeEmployee(Store store, User user) {
        if (repository.getUserRepository().getUser(user.getEmail()) == null || repository.getStoreRepository().getStore(store.getName()) == null) {
            return new StoreResponse.RemoveEmployeeResponse(false, "User or store not found");
        }
        repository.getStoreRepository().removeEmployee(store, repository.getUserRepository().getUserById(user.getId()));
        return new StoreResponse.RemoveEmployeeResponse(true, "User removed");
    }

    /**
     * Create an inventory item
     *
     * @param store    the store
     * @param name     the name of the item
     * @param price    the price of the item
     * @param quantity the quantity of the item
     * @return the create inventory item response
     */
    public StoreResponse.CreateInventoryItemResponse createInventoryItem(Store store, String name, int price, int quantity) {
        if (repository.getStoreRepository().getStore(store.getName()) == null) {
            return new StoreResponse.CreateInventoryItemResponse(false, "Store not found");
        }
        if (repository.getInventoryRepository().getInventory(store.getId()).getItems().stream().filter(inventoryItem -> inventoryItem.getName() != null).anyMatch(inventoryItem -> inventoryItem.getName().equals(name))) {
            return new StoreResponse.CreateInventoryItemResponse(false, "Inventory item already exists");
        }
        repository.getInventoryRepository().addItemToInventory(store.getInventory(), new InventoryItem(0, name, price, quantity));
        return new StoreResponse.CreateInventoryItemResponse(true, "Inventory item created");
    }

    /**
     * Update an inventory item
     *
     * @param store         the store
     * @param inventoryItem the inventory item
     * @param quantity      the quantity of the item
     * @return the update inventory item response
     */
    public StoreResponse.UpdateInventoryItemResponse updateInventoryItem(Store store, InventoryItem inventoryItem, int quantity) {
        if (repository.getStoreRepository().getStore(store.getName()) == null) {
            return new StoreResponse.UpdateInventoryItemResponse(false, "Store not found");
        }
        if (repository.getInventoryRepository().getInventory(store.getId()).getItems().stream().filter(inventoryItem1 -> inventoryItem1.getName() != null).noneMatch(inventoryItem1 -> inventoryItem1.getName().equals(inventoryItem.getName()))) {
            return new StoreResponse.UpdateInventoryItemResponse(false, "Inventory item not found");
        }
        repository.getInventoryRepository().updateItemInInventory(store.getInventory(), new InventoryItem(inventoryItem.getId(), inventoryItem.getName(), inventoryItem.getPrice(), quantity));
        return new StoreResponse.UpdateInventoryItemResponse(true, "Inventory item updated");
    }

    /**
     * Remove an inventory item
     *
     * @param store         the store
     * @param inventoryItem the inventory item
     * @return the remove inventory item response
     */
    public StoreResponse.DeleteInventoryItemResponse removeInventoryItem(Store store, InventoryItem inventoryItem) {
        if (repository.getStoreRepository().getStore(store.getName()) == null) {
            return new StoreResponse.DeleteInventoryItemResponse(false, "Store not found");
        }
        if (repository.getInventoryRepository().getInventory(store.getId()).getItems().stream().filter(inventoryItem1 -> inventoryItem1.getName() != null).noneMatch(inventoryItem1 -> inventoryItem1.getName().equals(inventoryItem.getName()))) {
            return new StoreResponse.DeleteInventoryItemResponse(false, "Inventory item not found");
        }
        repository.getInventoryRepository().deleteItemFromInventory(store.getInventory(), new InventoryItem(inventoryItem.getId(), inventoryItem.getName(), inventoryItem.getPrice(), inventoryItem.getQuantity()));
        return new StoreResponse.DeleteInventoryItemResponse(true, "Inventory item deleted");
    }

    public StoreResponse.AddPermissionResponse addPermission(Store store, User user) {
        if (repository.getStoreRepository().getStore(store.getName()) == null) {
            return new StoreResponse.AddPermissionResponse(false, "Store not found");
        }
        repository.getStoreRepository().addEmployeePermission(store, user);
        return new StoreResponse.AddPermissionResponse(true, "User added");
    }

    public List<User> getEmployeesPermissions(Store store) {
        return repository.getStoreRepository().getEmployeesPermissions(store);
    }

    public StoreResponse.RemovePermissionResponse removePermission(Store store, User user) {
        if (repository.getStoreRepository().getStore(store.getName()) == null) {
            return new StoreResponse.RemovePermissionResponse(false, "Store not found");
        }
        repository.getStoreRepository().removeEmployeePermission(store, user);
        return new StoreResponse.RemovePermissionResponse(true, "User removed");
    }


}
