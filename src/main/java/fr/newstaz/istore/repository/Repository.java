package fr.newstaz.istore.repository;

import fr.newstaz.istore.cache.StoreCache;
import fr.newstaz.istore.cache.UserCache;
import fr.newstaz.istore.dao.InventoryDAO;
import fr.newstaz.istore.database.Database;


/**
 * Repository class to manage the repositories
 * <p>
 * see {@link UserRepository}
 * see {@link StoreRepository}
 * see {@link InventoryRepository}
 * </p>
 *
 * @version 1.0
 */
public class Repository {

    /**
     * UserRepository instance
     *
     * @see UserRepository
     */
    private final UserRepository userRepository;

    /**
     * StoreRepository instance
     *
     * @see StoreRepository
     */
    private final StoreRepository storeRepository;

    /**
     * InventoryRepository instance
     *
     * @see InventoryRepository
     */
    private final InventoryRepository inventoryRepository;

    /**
     * Constructor
     *
     * @param database the database
     */
    public Repository(Database database) {
        this.userRepository = new UserCache(database);
        this.inventoryRepository = new InventoryDAO(database);
        this.storeRepository = new StoreCache(database, inventoryRepository);
    }

    /**
     * Get the UserRepository
     *
     * @return the UserRepository
     */
    public UserRepository getUserRepository() {
        return userRepository;
    }

    /**
     * Get the StoreRepository
     *
     * @return the StoreRepository
     */
    public StoreRepository getStoreRepository() {
        return storeRepository;
    }

    /**
     * Get the InventoryRepository
     *
     * @return the InventoryRepository
     */
    public InventoryRepository getInventoryRepository() {
        return inventoryRepository;
    }
}
