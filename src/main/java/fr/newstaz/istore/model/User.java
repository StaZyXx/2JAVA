package fr.newstaz.istore.model;

import java.util.Objects;

/**
 * User class to manage the user
 *
 * @version 1.0
 */
public class User {

    /**
     * The id of the user
     */
    private int id;

    /**
     * The email of the user
     *
     * @see #getEmail()
     * @see #setEmail(String)
     */
    private String email;

    /**
     * The password of the user
     *
     * @see #getPassword()
     * @see #setPassword(String)
     */
    private String password;

    /**
     * The role of the user
     *
     * @see #getRole()
     * @see #setRole(Role)
     */
    private Role role;

    /**
     * The verification status of the user
     *
     * @see #isVerified()
     * @see #setVerified(boolean)
     */
    private boolean isVerified = false;

    /**
     * Default constructor
     */
    public User() {
    }

    /**
     * Constructor with email and password
     *
     * @param email    the email of the user
     * @param password the password of the user
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.role = Role.USER;
    }

    /**
     * Constructor with id, email, password, role and verification status
     *
     * @param id        the id of the user
     * @param email     the email of the user
     * @param password  the password of the user
     * @param role      the role of the user
     * @param isVerified the verification status of the user
     */
    public User(int id, String email, String password, Role role, boolean isVerified) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isVerified = isVerified;
    }

    /**
     * Constructor with email, password and role
     *
     * @param email    the email of the user
     * @param hashpw   the password of the user hashed
     * @param role     the role of the user
     */
    public User(String email, String hashpw, Role role) {
        this.email = email;
        this.password = hashpw;
        this.role = role;
    }

    /**
     * Constructor with user to copy
     *
     * @param user the user
     */
    public User(User user) {
        this.id = user.id;
        this.email = user.email;
        this.password = user.password;
        this.role = user.role;
        this.isVerified = user.isVerified;
    }

    /**
     * Get the id of the user
     *
     * @return the id of the user
     */
    public int getId() {
        return id;
    }

    /**
     * Get the email of the user
     *
     * @return the email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email of the user
     *
     * @param email the email of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the password of the user
     *
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password of the user
     *
     * @param password the password of the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the role of the user
     *
     * @return the role of the user
     */
    public Role getRole() {
        return role;
    }

    /**
     * Set the role of the user
     *
     * @param role the role of the user
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Get the verification status of the user
     *
     * @return the verification status of the user
     */
    public boolean isVerified() {
        return isVerified;
    }

    /**
     * Set the verification status of the user
     *
     * @param verified the verification status of the user
     */
    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return id == user.id && Objects.equals(email, user.email) && Objects.equals(password, user.password) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, role);
    }

    /**
     * Role enum class
     *
     * @version 1.0
     */
    public enum Role {
        ADMIN, USER
    }
}
