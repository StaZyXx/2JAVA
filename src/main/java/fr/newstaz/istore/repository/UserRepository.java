package fr.newstaz.istore.repository;

import fr.newstaz.istore.model.User;

import java.util.List;

/**
 * UserRepository interface to manage the user repository
 *
 * @version 1.0
 * @see User
 */
public interface UserRepository {

    /**
     * Create a user
     *
     * @param user the user to create
     */
    void createUser(User user);

    /**
     * Get a user by id
     *
     * @param id the id of the user
     * @return the user
     */
    User getUser(int id);

    /**
     * Get a user by login
     *
     * @param login the login of the user
     * @return the user
     */
    User getUser(String login);

    /**
     * Get a user by id
     *
     * @param id the id of the user
     */
    User getUserById(int id);

    /**
     * Update a user
     *
     * @param user the user to update
     */
    void updateUser(User user);

    /**
     * Delete a user
     *
     * @param user the user to delete
     */
    void deleteUser(User user);

    /**
     * Get all users
     *
     * @return the list of all users
     */
    List<User> getAllUsers();
}
