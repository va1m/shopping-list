package com.github.va1m.shopping.services;

import com.github.va1m.shopping.entities.DeviceEntity;
import com.github.va1m.shopping.entities.ListEntity;
import com.github.va1m.shopping.entities.ListItemEntity;
import com.github.va1m.shopping.entities.UserEntity;
import com.github.va1m.shopping.exceptions.BadRequestException;
import com.github.va1m.shopping.exceptions.NotFoundException;
import com.github.va1m.shopping.repository.DevicesRepository;
import com.github.va1m.shopping.repository.ListItemsRepository;
import com.github.va1m.shopping.repository.ListsRepository;
import com.github.va1m.shopping.repository.UsersRepository;
import com.github.va1m.shopping.rest.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains business logic for working with shopping lists and items
 */
@Service
@Slf4j
public class ShoppingListService {

    private final ListsRepository listsRepository;

    private final ListItemsRepository listItemsRepository;

    private final UsersRepository usersRepository;

    private final DevicesRepository devicesRepository;

    /**
     * Constructor with injection
     */
    @Autowired
    public ShoppingListService(ListsRepository listsRepository, ListItemsRepository listItemsRepository,
                               UsersRepository usersRepository, DevicesRepository devicesRepository) {
        this.listsRepository = listsRepository;
        this.listItemsRepository = listItemsRepository;
        this.usersRepository = usersRepository;
        this.devicesRepository = devicesRepository;
    }

    /**
     * Store new list in the database
     *
     * @param list list with items to store
     * @return managed list entity with new list id
     */
    public ListEntity add(ListEntity list) {
        log.trace("Call add('{}')", list);

        if (list == null) {
            throw new BadRequestException();
        }

        UserEntity currentUser = getCurrentUser();

        DeviceEntity device = getManagedDevice(list.getDevice(), currentUser);

        list.setId(null);
        list.setOwner(currentUser);
        ListEntity managedList = listsRepository.save(list);

        list.getListItems().forEach(item -> {
            item.setId(null);
            item.setAuthor(currentUser);
            item.setDevice(device);
            item.setIsDeleted(false);
            item.setList(managedList);
            ListItemEntity managedItem = listItemsRepository.save(item);
            log.trace("'{}' added", managedItem);
        });

        log.trace("add() returns '{}'", managedList);
        return managedList;
    }

    /**
     * Returns all lists by logged in user
     * @return list of shopping lists with their items
     */
    public Collection<ListEntity> getAll() {
        log.trace("Call getAll()");
        UserEntity currentUser = getCurrentUser();
        Collection<ListEntity> lists = listsRepository.findAllByOwner(currentUser);
        log.trace("getAll() return '{}'", lists);
        return lists;
    }

    /**
     * Update existing list and items
     *
     * @param list modified user's list
     * @return update result - conflicted and new (on the backend) items updated early by
     * another collaborator or the same user but from another device
     * @throws BadRequestException if input list is not valid
     * @throws NotFoundException if original list not found by given id
     */
    public UpdateResult update(ListEntity list) {
        log.trace("Call update('{}')", list);

        if (list == null || list.getId() == null) {
            throw new BadRequestException();
        }
        UserEntity currentUser = getCurrentUser();

        ListEntity existingList = listsRepository.findOneByIdAndOwner(list.getId(), currentUser);
        if (existingList == null) {
            throw new NotFoundException(list.getId(), currentUser.getLogin());
        }

        list.setOwner(currentUser);
        ListEntity managedList = listsRepository.save(list);

        DeviceEntity managedDevice = getManagedDevice(list.getDevice(), currentUser);

        Collection<ListItemEntity> managedItems = listItemsRepository.findAllByList(managedList);
        List<ListItemEntity> updatedItems = new ArrayList<>(list.getListItems().size());
        List<ListItemEntity> conflictedItems = new ArrayList<>();

        list.getListItems().forEach(item -> {

            ListItemEntity savedItem;

            if (item.getId() == null) {
                // Add new item
                savedItem = saveItem(item, managedList, currentUser, managedDevice);
                updatedItems.add(savedItem);
                log.trace("new '{}' saved", savedItem);
            } else {
                // Update existing item

                ListItemEntity managedItem = managedItems.stream()
                        .filter(i -> item.getId().equals(i.getId()))
                        .findAny()
                        .orElseThrow(BadRequestException::new);

                if (managedItem.getVersion() != item.getVersion()) {
                    // conflict
                    if (list.getForce() != null && list.getForce()) {
                        // Resolve conflict
                        item.setVersion(managedItem.getVersion());
                        savedItem = saveItem(item, managedList, currentUser, managedDevice);
                        updatedItems.add(savedItem);
                        log.trace("'{}' conflicted with '{}' saved forcibly", item, savedItem);
                    } else {
                        conflictedItems.add(managedItem);
                        log.trace("'{}' conflicts with '{}'", item, managedItem);
                    }
                } else {
                    //no conflict
                    savedItem = saveItem(item, managedList, currentUser, managedDevice);
                    updatedItems.add(savedItem);
                    log.trace("'{}' saved", savedItem);
                }
            }
        });

        UpdateResult result = new UpdateResult();
        result.conflictedItems = conflictedItems;

        // Check if DB has more items than in input list
        result.newRemoteItems = managedItems.stream()
                .filter(mi -> updatedItems.stream().noneMatch(ui -> mi.getId().equals(ui.getId())))
                .filter(mi -> conflictedItems.stream().noneMatch(ci -> mi.getId().equals(ci.getId())))
                .collect(Collectors.toList());

        log.trace("update() returns '{}'", result);
        return result;
    }

