package com.github.va1m.shopping.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;

/**
 * Spring security config class
 */
@Configuration
class AuthenticationConfig extends GlobalAuthenticationConfigurerAdapter {

    private final UserDataService service;

    @Autowired
    public AuthenticationConfig(UserDataService service) {
        this.service = service;
    }

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service);
    }
}
