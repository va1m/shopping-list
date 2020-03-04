package com.github.va1m.shopping.integration;

import static com.github.va1m.shopping.integration.MasterData.MIRANDA;
import static com.github.va1m.shopping.integration.MasterData.MIRANDA_LOGIN;
import static com.github.va1m.shopping.integration.MasterData.PASSWORD;
import static com.github.va1m.shopping.integration.MasterData.SAMSUNG_TAB;
import static com.github.va1m.shopping.integration.MasterData.SERVICE_URI;
import static com.github.va1m.shopping.integration.MasterData.newListTemplate;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.github.va1m.shopping.entities.ListEntity;
import com.github.va1m.shopping.entities.ListItemEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Contains tests for the adding new list cases
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class TestAppendingLists {

    @Autowired
    private TestRestTemplate restTemplate;

    /** Tests normal case */
    @Test
    public void addNewValidList() throws CloneNotSupportedException {

        ListEntity newList = (ListEntity) newListTemplate.clone();

        ResponseEntity<String> postResponse = this.restTemplate
            .withBasicAuth(MIRANDA_LOGIN, PASSWORD)
            .postForEntity(SERVICE_URI, newList, String.class);

        assertThat(postResponse.getStatusCode(), is(HttpStatus.CREATED));
        String newListUrl = postResponse.getHeaders().get(HttpHeaders.LOCATION).get(0);
        assertThat(newListUrl, containsString("/lists/"));
        assertThat(postResponse.hasBody(), is(false));

        // Retrieve appended list
        ResponseEntity<ListEntity> getResponse = this.restTemplate
            .withBasicAuth(MIRANDA_LOGIN, PASSWORD)
            .getForEntity(newListUrl, ListEntity.class);

        ListEntity addedList = getResponse.getBody();
        assertThat(addedList.getOwner(), is(MIRANDA));
        assertThat(addedList.getDevice(), nullValue());
        assertThat(addedList.getName(), is(newList.getName()));
        assertThat(addedList.getDescription(), is(newList.getDescription()));
        assertThat(addedList.getListItems().size(), is(newList.getListItems().size()));

        ListItemEntity[] items = addedList.getListItems().toArray(new ListItemEntity[0]);
        ListItemEntity[] refItems = newList.getListItems().toArray(new ListItemEntity[0]);

        for (int i = 0; i < items.length; i++) {
            assertThat(items[i].getVersion(), is(refItems[i].getVersion()));
            assertThat(items[i].getIsDeleted(), is(refItems[i].getIsDeleted()));
            assertThat(items[i].getChecked(), is(refItems[i].getChecked()));
            assertThat(items[i].getName(), is(refItems[i].getName()));
            assertThat(items[i].getDescription(), is(refItems[i].getDescription()));
            assertThat(items[i].getQuantity(), is(refItems[i].getQuantity()));
            assertThat(items[i].getAuthor(), is(MIRANDA));
            assertThat(items[i].getDevice(), is(newList.getDevice()));
        }

    }

    /** Test case with shopping list with wrong id */
    @Test
    public void addListWithWrongId() throws CloneNotSupportedException {

        ListEntity newList = (ListEntity) newListTemplate.clone();

        // Any specific ID must be ignored
        newList.setId(1L);

        ResponseEntity<String> postResponse = this.restTemplate
            .withBasicAuth(MIRANDA_LOGIN, PASSWORD)
            .postForEntity(SERVICE_URI, newList, String.class);

        assertThat(postResponse.getStatusCode(), is(HttpStatus.CREATED));
        String newListUrl = postResponse.getHeaders().get(HttpHeaders.LOCATION).get(0);
        assertThat(newListUrl, containsString("/lists/"));
        assertThat(postResponse.hasBody(), is(false));

        Long newId = Long.parseLong(newListUrl.substring(newListUrl.length() - 1));
        assertThat(newId, is(not(newList.getId())));

    }

    /** Test case with null-list */
    @Test
    public void addNullList() throws URISyntaxException {

        RequestEntity<String> request = RequestEntity
            .post(new URI(SERVICE_URI))
            .contentType(MediaType.APPLICATION_JSON)
            .body(null);

        ResponseEntity<String> postResponse = restTemplate
            .withBasicAuth(MIRANDA_LOGIN, PASSWORD)
            .exchange(request, String.class);

        assertThat(postResponse.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(postResponse.getHeaders().get(HttpHeaders.LOCATION), is(nullValue()));

    }

    /** Test case with device wich belongs to a different user */
    @Test
    public void testAddListWithWrongDevice() throws CloneNotSupportedException {

        ListEntity newList = (ListEntity) newListTemplate.clone();

        // Set up Mark's device to the Miranda's list
        newList.setDevice(SAMSUNG_TAB);

        ResponseEntity<String> postResponse = this.restTemplate
            .withBasicAuth(MIRANDA_LOGIN, PASSWORD)
            .postForEntity(SERVICE_URI, newList, String.class);

        assertThat(postResponse.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(postResponse.getHeaders().get(HttpHeaders.LOCATION), is(nullValue()));
    }
}
