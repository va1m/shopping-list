package com.github.va1m.shopping.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * The exception used for showing that list data is not exists
 * When it's thrown the exception the server returns 404 (not found) response code to a client
 */
@Slf4j
public class NotFoundException extends javax.ws.rs.NotFoundException {

    public NotFoundException(long id, String login) {
        super();
        log.warn("List {} not found. User: {}", id, login);
    }
}
