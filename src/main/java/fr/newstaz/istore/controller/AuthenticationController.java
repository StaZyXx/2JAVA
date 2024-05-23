package fr.newstaz.istore.controller;

import fr.newstaz.istore.model.User;
import fr.newstaz.istore.repository.Repository;
import fr.newstaz.istore.response.LoginResponse;
import fr.newstaz.istore.response.RegisterResponse;
import fr.newstaz.istore.response.UserResponse;
import org.mindrot.jbcrypt.BCrypt;

/**
 * AuthenticationController class to manage the authentication
 *
 * @version 1.0
 * @see Repository
 * @see UserController
 * @see User
 */
public class AuthenticationController {

    /**
     * Repository instance
     *
     * @see Repository
     */
    private final Repository repository;

    /**
     * UserController instance
     *
     * @see UserController
     */
    private final UserController userController;

    /**
     * The logged user
     */
    private User loggedUser;

    /**
     * AuthenticationController constructor
     *
     * @param repository     the repository
     * @param userController the user controller
     */
    public AuthenticationController(Repository repository, UserController userController) {
        this.repository = repository;
        this.userController = userController;
    }

    /**
     * Register a user
     *
     * @param email    the email of the user
     * @param password the password of the user
     * @return the register response
     */
    public RegisterResponse register(String email, String password) {
        UserResponse.CreateUserResponse response = userController.createUser(new User(email, password, User.Role.USER));
        if (!response.success()) {
            return new RegisterResponse(false, response.message());
        }
        return new RegisterResponse(true, "User created");
    }

    /**
     * Login a user
     *
     * @param email    the email of the user
     * @param password the password of the user
     * @return the login response
     */
    public LoginResponse login(String email, String password) {
        if (email == null || email.isEmpty()) {
            return new LoginResponse(false, "Email is empty");
        }

        if (password == null || password.isEmpty()) {
            return new LoginResponse(false, "Password is empty");
        }

        User user = repository.getUserRepository().getUser(email);

        if (user == null) {
            return new LoginResponse(false, "User not found");
        }

        String hashedPassword = user.getPassword();

        if (!BCrypt.checkpw(password, hashedPassword)) {
            return new LoginResponse(false, "Wrong password");
        }

        if (!user.isVerified()) {
            return new LoginResponse(false, "User is not verified");
        }

        loggedUser = user;
        return new LoginResponse(true, "User logged in");
    }

    /**
     * Get the logged user
     *
     * @return the logged user
     */
    public User getLoggedUser() {
        return loggedUser;
    }
}
