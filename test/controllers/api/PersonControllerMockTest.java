package controllers.api;

import com.avaje.ebean.Query;
import com.google.common.collect.ImmutableList;
import models.person.Person;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import util.EbeanMockTest;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static play.test.Helpers.*;

/**
 * Tests for {@link PersonController}.
 *
 * @author Bo Gotthardt
 */
public class PersonControllerMockTest extends EbeanMockTest {

    @Test
    public void testGetFound() {
        Person person = new Person("test");

        when(mockServer.find(Person.class, 1)).thenReturn(person);

        Result result = routeAndCall(fakeRequest("GET", "/api/persons/1"));

        assertThat(status(result)).isEqualTo(Http.Status.OK);
        assertThat(contentType(result)).isEqualTo("application/json");
        assertThat(contentAsString(result)).isEqualTo(Json.toJson(person).toString());
    }

    @Test
    public void testGetNotFound() {
        Result result = routeAndCall(fakeRequest("GET", "/api/persons/1"));

        assertThat(status(result)).isEqualTo(Http.Status.NOT_FOUND);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testList() {
        List<Person> persons = ImmutableList.of(new Person("test1"), new Person("test2"));

        Query<Person> query = mock(Query.class);
        when(query.setMaxRows(anyInt())).thenReturn(query);
        when(query.setFirstRow(anyInt())).thenReturn(query);
        when(query.findList()).thenReturn(persons);

        when(mockServer.find(Person.class)).thenReturn(query);

        Result result = routeAndCall(fakeRequest("GET", "/api/persons"));

        assertThat(status(result)).isEqualTo(Http.Status.OK);
        assertThat(contentType(result)).isEqualTo("application/json");
        assertThat(contentAsString(result)).isEqualTo(Json.toJson(persons).toString());
    }

}
