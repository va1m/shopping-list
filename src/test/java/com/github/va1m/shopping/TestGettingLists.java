package com.github.va1m.shopping;

import com.github.va1m.shopping.entities.ListEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.va1m.shopping.MasterData.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Contains tests for the getting list cases
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class TestGettingLists {

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Test authorization.
     * Fail if provides data without credentials.
     */
    @Test
    public void testAuthorisation() {
        ResponseEntity<String> response = this.restTemplate.getForEntity(SERVICE_URI, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
        assertThat(response.getBody(), is(not(containsString("Flowers for Marta's birthday"))));
    }

    /** Tests the normal case with getting all Mark's shopping lists */
    @Test
    public void testGetMarksAllLists() {

        ResponseEntity<ListEntity[]> response = this.restTemplate
                .withBasicAuth(MARK_LOGIN, PASSWORD)
                .getForEntity(SERVICE_URI, ListEntity[].class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        ListEntity[] lists = response.getBody();
        assertThat(lists.length, is(greaterThanOrEqualTo(3)));
        assertThat(lists[0], is(referenceListCPUs));
        assertThat(lists[1], is(referenceListHDDs));
        assertThat(lists[2], is(referenceListRAMs));
    }

    /** Tests the normal case with getting all Miranda's shopping lists */
    @Test
    public void testGetMirandaAllLists() {

        ResponseEntity<ListEntity[]> response = this.restTemplate
                .withBasicAuth(MIRANDA_LOGIN, PASSWORD)
                .getForEntity(SERVICE_URI, ListEntity[].class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        ListEntity[] lists = response.getBody();
        assertThat(lists.length, is(greaterThanOrEqualTo(3)));
        assertThat(lists[0], is(referenceListFlowers));
        assertThat(lists[1], is(referenceListGifts));
        assertThat(lists[2], is(referenceListNew));
    }

    /** Tests the normal case with getting specific shopping list */
    @Test
    public void testGetSpecificList() {
        ResponseEntity<ListEntity> response = this.restTemplate
                .withBasicAuth(MARK_LOGIN, PASSWORD)
                .getForEntity(SERVICE_URI + "1", ListEntity.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        ListEntity list = response.getBody();

        assertThat(list, is(referenceListCPUs));
    }

    /** Test case nonexistent list id */
    @Test
    public void testGetWrongIdList() {
        ResponseEntity<String> response = this.restTemplate
                .withBasicAuth(MARK_LOGIN, PASSWORD)
                .getForEntity(SERVICE_URI + "/13", String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
        assertThat(response.getBody(), is(not(containsString("listItems"))));
    }

    /** Test case getting list belongs a different user */
    @Test
    public void testGetOtherUserList() {
        ResponseEntity<String> response = this.restTemplate
                .withBasicAuth(MARK_LOGIN, PASSWORD)
                .getForEntity(SERVICE_URI + "/3", String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
        assertThat(response.getBody(), is(not(containsString("listItems"))));
    }
}