    /**
     * Saves an item in the database
     *
     * @param item   new or existing item to be saved
     * @param list   the item belongs to
     * @param user   an editor of the item
     * @param device where the item was edited
     * @return saved and managed item with its id
     */
    private ListItemEntity saveItem(ListItemEntity item, ListEntity list, UserEntity user,
                                    DeviceEntity device) {
        log.trace("Call saveItem('{}', '{}', '{}', '{}')", item, list, user, device);
        item.setList(list);
        item.setAuthor(user);
        if (item.getIsDeleted() == null) {
            item.setIsDeleted(false);
        }
        item.setDevice(getManagedDevice(device, user));
        ListItemEntity savedItem = listItemsRepository.save(item);
        log.trace("saveItem() returns '{}'", savedItem);
        return savedItem;
    }

    /**
     * If the given device is new - stores it in the database, otherwise - returns managed device entity.
     * @param device new or deattached device entity
     * @param currentUser logged in user
     * @return managed device entity.
     */
    private DeviceEntity getManagedDevice(DeviceEntity device, UserEntity currentUser) {
        log.trace("Call getManagedDevice('{}', '{}')", device, currentUser);
        DeviceEntity managedDevice;
        if (device.getId() == null) {
            device.setUser(currentUser);
            managedDevice = devicesRepository.save(device);
        } else {
            managedDevice = devicesRepository.findOneByIdAndUser(device.getId(), currentUser);
            if (managedDevice == null) {
                throw new BadRequestException("Device '{}' owned by current user '{}' not found", device, currentUser);
            }
            return managedDevice;
        }
        log.trace("getManagedDevice() returns '{}'", managedDevice);
        return managedDevice;
    }

    /**
     * Deletes shopping list from the database
     * @param id shopping list id
     * @throws NotFoundException if list with given id not found or belongs another user.
     */
    public void delete(long id) {
        log.trace("Call delete('{}')", id);

        UserEntity user = getCurrentUser();
        ListEntity list = listsRepository.findOneByIdAndOwner(id, user);
        if (list != null) {
            listsRepository.delete(list);
        } else {
            throw new NotFoundException(id, user.getLogin());
        }

        log.trace("delete() exits");
    }

    /**
     * Returns existing shopping list by its id
     * @param id list id
     * @return the list
     * @throws NotFoundException if list with given id not found
     */
    public ListEntity get(long id) {
        log.trace("Call get('{}')", id);

        UserEntity user = getCurrentUser();
        ListEntity list = listsRepository.findOneByIdAndOwner(id, user);
        if (list == null) {
            throw new NotFoundException(id, user.getLogin());
        }
        log.trace("get() returns '{}'", list);
        return list;
    }

    /**
     * Returns the logged in user who performs current request to the server.
     * @return managed user entity
     */
    private UserEntity getCurrentUser() {
        log.trace("Call getCurrentUser()");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        UserEntity user = usersRepository.findOneByLogin(login);
        log.trace("getCurrentUser() returns '{}'", user);
        return user;
    }

}
