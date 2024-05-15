package edu.tacoma.uw.barber_mobile_application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;


import org.junit.Test;

import edu.tacoma.uw.barber_mobile_application.ui.account.Account;

public class AccountTest {

    @Test
    public void testValidAccountCreation() {
        Account account = new Account("test@example.com", "John", "Doe", "Passw0rd!", 1234567890);
        assertEquals("test@example.com", account.getEmail());
        assertEquals("John", account.getFirstName());
        assertEquals("Doe", account.getLastName());
        assertEquals("Passw0rd!", account.getPassword());
        assertEquals(1234567890, account.getPhoneNumber());
    }

    @Test
    public void testInvalidEmail() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Account("invalidemail", "John", "Doe", "Passw0rd!", 1234567890);
        });
        assertEquals("Invalid email. Format should be ___@___.___", exception.getMessage());
    }

    @Test
    public void testInvalidPassword() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Account("test@example.com", "John", "Doe", "pass", 1234567890);
        });
        assertEquals("Invalid password. Must have a symbol, a character and be at least 6 characters in length", exception.getMessage());
    }

    @Test
    public void testInvalidFirstName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Account("test@example.com", "J0hn", "Doe", "Passw0rd!", 1234567890);
        });
        assertEquals("Invalid First Name. Must only contain characters", exception.getMessage());
    }

    @Test
    public void testInvalidLastName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Account("test@example.com", "John", "D0e", "Passw0rd!", 1234567890);
        });
        assertEquals("Invalid Last Name. Must only contain characters", exception.getMessage());
    }

    @Test
    public void testInvalidPhoneNumber() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Account("test@example.com", "John", "Doe", "Passw0rd!", 12345);
        });
        assertEquals("Invalid Phone Number. A 10 digit number in the format of ########## is required", exception.getMessage());
    }

    @Test
    public void testSetValidEmail() {
        Account account = new Account("test@example.com", "Passw0rd!");
        account.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", account.getEmail());
    }

    @Test
    public void testSetInvalidEmail() {
        Account account = new Account("test@example.com", "Passw0rd!");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            account.setEmail("invalidemail");
        });
        assertEquals("Invalid email. Format should be ___@___.___", exception.getMessage());
    }

    @Test
    public void testSetValidPassword() {
        Account account = new Account("test@example.com", "Passw0rd!");
        account.setPassword("NewP@ssw0rd");
        assertEquals("NewP@ssw0rd", account.getPassword());
    }

    @Test
    public void testSetInvalidPassword() {
        Account account = new Account("test@example.com", "Passw0rd!");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            account.setPassword("short");
        });
        assertEquals("Invalid password. Must have a symbol, a character and be at least 6 characters in length", exception.getMessage());
    }

    @Test
    public void testSetValidPhoneNumber() {
        Account account = new Account("test@example.com", "Passw0rd!");
        account.setPhoneNumber(9876543211);
        assertEquals(9876543211, account.getPhoneNumber());
    }

    @Test
    public void testSetInvalidPhoneNumber() {
        Account account = new Account("test@example.com", "Passw0rd!");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            account.setPhoneNumber(12345);
        });
        assertEquals("Invalid Phone Number. A 10 digit number in the format of ########## is required", exception.getMessage());
    }

    @Test
    public void testSetValidFirstName() {
        Account account = new Account("test@example.com", "Passw0rd!");
        account.setFirstName("Jane");
        assertEquals("Jane", account.getFirstName());
    }

    @Test
    public void testSetInvalidFirstName() {
        Account account = new Account("test@example.com", "Passw0rd!");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            account.setFirstName("J@ne");
        });
        assertEquals("Invalid First Name. Must only contain characters", exception.getMessage());
    }

    @Test
    public void testSetValidLastName() {
        Account account = new Account("test@example.com", "Passw0rd!");
        account.setLastName("Smith");
        assertEquals("Smith", account.getLastName());
    }

    @Test
    public void testSetInvalidLastName() {
        Account account = new Account("test@example.com", "Passw0rd!");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            account.setLastName("Sm1th");
        });
        assertEquals("Invalid Last Name. Must only contain characters", exception.getMessage());
    }
}
