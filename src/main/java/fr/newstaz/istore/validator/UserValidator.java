package fr.newstaz.istore.validator;

/**
 * UserValidator class to manage the user validation
 *
 * @version 1.0
 */
public class UserValidator {

    /**
     * Check if the email is valid
     *
     * @param email the email to check
     * @return true if the email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    /**
     * Check if the password is valid
     *
     * @param password the password to check
     * @return true if the password is valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }
}
