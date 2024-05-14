package edu.tacoma.uw.barber_mobile_application;

import java.util.regex.Pattern;

public class Account {

    private String mEmail;
    private String mFirstName;
    private String mLastName;
    private String mPassword;
    private int mPhoneNumber;


    public final static String EMAIL = "Email";
    public final static String FIRST_NAME = "FirstName";
    public final static String LAST_NAME = "LastName";
    public final static String PHONE_NUMBER = "PhoneNumber";

    /**
     * Email validation pattern.
     */
    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    private final static int PASSWORD_LEN = 6;

    public Account(String theEmail, String theFirstName,
            String theLastName, String thePassword, int thePhoneNumber) {
        setEmail(theEmail);
        setFirstName(theFirstName);
        setLastName(theLastName);
        setPassword(thePassword);
        setPhoneNumber(thePhoneNumber);
    }

    public Account (String theFirstName, String theLastName, String theEmail, int thePhoneNumber) {
        setFirstName(theFirstName);
        setLastName(theLastName);
        setEmail(theEmail);
        setPhoneNumber(thePhoneNumber);
    }

    public Account(String theEmail, String thePassword) {
        setEmail(theEmail);
        setPassword(thePassword);
    }

    public void setEmail(String thePassedEmail) {
        if (!isValidEmail(thePassedEmail)) {
            throw new IllegalArgumentException("Invalid email. Format should be ___@___.___");
        }
        this.mEmail = thePassedEmail;
    }
    public void setFirstName(String theFirstName) {
        if (!isValidFirstName(theFirstName)) {
            throw new IllegalArgumentException("Invalid First Name. Must only contain characters");
        }
        this.mFirstName = theFirstName;
    }
    public void setLastName(String theLastName) {
        if (!isValidLastName(theLastName)) {
            throw new IllegalArgumentException("Invalid Last Name. Must only contain characters");
        }
        this.mLastName = theLastName;
    }
    public void setPassword(String thePassedPassword) {
        if (!isValidPassword(thePassedPassword)) {
            throw new IllegalArgumentException("Invalid password. Must have a symbol, a character and be at least 6 characters in length");
        }
        this.mPassword = thePassedPassword;
    }
    public void setPhoneNumber(int thePhoneNumber) {
        if (!isValidPhoneNumber(thePhoneNumber)) {
            throw new IllegalArgumentException("Invalid Phone Number. A 10 digit number in the format of ########## is required");
        }
        this.mPhoneNumber = thePhoneNumber;
    }

    public String getEmail() {
        return mEmail;
    }
    public String getFirstName() {
        return mFirstName;
    }
    public String getLastName() {
        return mLastName;
    }
    public String getPassword() {
        return mPassword;
    }
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
     * Valid password must be at last 6 characters long
     * with at least one digit and one symbol.
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

    public static boolean isValidFirstName(String firstName) {
        return firstName.matches("[a-zA-Z]+");
    }

    public static boolean isValidLastName(String lastName) {
        return lastName.matches("[a-zA-Z]+");
    }

    public static boolean isValidPhoneNumber(int phoneNumber) {
        return String.valueOf(phoneNumber).matches("\\d{10}");
    }

}
