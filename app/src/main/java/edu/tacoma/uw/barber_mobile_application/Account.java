package edu.tacoma.uw.barber_mobile_application;

import java.util.regex.Pattern;

/**
 * Represents an account entity with associated properties such as email, first name, last name,
 * password, and phone number.
 *
 * @author Hassan Bassam Farhat
 * @version Spring 2024
 */
public class Account {

    // Constant definitions for Account Object Entity
    public final static String EMAIL = "Email";
    public final static String FIRST_NAME = "FirstName";
    public final static String LAST_NAME = "LastName";
    public final static String PHONE_NUMBER = "PhoneNumber";
    private final static int PASSWORD_LEN = 6;
    /** Email validation pattern. */
    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    // Class Variable(s) attributes
    private String mEmail;
    private String mFirstName;
    private String mLastName;
    private String mPassword;
    private int mPhoneNumber;


    // Constructors

    /**
     * Constructs an Account object with provided email, first name, last name, password,
     * and phone number.
     *
     * @param theEmail The email address of the account.
     * @param theFirstName The first name of the account holder.
     * @param theLastName The last name of the account holder.
     * @param thePassword The password of the account.
     * @param thePhoneNumber The phone number of the account holder.
     */
    public Account(String theEmail, String theFirstName,
            String theLastName, String thePassword, int thePhoneNumber) {
        setEmail(theEmail);
        setFirstName(theFirstName);
        setLastName(theLastName);
        setPassword(thePassword);
        setPhoneNumber(thePhoneNumber);
    }

    /**
     * Constructs an Account object with provided first name, last name, email, and phone number.
     *
     * @param theFirstName The first name of the account holder.
     * @param theLastName The last name of the account holder.
     * @param theEmail The email address of the account.
     * @param thePhoneNumber The phone number of the account holder.
     */
    public Account (String theFirstName, String theLastName,
                    String theEmail, int thePhoneNumber) {
        setFirstName(theFirstName);
        setLastName(theLastName);
        setEmail(theEmail);
        setPhoneNumber(thePhoneNumber);
    }

    /**
     * Constructs an Account object with provided email and password.
     *
     * @param theEmail The email address of the account.
     * @param thePassword The password of the account.
     */
    public Account(String theEmail, String thePassword) {
        setEmail(theEmail);
        setPassword(thePassword);
    }


    // Public Class Methods

    /**
     * Sets the email address of the account.
     *
     * @param thePassedEmail The email address to be set.
     * @throws IllegalArgumentException If the provided email address is invalid.
     */
    public void setEmail(String thePassedEmail) {
        if (!isValidEmail(thePassedEmail)) {
            throw new IllegalArgumentException("Invalid email. Format should be ___@___.___");
        }
        this.mEmail = thePassedEmail;
    }

    /**
     * Sets the first name of the account holder.
     *
     * @param theFirstName The first name to be set.
     * @throws IllegalArgumentException If the provided first name is invalid.
     */
    public void setFirstName(String theFirstName) {
        if (!isValidFirstName(theFirstName)) {
            throw new IllegalArgumentException("Invalid First Name. Must only contain characters");
        }
        this.mFirstName = theFirstName;
    }

    /**
     * Sets the last name of the account holder.
     *
     * @param theLastName The last name to be set.
     * @throws IllegalArgumentException If the provided last name is invalid.
     */
    public void setLastName(String theLastName) {
        if (!isValidLastName(theLastName)) {
            throw new IllegalArgumentException("Invalid Last Name. Must only contain characters");
        }
        this.mLastName = theLastName;
    }

    /**
     * Sets the password of the account.
     *
     * @param thePassedPassword The password to be set.
     * @throws IllegalArgumentException If the provided password is invalid.
     */
    public void setPassword(String thePassedPassword) {
        if (!isValidPassword(thePassedPassword)) {
            throw new IllegalArgumentException("Invalid password. Must have a symbol, a " +
                                               "character and be at least 6 characters in length");
        }
        this.mPassword = thePassedPassword;
    }

    /**
     * Sets the phone number of the account holder.
     *
     * @param thePhoneNumber The phone number to be set.
     * @throws IllegalArgumentException If the provided phone number is invalid.
     */
    public void setPhoneNumber(int thePhoneNumber) {
        if (!isValidPhoneNumber(thePhoneNumber)) {
            throw new IllegalArgumentException("Invalid Phone Number. A 10 digit number in " +
                                               "the format of ########## is required");
        }
        this.mPhoneNumber = thePhoneNumber;
    }


    /**
     * Retrieves the email address of the account.
     *
     * @return The email address of the account.
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     * Retrieves the first name of the account holder.
     *
     * @return The first name of the account holder.
     */
    public String getFirstName() {
        return mFirstName;
    }

    /**
     * Retrieves the last name of the account holder.
     *
     * @return The last name of the account holder.
     */
    public String getLastName() {
        return mLastName;
    }

    /**
     * Retrieves the password of the account.
     *
     * @return The password of the account.
     */
    public String getPassword() {
        return mPassword;
    }

    /**
     * Retrieves the phone number of the account holder.
     *
     * @return The phone number of the account holder.
     */
    public int getPhoneNumber() {
        return mPhoneNumber;
    }

    /**
     * Validates if the given input is a valid email address.
     *
     * @param email        The email to validate.
     * @return {@code true} if the input is a valid email. {@code false} otherwise.
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates if the given password is valid.
     * Valid password must be at last 6 characters long with at least one digit and one symbol.
     *
     * @param password        The password to validate.
     * @return {@code true} if the input is a valid password.
     * {@code false} otherwise.
     */
    public static boolean isValidPassword(String password) {
        boolean foundDigit = false, foundSymbol = false;
        if (password == null ||
                password.length() < PASSWORD_LEN)
            return false;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i)))
                foundDigit = true;
            if (!Character.isLetterOrDigit(password.charAt(i)))
                foundSymbol = true;
        }
        return foundDigit && foundSymbol;
    }

    /**
     * Validates if the given first name is valid.
     *
     * @param firstName The first name to validate.
     * @return {@code true} if the input is a valid first name.
     * {@code false} otherwise.
     */
    public static boolean isValidFirstName(String firstName) {
        return firstName.matches("[a-zA-Z]+");
    }

    /**
     * Validates if the given last name is valid.
     *
     * @param lastName The last name to validate.
     * @return {@code true} if the input is a valid last name.
     * {@code false} otherwise.
     */
    public static boolean isValidLastName(String lastName) {
        return lastName.matches("[a-zA-Z]+");
    }

    /**
     * Validates if the given phone number is valid.
     *
     * @param phoneNumber The phone number to validate.
     * @return {@code true} if the input is a valid phone number.
     * {@code false} otherwise.
     */
    public static boolean isValidPhoneNumber(int phoneNumber) {
        return String.valueOf(phoneNumber).matches("\\d{10}");
    }

}
