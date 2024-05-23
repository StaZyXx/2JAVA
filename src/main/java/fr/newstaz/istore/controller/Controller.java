package fr.newstaz.istore.controller;

import fr.newstaz.istore.repository.Repository;

/**
 * Controller class to manage the controllers
 *
 * @version 1.0
 * @see AuthenticationController
 * @see UserController
 * @see StoreController
 */
public class Controller {

    /**
     * AuthenticationController instance
     *
     * @see AuthenticationController
     */
    private final AuthenticationController authenticationController;

    /**
     * UserController instance
     *
     * @see UserController
     */
    private final UserController userController;

    /**
     * StoreController instance
     *
     * @see StoreController
     */
    private final StoreController storeController;

    /**
     * Controller constructor
     *
     * @param repository the repository
     */
    public Controller(Repository repository) {
        this.userController = new UserController(repository);
        this.authenticationController = new AuthenticationController(repository, userController);
        this.storeController = new StoreController(repository);
    }

    /**
     * Get the authentication controller
     *
     * @return the authentication controller
     */
    public AuthenticationController getAuthenticationController() {
        return authenticationController;
    }

    /**
     * Get the user controller
     *
     * @return the user controller
     */
    public UserController getUserController() {
        return userController;
    }

    /**
     * Get the store controller
     *
     * @return the store controller
     */
    public StoreController getStoreController() {
        return storeController;
    }
}
