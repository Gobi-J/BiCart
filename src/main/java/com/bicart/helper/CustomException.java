package com.bicart.helper;

/**
 * Custom Exception class for handling custom Database Exception
 */
public class CustomException extends RuntimeException {

    /**
     * Constructs a new CustomException with the specified detail message.
     *
     * @param message the detail message.
     */
    public CustomException(String message) {
        super(message);
    }

    /**
     * Constructs a new CustomException with the specified detail message.
     *
     * @param error the detail error.
     */
    public CustomException(Throwable error) {
        super(error);
    }

    /**
     * Constructs a new CustomException with the specified detail message.
     *
     * @param message the detail message
     * @param error   the detail error.
     */
    public CustomException(String message, Throwable error) {
        super(message, error);
    }
}
