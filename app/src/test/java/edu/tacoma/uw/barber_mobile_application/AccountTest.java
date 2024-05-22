package edu.tacoma.uw.barber_mobile_application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;


import org.junit.Test;

import edu.tacoma.uw.barber_mobile_application.ui.account.Account;

public class AccountTest {

    @Test
    public void testValidAccountCreation() {
        Account account = new Account("test@hotmail.com", "Jane", "Doe", "passw!0rD", 1234567890);
        assertEquals("test@hotmail.com", account.getEmail());
        assertEquals("Jane", account.getFirstName());
        assertEquals("Doe", account.getLastName());
        assertEquals("passw!0rD", account.getPassword());
        assertEquals(1234567890, account.getPhoneNumber());
    }

    @Test
    public void testInvalidEmail() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Account("invalidemail", "Jane", "Doe", "passw!0rD", 1234567890);
        });
        assertEquals("Invalid email. Format should be ___@___.___", exception.getMessage());
    }

    @Test
    public void testInvalidPassword() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Account("test@hotmail.com", "Jane", "Doe", "pass", 1234567890);
        });
        assertEquals("Invalid password. Must have a symbol, a character and be at least 6 characters in length", exception.getMessage());
    }

    @Test
    public void testInvalidFirstName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Account("test@hotmail.com", "J0hn", "Doe", "passw!0rD", 1234567890);
        });
        assertEquals("Invalid First Name. Must only contain characters", exception.getMessage());
    }

    @Test
    public void testInvalidLastName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Account("test@hotmail.com", "Jane", "D0e", "passw!0rD", 1234567890);
        });
        assertEquals("Invalid Last Name. Must only contain characters", exception.getMessage());
    }

    @Test
    public void testSetValidFirstName() {
        Account account = new Account("test@hotmail.com", "passw!0rD");
        account.setFirstName("Jane");
        assertEquals("Jane", account.getFirstName());
    }

    @Test
    public void testSetInvalidFirstName() {
        Account account = new Account("test@hotmail.com", "passw!0rD");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            account.setFirstName("J@ne");
        });
        assertEquals("Invalid First Name. Must only contain characters", exception.getMessage());
    }

    @Test
    public void testSetValidLastName() {
        Account account = new Account("test@hotmail.com", "passw!0rD");
        account.setLastName("Bell");
        assertEquals("Bell", account.getLastName());
    }

    @Test
    public void testSetInvalidLastName() {
        Account account = new Account("test@hotmail.com", "passw!0rD");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            account.setLastName("Be1l");
        });
        assertEquals("Invalid Last Name. Must only contain characters", exception.getMessage());
    }

    @Test
    public void testInvalidPhoneNumber() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Account("test@hotmail.com", "Jane", "Doe", "passw!0rD", 12345);
        });
        assertEquals("Invalid Phone Number. A 10 digit number in the format of ########## is required", exception.getMessage());
    }

    @Test
    public void testSetValidEmail() {
        Account account = new Account("test@hotmail.com", "passw!0rD");
        account.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", account.getEmail());
    }

    @Test
    public void testSetInvalidEmail() {
        Account account = new Account("test@hotmail.com", "passw!0rD");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            account.setEmail("invalidemail");
        });
        assertEquals("Invalid email. Format should be ___@___.___", exception.getMessage());
    }

    @Test
    public void testSetValidPassword() {
        Account account = new Account("test@hotmail.com", "passw!0rD");
        account.setPassword("NewP@ssw0rd");
        assertEquals("NewP@ssw0rd", account.getPassword());
    }

    @Test
    public void testSetInvalidPassword() {
        Account account = new Account("test@hotmail.com", "passw!0rD");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            account.setPassword("short");
        });
        assertEquals("Invalid password. Must have a symbol, a character and be at least 6 characters in length", exception.getMessage());
    }

    @Test
    public void testSetValidPhoneNumber() {
        Account account = new Account("test@hotmail.com", "passw!0rD");
        account.setPhoneNumber(1876543211);
        assertEquals(1876543211, account.getPhoneNumber());
    }

    @Test
    public void testSetInvalidPhoneNumber() {
        Account account = new Account("test@hotmail.com", "passw!0rD");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            account.setPhoneNumber(12345);
        });
        assertEquals("Invalid Phone Number. A 10 digit number in the format of ########## is required", exception.getMessage());
    }
}
