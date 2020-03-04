package com.github.va1m.shopping.rest;

import com.github.va1m.shopping.entities.ListEntity;
import com.github.va1m.shopping.exceptions.BadRequestException;
import com.github.va1m.shopping.exceptions.NotFoundException;
import com.github.va1m.shopping.services.ShoppingListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.net.URI;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * RESTful interface to work with the shopping list and its items.
 * Matched to the '/lists' URI with the methods GET, POST, and DELETE.
 * Accepts and produces content with type 'application/json;charset=UTF-8'
 */
@Component
@Path("/lists")
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Transactional
public class ShoppingListEndpoint {

    private final ShoppingListService service;

    /**
     * Constructor with injection
     */
    @Autowired
    public ShoppingListEndpoint(ShoppingListService service) {
        this.service = service;
    }

    /**
     * Returns all shopping lists belonged to the logged in user
     *
     * @return all shopping lists if exist, otherwise - empty list
     */
    @GET
    @Transactional(readOnly = true)
    public Collection<ListEntity> getAll() {
        log.trace("Call getAll()");

        Collection<ListEntity> result = service.getAll();

        log.trace("getAll() returns {} lists", result.size());
        return result;
    }

    /**
     * Returns a shopping list belonged to the logged in user by given id
     *
     * @param id list id
     * @return shopping list if exist, otherwise - null
     * @throws NotFoundException if the list with given id is not found or not belongs to the logged in user.
     */
    @GET
    @Path("{id}")
    @Transactional(readOnly = true)
    public ListEntity get(@PathParam("id") long id) {
        log.trace("Call get({})", id);

        ListEntity result = service.get(id);

        log.trace("get() returns {}", result);
        return result;
    }

    /**
     * Stores new list into database
     *
     * @param list new list with items
     * @param uriInfo automatically injected {@link UriInfo}
     * @return response code 201 and URL to the appended list if success, otherwise - response code 400.
     * @throws BadRequestException if the list parameter has invalid structure
     */
    @POST
    @Transactional
    public Response add(@Validated ListEntity list, @Context UriInfo uriInfo) {
        log.trace("Call add({})", list);

        ListEntity savedList = service.add(list);

        URI uri = uriInfo.getAbsolutePathBuilder().path(savedList.getId().toString()).build();
        log.trace("get() done");
        return Response.created(uri).build();
    }

    /**
     * Updates existing in the database list. Overwrites items by new ones if they have "force" flag.
     *
     * @param id list id
     * @param list list's data with items have to be stored.
     * @return UpdateResult structure with conflicted items and response code 200 if no errors,
     * otherwise - response code 400.
     * @throws BadRequestException if the list parameter has invalid structure
     * @throws NotFoundException   if the list with given id is not found or not belongs to the logged in user.
     */
    @POST
    @Path("{id}")
    @Transactional
    public UpdateResult update(@PathParam("id") long id, @Validated ListEntity list) {
        log.trace("Call update({}, {})", id, list);

        list.setId(id);
        UpdateResult result = service.update(list);

        log.trace("update() returns {}", result);
        return result;
    }

    /**
     * Deletes a shopping list with given id from the database
     *
     * @param id shopping list id
     * @return response code 200 if success, otherwise - 400 if {@link NotFoundException} was thrown.
     * @throws NotFoundException if the list with given id is not found or not belongs to the logged in user.
     */
    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") long id) {
        log.trace("Call delete({})", id);

        service.delete(id);

        log.trace("delete() done");
        return Response.ok().build();
    }
}
