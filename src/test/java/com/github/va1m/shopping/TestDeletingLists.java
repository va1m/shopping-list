package com.github.va1m.shopping;

import com.github.va1m.shopping.entities.ListEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;

import static com.github.va1m.shopping.MasterData.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Contains tests for the deleting list cases
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class TestDeletingLists {

    @Autowired
    private TestRestTemplate restTemplate;

    /** Tests normal case */
    @Test
    public void testDeleteList() throws URISyntaxException, CloneNotSupportedException {

        String itemUrl = addNewList();

        RequestEntity<Void> request = RequestEntity
                .delete(new URI(itemUrl))
                .build();

        ResponseEntity<String> response = restTemplate
                .withBasicAuth(MIRANDA_LOGIN, PASSWORD)
                .exchange(request, String.class);


        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.hasBody(), is(false));

        // Try to retrieve deleted list
        ResponseEntity<String> getResponse = this.restTemplate
                .withBasicAuth(MIRANDA_LOGIN, PASSWORD)
                .getForEntity(itemUrl, String.class);

        assertThat(getResponse.getStatusCode(), is(HttpStatus.NOT_FOUND));
        assertThat(getResponse.getBody(), is(not(containsString("listItems"))));

    }

    /** Test case with deleting a shopping list which belongs to another user */
    @Test
    public void testDeleteListByWrongUser() throws URISyntaxException {

        String itemUri = SERVICE_URI + "1";

        RequestEntity<Void> request = RequestEntity
                .delete(new URI(itemUri))
                .build();

        ResponseEntity<String> response = restTemplate
                .withBasicAuth(MIRANDA_LOGIN, PASSWORD)
                .exchange(request, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    /** Test case with deleting a shopping list which nonexistent id */
    @Test
    public void testDeleteListByWrongId() throws URISyntaxException {

        String itemUri = SERVICE_URI + "13";

        RequestEntity<Void> request = RequestEntity
                .delete(new URI(itemUri))
                .build();

        ResponseEntity<String> response = restTemplate
                .withBasicAuth(MARK_LOGIN, PASSWORD)
                .exchange(request, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    /**
     * Store new list on the server
     *
     * @return URL of the list
     */
    private String addNewList() throws CloneNotSupportedException {

        ListEntity newList = (ListEntity) newListTemplate.clone();

        ResponseEntity<String> response = this.restTemplate
                .withBasicAuth(MIRANDA_LOGIN, PASSWORD)
                .postForEntity(SERVICE_URI, newList, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        return response.getHeaders().get(HttpHeaders.LOCATION).get(0);
    }

}
