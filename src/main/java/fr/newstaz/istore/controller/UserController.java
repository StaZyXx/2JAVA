package fr.newstaz.istore.controller;

import fr.newstaz.istore.model.User;
import fr.newstaz.istore.repository.Repository;
import fr.newstaz.istore.response.UserResponse;
import fr.newstaz.istore.validator.UserValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.function.Predicate;

/**
 * UserController class to manage the user controller
 *
 * @version 1.0
 * @see Repository
 * @see User
 */
public class UserController {

    /**
     * Repository instance
     *
     * @see Repository
     */
    private final Repository repository;

    /**
     * UserController constructor
     *
     * @param repository the repository
     */
    public UserController(Repository repository) {
        this.repository = repository;
    }

    /**
     * Create a user
     *
     * @param user the user to create
     * @return the create user response
     */
    public UserResponse.CreateUserResponse createUser(User user) {
        UserResponse userResponse = validateUser(user);
        if (!userResponse.success()) {
            return new UserResponse.CreateUserResponse(false, userResponse.message());
        }

        if (repository.getUserRepository().getUser(user.getEmail()) != null) {
            return new UserResponse.CreateUserResponse(false, "User already exists");
        }

        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

        repository.getUserRepository().createUser(user);
        return new UserResponse.CreateUserResponse(true, "User created");
    }

    /**
     * Get all users
     *
     * @return the list of all users
     */
    public List<User> getAllUsers() {
        return repository.getUserRepository().getAllUsers();
    }

    /**
     * Get all users by predicate
     *
     * @param predicate the predicate
     * @return the list of all users
     */
    public List<User> getAllUsers(Predicate<User> predicate) {
        return repository.getUserRepository().getAllUsers().stream().filter(predicate).toList();
    }

    /**
     * Search users
     *
     * @param text the text to search
     * @return the list of users
     */
    public List<User> searchUsers(String text) {
        return getAllUsers(user -> user.getEmail().toLowerCase().contains(text.toLowerCase()));
    }

    /**
     * Edit a user
     *
     * @param user     the user to edit
     * @param email    the new email
     * @param password the new password
     * @param role     the new role
     * @return the edit user response
     */
    public UserResponse.EditUserResponse editUser(User user, String email, String password, User.Role role) {
        User newUser = new User(user);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setRole(role);
        UserResponse userResponse = validateUser(newUser);

        if (!userResponse.success()) {
            return new UserResponse.EditUserResponse(false, userResponse.message());
        }

        if (repository.getUserRepository().getUser(email) != null && !email.equals(user.getEmail())) {
            return new UserResponse.EditUserResponse(false, "User already exists");
        }

        newUser.setPassword(BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt()));
        repository.getUserRepository().updateUser(newUser);
        return new UserResponse.EditUserResponse(true, "User edited");
    }

    /**
     * Delete a user
     *
     * @param user the user to delete
     * @return the delete user response
     */
    public UserResponse.DeleteUserResponse deleteUser(User user) {
        if (repository.getUserRepository().getUser(user.getEmail()) == null) {
            return new UserResponse.DeleteUserResponse(false, "User not found");
        }
        repository.getUserRepository().deleteUser(user);
        return new UserResponse.DeleteUserResponse(true, "User deleted");
    }

    /**
     * Validate a user
     *
     * @param user the user to validate
     * @return the user response
     */
    private UserResponse validateUser(User user) {
        String email = user.getEmail();
        String password = user.getPassword();

        if (email == null || email.isEmpty()) {
            return new UserResponse(false, "Email is empty");
        }

        if (password == null || password.isEmpty()) {
            return new UserResponse(false, "Password is empty");
        }

        if (!UserValidator.isValidEmail(email)) {
            return new UserResponse(false, "Email is not valid");
        }

        if (!UserValidator.isValidPassword(password)) {
            return new UserResponse(false, "Password is too short");
        }

        return new UserResponse(true, "User validated");
    }

    /**
     * Verify a user
     *
     * @param user the user to verify
     */
    public void verifyUser(User user) {
        user.setVerified(true);
        repository.getUserRepository().updateUser(user);
    }

    /**
     * Get a user by email
     *
     * @param email the email of the user
     * @return the user
     */
    public User getUser(String email) {
        return repository.getUserRepository().getUser(email);
    }
}
