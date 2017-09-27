package com.github.va1m.shopping;

import com.github.va1m.shopping.entities.DeviceEntity;
import com.github.va1m.shopping.entities.ListEntity;
import com.github.va1m.shopping.entities.ListItemEntity;
import com.github.va1m.shopping.entities.UserEntity;

import java.util.Arrays;

/**
 * Contains master data to compare with
 */
class MasterData {

    /**
     * Endpoint URI
     */
    static final String SERVICE_URI = "/lists/";

    /**
     * User logins from the test database
     */
    static final String MARK_LOGIN = "mark.johnson@yahoo.com";
    static final String MIRANDA_LOGIN = "miranda.johnson@yahoo.com";

    /** Test users password */
    static final String PASSWORD = "password";

    /** Test users: Mark and Miranda */
    static final UserEntity MARK = new UserEntity(1L, MARK_LOGIN, "Mark Johnson", null, null, null, null);
    static final UserEntity MIRANDA = new UserEntity(2L, MIRANDA_LOGIN, "Miranda Johnson", null, null, null, null);

    /** Users' devices */
    private static final DeviceEntity SAMSUNG = new DeviceEntity(1L, "Samsung Galaxy S8", null, null);
    static final DeviceEntity SAMSUNG_TAB = new DeviceEntity(2L, "Samsung Tab A", null, null);
    static final DeviceEntity IPHONE = new DeviceEntity(3L, "Apple iPhone S7", null, null);
    static final DeviceEntity MACBOOK = new DeviceEntity(4L, "MacBook Air", null, null);

    /** Reference shopping list with CPUs owned by Mark */
    static final ListEntity referenceListCPUs = new ListEntity(1L,
            "CPUs",
            "Processors for servers #1, #6, #11",
            MARK,
            Arrays.asList(
                    new ListItemEntity(1L, 0, "Core i5", "CPU Intel Core i5", true, 2, false, null, MARK, SAMSUNG),
                    new ListItemEntity(2L, 0, "Core i7", "CPU Intel Core i7", true, 1, false, null, MARK, SAMSUNG)
            ),
            null,
            null
    );

    /** Reference list with HDDs owned by Mark */
    static final ListEntity referenceListHDDs = new ListEntity(2L,
            "HDDs",
            "Hard drives for servers #1, #6, #11, #12",
            MARK,
            Arrays.asList(
                    new ListItemEntity(3L, 0, "WD 1TB", "Western Digital WD Blue Desktop 1 TB (WD10EZRZ)", true, 1,
                            false, null, MARK, SAMSUNG),
                    new ListItemEntity(4L, 0, "Seagate 1TB", "Seagate STEA1000400", false, 1, false, null, MARK,
                            SAMSUNG),
                    new ListItemEntity(5L, 0, "SSD Kingston 240G", "Kingston SUV400S37/240G", true, 1, false, null,
                            MARK, SAMSUNG),
                    new ListItemEntity(6L, 0, "WD 500GB", "Western Digital WD Black 500 GB (WD5000LPLX)", true, 2,
                            false, null, MARK, SAMSUNG)
            ),
            null,
            null
    );

    /** Reference list with RAMs owned by Mark */
    static final ListEntity referenceListRAMs = new ListEntity(5L,
            "RAMs",
            "Memory for servers",
            MARK,
            Arrays.asList(new ListItemEntity[0]),
            null,
            null
    );

    /** Reference list with flowers owned by Miranda */
    static final ListEntity referenceListFlowers = new ListEntity(3L,
            "Flowers",
            "Flowers for Marta's birthday",
            MIRANDA,
            Arrays.asList(
                    new ListItemEntity(7L, 0, "Rose", "Red roses", true, 11, false, null, MIRANDA, IPHONE),
                    new ListItemEntity(8L, 0, "Chrysanthemum", "White", true, 7, false, null, MIRANDA, IPHONE),
                    new ListItemEntity(9L, 0, "Tulip", "White", true, 7, true, null, MIRANDA, IPHONE),
                    new ListItemEntity(10L, 0, "Daisy Flowers", "Green", true, 3, true, null, MIRANDA, IPHONE)
            ),
            null,
            null
    );

    /** Reference list with gifts owned by Miranda */
    static final ListEntity referenceListGifts = new ListEntity(4L,
            "Gifts",
            "Gifts for Marta's birthday",
            MIRANDA,
            Arrays.asList(
                    new ListItemEntity(11L, 1, "Candy", "Kommunarka", true, 1, false, null, MIRANDA, IPHONE),
                    new ListItemEntity(12L, 0, "Champagne", "Moët & Chandon 2008", true, 3, false, null, MARK, SAMSUNG_TAB)
            ),
            null,
            null
    );

    /** Reference empty incomplete 'new' shopping list owned by Miranda */
    static final ListEntity referenceListNew = new ListEntity(6L,
            "New List",
            "",
            MIRANDA,
            Arrays.asList(new ListItemEntity[0]),
            null,
            null);

    /** Template list which is cloning in some test to fill custom data */
    final static ListEntity newListTemplate = new ListEntity(null,
            "Books",
            "Short list for reading",
            null,
            Arrays.asList(
                    new ListItemEntity(null, 0, "O. Henry", "The Voice of the City", true, 1, false, null, null, null),
                    new ListItemEntity(null, 0, "J. K. Rowling", "H.P. The Order of the Phoenix", false, 1, false, null, null, null),
                    new ListItemEntity(null, 0, "Poul Anderson", "A Plague of Masters", true, 1, false, null, null, null),
                    new ListItemEntity(null, 0, "Isaac Asimov", "Legal Rites", true, 1, false, null, null, null),
                    new ListItemEntity(null, 0, "Jack London", "The Star Rover", true, 1, false, null, null, null)
            ),
            IPHONE,
            null);
}
