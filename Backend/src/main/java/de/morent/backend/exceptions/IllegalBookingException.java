package de.morent.backend.exceptions;

public class IllegalBookingException extends Throwable {
    public IllegalBookingException(String message) {
        super(message);
    }
}
