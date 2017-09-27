package com.github.va1m.shopping.repository;

import com.github.va1m.shopping.entities.DeviceEntity;
import com.github.va1m.shopping.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

/**
 * Repository to manipulate data in the 'devices' table
 */
@Repository
public interface DevicesRepository extends CrudRepository<DeviceEntity, Long> {

    /**
     * Returns device by given id owned by given user.
     *
     * @param id   device id
     * @param user owner
     * @return a device if found, otherwise - null.
     */
    DeviceEntity findOneByIdAndUser(long id, @NotNull UserEntity user);
}