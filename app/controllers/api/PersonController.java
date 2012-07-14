package controllers.api;

import models.person.Person;
import play.mvc.Result;

import javax.annotation.Nullable;

/**
 * REST API controller for {@link Person}.
 *
 * @author Bo Gotthardt
 */
public class PersonController extends RestController {
//    static {
//        type = Person.class;
//    }

    public static Result one(int id) {
        return one(Person.class, id);
    }

    public static Result many(@Nullable String searchQuery, int limit, int offset) {
        return many(Person.class, searchQuery, limit, offset);
    }

    public static Result create() {
        return create(Person.class);
    }

    public static Result update(int id) {
        return update(Person.class, id);
    }
}