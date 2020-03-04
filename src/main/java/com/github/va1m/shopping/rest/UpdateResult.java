package com.github.va1m.shopping.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.va1m.shopping.entities.ListItemEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;

/**
 * Contains result of updating the shopping list - conflicted items, new on the server items,
 * or empty collection if there was no conflicts.
 */
@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateResult {

    /**
     * Contains items updated by another user or on another device
     */
    private Collection<ListItemEntity> conflictedItems;

    /**
     * Contains new items that exist in the database but didn't exist in user's list
     */
    private Collection<ListItemEntity> newRemoteItems;
}
