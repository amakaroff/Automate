package org.makarov.automate.exception;

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
