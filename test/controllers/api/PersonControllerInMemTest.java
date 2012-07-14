package controllers.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.net.MediaType;
import models.person.Person;
import org.codehaus.jackson.node.ObjectNode;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import util.EbeanInMemoryTest;

import java.util.List;
import java.util.Map;

import static util.fest.PlayAssertions.*;
import static play.test.Helpers.*;

/**
 * Tests for {@link PersonController}.
 *
 * @author Bo Gotthardt
 */
public class PersonControllerInMemTest extends EbeanInMemoryTest {
    @Test
    public void testGet() {
        Person p = new Person("test");
        p.save();

        Result result = routeAndCall(fakeRequest("GET", "/api/persons/" + p.getId()));

        assertThat(result).hasStatus(Http.Status.OK)
                          .hasJSONContent(p);
    }

    @Test
    public void testGetNotFound() {
        Result result = routeAndCall(fakeRequest("GET", "/api/persons/1"));

        assertThat(result).hasStatus(Http.Status.NOT_FOUND)
                          .hasContentType(MediaType.JSON_UTF_8);
    }

    @Test
    public void testList() {
        Person p1 = new Person("test1");
        p1.save();
        Person p2 = new Person("test2");
        p2.save();

        Result result = routeAndCall(fakeRequest("GET", "/api/persons"));

        assertThat(result).hasStatus(Http.Status.OK)
                          .hasJSONContent(ImmutableList.of(p1, p2));
    }

    @Test
    public void testListQuery() {
        Person p1 = new Person("test1");
        p1.save();
        Person p2 = new Person("test2");
        p2.save();

        Result result = routeAndCall(fakeRequest("GET", "/api/persons?q=2"));

        assertThat(result).hasStatus(Http.Status.OK)
                          .hasJSONContent(ImmutableList.of(p2));
    }

    @Test
     public void testListLimitOffset() {
        Person p1 = new Person("test1");
        p1.save();
        Person p2 = new Person("test2");
        p2.save();
        Person p3 = new Person("test3");
        p3.save();

        Result result = routeAndCall(fakeRequest("GET", "/api/persons?limit=1&offset=1"));

        assertThat(result).hasStatus(Http.Status.OK)
                          .hasJSONContent(ImmutableList.of(p2));
    }

    @Test
    public void testListLimit0ReturnsAll() {
        Person p1 = new Person("test1");
        p1.save();
        Person p2 = new Person("test2");
        p2.save();
        Person p3 = new Person("test3");
        p3.save();

        Result result = routeAndCall(fakeRequest("GET", "/api/persons?limit=0&offset=1"));

        assertThat(result).hasStatus(Http.Status.OK)
                          .hasJSONContent(ImmutableList.of(p1, p2, p3));
    }

    @Test
    public void testCreate() {
        ObjectNode input = Json.newObject();
        input.put("name", "test");

        Result result = routeAndCall(fakeRequest("POST", "/api/persons").withJsonBody(input));
        List<Person> persons = Person.find.all();

        assertThat(result).hasStatus(Http.Status.OK);
        assertThat(persons).hasSize(1);
        assertThat(persons.get(0).getName()).isEqualTo("test");

//        int id = Json.parse(contentAsString(result)).get("id").asInt();
//        Person p = Person.find.byId(id);
//        assertThat(p).isNotNull();
//        assertThat(p.getName()).isEqualTo("test");
    }

    @Test
    public void testCreateInvalidPropertyValue() {
        ObjectNode input = Json.newObject();
        input.putNull("name");

        Result result = routeAndCall(fakeRequest("POST", "/api/persons").withJsonBody(input));

        assertThat(result).hasStatus(Http.Status.BAD_REQUEST)
                          .hasContentType(MediaType.JSON_UTF_8);
        assertThat(Person.find.all()).isEmpty();
    }

    @Test
    public void testCreateFormBodyInputNotAllowed() {
        Map<String, String> input = ImmutableMap.of("name", "test");

        Result result = routeAndCall(fakeRequest("POST", "/api/persons").withFormUrlEncodedBody(input));

        assertThat(result).hasStatus(Http.Status.BAD_REQUEST)
                          .hasContentType(MediaType.JSON_UTF_8);
        assertThat(Person.find.all()).isEmpty();
    }

    @Test
    public void testCreateParameterInputNotAllowed() {
        Result result = routeAndCall(fakeRequest("POST", "/api/persons?name=test"));

        assertThat(result).hasStatus(Http.Status.BAD_REQUEST)
                          .hasContentType(MediaType.JSON_UTF_8);
        assertThat(Person.find.all()).isEmpty();
    }

    @Test
    public void testUpdate() {
        Person p = new Person("test");
        p.save();

        ObjectNode input = Json.newObject();
        input.put("name", "test2");

        Result result = routeAndCall(fakeRequest("PUT", "/api/persons/" + p.getId()).withJsonBody(input, "PUT"));
        p.refresh();

        assertThat(result).hasStatus(Http.Status.OK)
                          .hasJSONContent(p);
        assertThat(p.getName()).isEqualTo("test2");
    }

    @Test
    public void testUpdateDoesNotOverwriteId() {
        Person p = new Person("test");
        p.save();
        int oldId = p.getId();

        ObjectNode input = Json.newObject();
        input.put("name", "test2");
        input.put("id", oldId + 100);

        Result result = routeAndCall(fakeRequest("PUT", "/api/persons/" + oldId).withJsonBody(input, "PUT"));
        p.refresh();

        assertThat(result).hasStatus(Http.Status.OK);
        assertThat(p.getId()).isEqualTo(oldId);
    }

    @Test
    public void testUpdateNotFound() {
        ObjectNode input = Json.newObject();
        input.put("name", "test");

        Result result = routeAndCall(fakeRequest("PUT", "/api/persons/1").withJsonBody(input, "PUT"));

        assertThat(result).hasStatus(Http.Status.NOT_FOUND)
                          .hasContentType(MediaType.JSON_UTF_8);
    }
}