package com.github.va1m.shopping.integration;

import static com.github.va1m.shopping.integration.MasterData.IPHONE;
import static com.github.va1m.shopping.integration.MasterData.MACBOOK;
import static com.github.va1m.shopping.integration.MasterData.MARK;
import static com.github.va1m.shopping.integration.MasterData.MARK_LOGIN;
import static com.github.va1m.shopping.integration.MasterData.MIRANDA_LOGIN;
import static com.github.va1m.shopping.integration.MasterData.PASSWORD;
import static com.github.va1m.shopping.integration.MasterData.SERVICE_URI;
import static com.github.va1m.shopping.integration.MasterData.referenceListCPUs;
import static com.github.va1m.shopping.integration.MasterData.referenceListGifts;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.github.va1m.shopping.entities.DeviceEntity;
import com.github.va1m.shopping.entities.ListEntity;
import com.github.va1m.shopping.entities.ListItemEntity;
import com.github.va1m.shopping.rest.UpdateResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Contains tests for the updating list cases
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class TestUpdatingLists {

    @Autowired
    private TestRestTemplate restTemplate;

    /** Test case with no conflicts */
    @Test
    public void testUpdatingValid() throws CloneNotSupportedException {

        ListEntity updatedList = (ListEntity) referenceListCPUs.clone();

        updatedList.setName("My CPUs");
        updatedList.setDescription("Processors for my servers #1, #6, #12");
        updatedList.setDevice(MACBOOK);

        // add one item in the list
        addItem(updatedList,
            new ListItemEntity(null, 0, "Cyrix", "Cyrix MII", true, 1, false, null, null, null));

        Iterator<ListItemEntity> iterator = updatedList.getListItems().iterator();
        // mark another item as deleted
        ListItemEntity deletedItem = iterator.next();
        deletedItem.setIsDeleted(true);
        // uncheck yet another item
        iterator.next().setChecked(false);

        final String itemUri = SERVICE_URI + updatedList.getId();
        ResponseEntity<UpdateResult> postResponse = this.restTemplate
            .withBasicAuth(MARK_LOGIN, PASSWORD)
            .postForEntity(itemUri, updatedList, UpdateResult.class);

        assertThat(postResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(postResponse.hasBody(), is(true));
        assertThat(postResponse.getBody().getConflictedItems(), is(empty()));
        assertThat(postResponse.getBody().getNewRemoteItems(), is(empty()));

        // Retrieve edited list
        ResponseEntity<ListEntity> getResponse = this.restTemplate
            .withBasicAuth(MARK_LOGIN, PASSWORD)
            .getForEntity(itemUri, ListEntity.class);

        ListEntity remoteList = getResponse.getBody();
        assertThat(remoteList.getOwner(), is(MARK));
        assertThat(remoteList.getDevice(), nullValue());
        assertThat(remoteList.getName(), is(updatedList.getName()));
        assertThat(remoteList.getDescription(), is(updatedList.getDescription()));
        assertThat(remoteList.getListItems().size(), is(updatedList.getListItems().size()));

        ListItemEntity[] remoteItems = remoteList.getListItems().toArray(new ListItemEntity[0]);
        ListItemEntity[] originItems = updatedList.getListItems().toArray(new ListItemEntity[0]);

        for (int i = 0; i < remoteItems.length; i++) {
            int expectedVersion = (i==2) ? 0:1;
            assertThat(remoteItems[i].getVersion(), is(expectedVersion));
            assertThat(remoteItems[i].getIsDeleted(), is(originItems[i].getIsDeleted()));
            assertThat(remoteItems[i].getChecked(), is(originItems[i].getChecked()));
            assertThat(remoteItems[i].getName(), is(originItems[i].getName()));
            assertThat(remoteItems[i].getDescription(), is(originItems[i].getDescription()));
            assertThat(remoteItems[i].getQuantity(), is(originItems[i].getQuantity()));
            assertThat(remoteItems[i].getAuthor(), is(MARK));
            assertThat(remoteItems[i].getDevice(), is(updatedList.getDevice()));
        }
    }

    /** Test case with conflicts and their resolving */
    @Test
    public void testUpdatingWithConflict() throws CloneNotSupportedException {

        ListEntity updatedList = (ListEntity) referenceListGifts.clone();

        updatedList.setDevice(IPHONE);

        Iterator<ListItemEntity> iterator = updatedList.getListItems().iterator();
        // emulate saving the "Candy" item with the older version
        ListItemEntity item = iterator.next();
        item.setVersion(0);

        final String itemUri = SERVICE_URI + updatedList.getId();
        ResponseEntity<UpdateResult> postResponse = this.restTemplate
            .withBasicAuth(MIRANDA_LOGIN, PASSWORD)
            .postForEntity(itemUri, updatedList, UpdateResult.class);

        assertThat(postResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(postResponse.hasBody(), is(true));
        assertThat(postResponse.getBody().getNewRemoteItems(), is(empty()));

        ListItemEntity expectedItem = (ListItemEntity) item.clone();
        expectedItem.setVersion(1);

        Collection<ListItemEntity> conflictedItems = postResponse.getBody().getConflictedItems();
        assertThat(conflictedItems.size(), is(1));
        assertThat(conflictedItems, hasItem(expectedItem));

        // Resolve conflict
        updatedList.setForce(true);
        assertThat(item.getVersion(), is(0));

        postResponse = this.restTemplate
            .withBasicAuth(MIRANDA_LOGIN, PASSWORD)
            .postForEntity(itemUri, updatedList, UpdateResult.class);

        assertThat(postResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(postResponse.hasBody(), is(true));
        assertThat(postResponse.getBody().getNewRemoteItems(), is(empty()));
        assertThat(postResponse.getBody().getConflictedItems(), is(empty()));

    }

    /** Test case with updating shopping list with nonexistent id */
    @Test
    public void testUpdateListWithWrongId() throws CloneNotSupportedException {
        updateList(13L, MARK_LOGIN, MACBOOK, HttpStatus.NOT_FOUND);
    }

    /** Test case with updating shopping list belongs another user */
    @Test
    public void testUpdateListWithWrongUser() throws CloneNotSupportedException {
        updateList(1L, MIRANDA_LOGIN, IPHONE, HttpStatus.NOT_FOUND);
    }

    /** Test case with updating list with device belongs another user */
    @Test
    public void testUpdateListWithWrongDevice() throws CloneNotSupportedException {
        updateList(1L, MARK_LOGIN, IPHONE, HttpStatus.BAD_REQUEST);
    }

    /** Method has common code for different test cases */
    private void updateList(long id, String user, DeviceEntity dev, HttpStatus expectedStatus) throws CloneNotSupportedException {
        ListEntity updatedList = (ListEntity) referenceListCPUs.clone();

        updatedList.setName("My CPUs");
        updatedList.setDescription("Processors for my servers #1, #6, #12");
        updatedList.setDevice(dev);

        final String itemUri = SERVICE_URI + id;
        ResponseEntity<String> postResponse = this.restTemplate
            .withBasicAuth(user, PASSWORD)
            .postForEntity(itemUri, updatedList, String.class);

        assertThat(postResponse.getStatusCode(), is(expectedStatus));
        assertThat(postResponse.getBody(), is(not(containsString("listItems"))));
    }

    /**
     * Add item in a list.
     */
    private void addItem(ListEntity list, ListItemEntity item) {
        List<ListItemEntity> newList = new ArrayList<>(list.getListItems());
        newList.add(item);
        list.setListItems(newList);
    }
}
