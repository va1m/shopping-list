package com.github.va1m.shopping.repository;

import com.github.va1m.shopping.entities.ListEntity;
import com.github.va1m.shopping.entities.ListItemEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

import javax.validation.constraints.NotNull;

/**
 * Repository to manipulate data in the 'list_items' table
 */
@Repository
public interface ListItemsRepository extends CrudRepository<ListItemEntity, Long> {

    /**
     * Returns device by given id owned by given user.
     *
     * @param list to which items belong
     * @return list of items in the list or empty collection if the list doesn't contain items.
     */
    Collection<ListItemEntity> findAllByList(@NotNull ListEntity list);
}
