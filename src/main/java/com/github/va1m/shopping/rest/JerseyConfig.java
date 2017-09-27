package com.github.va1m.shopping.rest;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

/**
 * Class for registration Jersey RESTful endpoints
 */
@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(ShoppingListEndpoint.class);
    }
}
