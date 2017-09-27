package com.github.va1m.shopping.rest;

import com.github.va1m.shopping.entities.ListItemEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.ToString;

import java.util.Collection;

/**
 * Contains result of updating the shopping list - conflicted items, new on the server items,
 * or empty collection if there was no conflicts.
 */
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateResult {

    /**
     * Contains items updated by another user or on another device
     */
    public Collection<ListItemEntity> conflictedItems;

    /**
     * Contains new items that exist in the database but didn't exist in user's list
     */
    public Collection<ListItemEntity> newRemoteItems;
}
