package org.makarov.automate;

public class AutomateException extends RuntimeException {

    public AutomateException() {
    }

    public AutomateException(String message) {
        super(message);
    }

    public AutomateException(String message, Throwable cause) {
        super(message, cause);
    }
}
