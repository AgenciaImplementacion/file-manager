package org.ut.driver;

public class DriverNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public DriverNotFoundException(String message) {
        super(message);
    }
}
