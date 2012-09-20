package controllers.api;

import lombok.extern.slf4j.Slf4j;
import models.person.Person;
import play.mvc.Controller;
import play.mvc.Result;

import javax.annotation.Nullable;

/**
 * REST API controller for {@link Person}.
 *
 * @author Bo Gotthardt
 */
@Slf4j
public class PersonController extends Controller {
    private static final RestController controller = new RestController(Person.class);


    public static Result one(int id) {
        return controller.one(id);
    }

    public static Result many(@Nullable String searchQuery, int limit, int offset) {
        return controller.many(searchQuery, limit, offset);
    }

    public static Result create() {
        return controller.create();
    }

    public static Result update(int id) {
        return controller.update(id);
    }
}