package com.bicart.helper;

import javax.security.sasl.AuthenticationException;

/**
 * Custom Exception class for handling custom Jwt Authentication Exception.
 */
public class JwtAuthenticationException extends AuthenticationException {

    /**
     * <p>
     * Constructs a new JwtAuthenticationException with the specified detail message.
     * </p>
     *
     * @param message the detail message to be included with the exception
     */
    public JwtAuthenticationException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs a new JwtAuthenticationException with the specified detail message and cause.
     * </p>
     *
     * @param message the detail message to be included with the exception
     * @param cause   the cause of the exception
     */
    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}