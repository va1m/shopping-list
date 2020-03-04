package com.github.va1m.shopping.security;

import com.github.va1m.shopping.entities.UserEntity;
import com.github.va1m.shopping.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Provides user data from the database for the Spring security.
 */
@Service
public class UserDataService implements UserDetailsService {

    private final UsersRepository dao;

    @Autowired
    public UserDataService(UsersRepository dao) {
        this.dao = dao;
    }

    @Override
    public UserDetails loadUserByUsername(String login) {
        UserEntity user = dao.findOneByLogin(login);
        if (user!=null) {
            return new User(user.getLogin(), user.getPassword(), true, true, true, true, AuthorityUtils.NO_AUTHORITIES);
        } else {
            throw new UsernameNotFoundException("could not find the user '" + login + "'");
        }
    }
}
