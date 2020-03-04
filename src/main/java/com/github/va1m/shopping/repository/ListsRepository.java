package com.github.va1m.shopping.repository;

import com.github.va1m.shopping.entities.ListEntity;
import com.github.va1m.shopping.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

import javax.validation.constraints.NotNull;

/**
 * Repository to manipulate data in the 'lists' table
 */
@Repository
public interface ListsRepository extends CrudRepository<ListEntity, Long> {

    /**
     * Finds list items by given user.
     *
     * @param owner user
     * @return A list of items whose owner is equals to the given one.
     * If no items found, this method returns empty collection.
     */
    Collection<ListEntity> findAllByOwner(@NotNull UserEntity owner);

    /**
     * Finds one item by its id and given user.
     *
     * @param id list id
     * @param owner user
     * @return An item with given id and whose owner is equals to the given one.
     * If no item found, this method returns null.
     */
    ListEntity findOneByIdAndOwner(long id, @NotNull UserEntity owner);
}
