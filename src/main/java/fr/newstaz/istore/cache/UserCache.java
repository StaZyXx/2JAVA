package fr.newstaz.istore.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import fr.newstaz.istore.dao.UserDAO;
import fr.newstaz.istore.database.Database;
import fr.newstaz.istore.model.User;
import fr.newstaz.istore.repository.UserRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * UserCache class to manage the user cache
 *
 * @version 1.0
 * @see User
 * @see UserRepository
 */
public class UserCache implements UserRepository {

    /**
     * Cache of the users (default expiration time: 10 minutes)
     *
     * @see Cache
     */
    private final Cache<String, List<User>> users;

    /**
     * UserDAO instance
     *
     * @see UserDAO
     */
    private final UserDAO userDAO;

    /**
     * UserCache constructor
     *
     * @param database the database
     */
    public UserCache(Database database) {
        this.users = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
        this.userDAO = new UserDAO(database);

    }

    /**
     * Create a user
     * Cache is invalidated after the creation
     *
     * @param user the user to create
     */
    @Override
    public void createUser(User user) {
        userDAO.createUser(user);

        users.invalidateAll();
    }

    /**
     * Get a user by id
     *
     * @param id the id of the user
     * @return the user
     */
    @Override
    public User getUser(int id) {
        List<User> users = this.users.getIfPresent("users");

        if (users == null) {
            users = userDAO.getAllUsers();

            this.users.put("users", users);
        }

        return users.stream().filter(user -> user.getId() == id).findFirst().orElse(null);
    }

    /**
     * Get a user by login
     *
     * @param login the login of the user
     * @return the user
     */
    @Override
    public User getUser(String login) {
        List<User> users = this.users.getIfPresent("users");

        if (users == null) {
            users = userDAO.getAllUsers();

            this.users.put("users", users);
        }

        return users.stream().filter(user -> user.getEmail().equals(login)).findFirst().orElse(null);
    }

    /**
     * Get a user by id
     *
     * @param id the id of the user
     * @return the user
     */
    @Override
    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }

    /**
     * Update a user
     * Cache is invalidated after the update
     *
     * @param user the user to update
     */
    @Override
    public void updateUser(User user) {
        userDAO.updateUser(user);

        users.invalidateAll();
    }

    /**
     * Delete a user
     * Cache is invalidated after the deletion
     *
     * @param user the user to delete
     */
    @Override
    public void deleteUser(User user) {
        userDAO.deleteUser(user);

        users.invalidateAll();
    }

    /**
     * Get all the users (from the cache if possible)
     *
     * @return the list of users
     */
    @Override
    public List<User> getAllUsers() {
        List<User> users = this.users.getIfPresent("users");

        if (users == null) {
            users = userDAO.getAllUsers();

            this.users.put("users", users);
        }

        return users;
    }
}
