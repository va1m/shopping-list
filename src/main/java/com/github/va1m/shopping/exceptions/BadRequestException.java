package com.github.va1m.shopping.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * The exception used for showing that list data is invalid
 * When it's thrown the exception the server returns 400 (bed request) response code to a client
 */
@Slf4j
public class BadRequestException extends javax.ws.rs.BadRequestException {

    public BadRequestException() {
        super();
        log.warn("Invalid list data");
    }

    public BadRequestException(String format, Object... arguments) {
        super();
        log.warn(format, arguments);
    }
}
