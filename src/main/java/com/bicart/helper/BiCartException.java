package com.bicart.helper;

/**
 * Custom Exception class for handling custom Database Exception
 */
public class BiCartException extends RuntimeException {

    /**
     * Constructs a new CustomException with the specified detail message.
     *
     * @param message the detail message.
     */
    public BiCartException(String message) {
        super(message);
    }

    /**
     * Constructs a new CustomException with the specified detail message.
     *
     * @param error the detail error.
     */
    public BiCartException(Throwable error) {
        super(error);
    }

    /**
     * Constructs a new CustomException with the specified detail message.
     *
     * @param message the detail message
     * @param error   the detail error.
     */
    public BiCartException(String message, Throwable error) {
        super(message, error);
    }
}
