package com.github.va1m.shopping.repository;

import com.github.va1m.shopping.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

/**
 * Repository to manipulate data in the 'users' table
 */
@Repository
public interface UsersRepository extends CrudRepository<UserEntity, Long> {

    /**
     * Returns a user by its login
     *
     * @param login user's login
     * @return a managed user entity if found, otherwise - null.
     */
    UserEntity findOneByLogin(@NotNull String login);

}