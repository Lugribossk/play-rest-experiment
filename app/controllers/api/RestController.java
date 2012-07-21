package controllers.api;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.InvalidValue;
import com.avaje.ebean.Query;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import play.db.ebean.Model;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.annotation.Nullable;
import java.io.IOException;

/**
 * Abstract REST API with basic functionality.
 * <br/><br/>
 * Java can't use a generic type specified on the class for static methods, so they have to take the type to work on as
 * a parameter (or as a variable that subclasses set in a static initializer).
 * <br/>
 * Requests can't be routed directly to methods in superclasses, so concrete subclasses have to create their own methods
 * that call these.
 *
 * @author Bo Gotthardt
 */
@Slf4j
public abstract class RestController extends Controller {
    // Reuse the same ObjectMapper instance for multiple calls, as this is how Jackson is designed to work.
    // Hopefully this will be fixed in a future version of Play.
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    //    protected static Class<? extends Model> type;

    /**
     * Return the object with the specified ID if it exists.
     *
     * @param type the object type
     * @param id the object ID
     * @return a JSON result with the object, or error 404 if it was not found
     */
    protected static Result one(Class<? extends Model> type, int id) {
        Model item = Ebean.find(type, id);

        if (item != null) {
            return okJson(item);
        } else {
            return notFoundJson(id);
        }
    }

    /**
     * Return all objects (possibly only those that match a search query).
     *
     * @param type the object type
     * @param searchQuery the search query, or null to get all objects
     * @param limit the maximum number of objects to return, or 0 for no limit
     * @param offset the offset into the result set to start returning from
     * @return a JSON result with the list (possibly empty) of objects, or error 400
     */
    protected static Result many(Class<? extends Model> type, @Nullable String searchQuery, int limit, int offset) {
        if (limit < 0) {
            return badRequestJson("Limit must be >= 0, was " + limit + ". If set to 0, all objects are returned and offset is ignored. If not set, defaults to 10.");
        }

        if (offset < 0) {
            return badRequestJson("Offset must be >= 0 , was " + offset + ". If not set, defaults to 0.");
        }

        Query<? extends Model> dbQuery = Ebean.find(type).orderBy().asc("id");
        if (limit != 0) {
            dbQuery = dbQuery.setMaxRows(limit).setFirstRow(offset);
        }
        if (searchQuery != null) {
            dbQuery = dbQuery.where().like("name", "%" + searchQuery + "%").query();
        }

        return okJson(dbQuery.findList());
    }

    /**
     * Create a new object, based on the JSON properties in the request body.
     *
     * @param type the object type
     * @return a JSON result with the new object, or error 400 or 500
     */
    protected static Result create(Class<? extends Model> type) {
        return createUpdate(type, null);
    }

    /**
     * Update an existing object, based on the JSON properties in the request body.
     * <br/>
     * Any ID property in the body will be ignored in favor of the specified one.
     *
     * @param type the object type
     * @param id the object ID
     * @return a JSON result with the updated object, or error 400, 404 or 500
     */
    protected static Result update(Class<? extends Model> type, int id) {
        return createUpdate(type, id);
    }

    /**
     * Utility method that either creates or updates an object as the logic is nearly the same.
     *
     * @param type the object type
     * @param id the object ID to update, or null to create
     * @return a JSON result with the created/updated object, or error 400, 404 or 500
     */
    private static Result createUpdate(Class<? extends Model> type, @Nullable Integer id) {
        JsonNode json = request().body().asJson();

        // BodyParser.Json seems to still allow a null value sometimes, so lets not use it and do this instead.
        if (json == null) {
            log.debug("Create/update {}, input must be JSON.", type);
            return badRequestJson("Input data must be in JSON format.");
        }

        try {
            // TODO Disallow creating/updating sensitive properties via Jackson JsonViews
            Model item = OBJECT_MAPPER.treeToValue(json, type);
            InvalidValue validationErrors = Ebean.validate(item);

            if (validationErrors != null) {
                log.debug("Create/update {}, validation error: {}", type, validationErrors);
                return badRequestJson(createValidationErrorMessage(validationErrors));
            }

            if (id != null) {
                // Updating
                if (objectExists(type, id)) {
                    item.update(id);
                } else {
                    return notFoundJson(id);
                }
            } else {
                // Creating
                item.save();
            }

            return okJson(item);
        } catch (JsonMappingException e) {
            // Or disable FAIL_ON_UNKNOWN_PROPERTIES ?
            log.debug("Create/update {}, field mapping problem: ", type);
            return badRequestJson("Problem with field '" + e.getPath().get(0).getFieldName() + "'.");
        } catch (IOException e) {
            log.error("Error mapping JSON to create/update.", e);
            return internalServerErrorJson();
        }
    }

    /**
     * Delete the object with the specified ID if it exists.
     *
     * @param type the object type
     * @param id the object ID
     * @return an empty result, or error 404
     */
    protected static Result delete(Class<? extends Model> type, int id) {
        if (objectExists(type, id)) {
            Ebean.delete(type, id);
            return ok(); // TODO return some sort of JSON?
        } else {
            return notFoundJson(id);
        }
    }


    /**
     * Check whether an object of the specified type with the specified ID exists.
     *
     * @param type the object type
     * @param id the object ID
     * @return whether the object exists
     */
    protected static boolean objectExists(Class<? extends Model> type, int id) {
        // Presumably this is (slightly) faster than retrieving the object.
        return Ebean.find(type).where().eq("id", id).findRowCount() == 1;
    }

    protected static String createValidationErrorMessage(InvalidValue validationErrors) {
        InvalidValue[] errors = validationErrors.getErrors();

        return "There was a '" + errors[0].getValidatorKey() + "' problem with the '" + errors[0].getPropertyName() + "' property.";
    }

    protected static Result okJson(Object data) {
        return ok(OBJECT_MAPPER.valueToTree(data));
    }

    protected static Result notFoundJson(int id) {
        ObjectNode output = Json.newObject();
        output.put("status", Http.Status.NOT_FOUND);
        output.put("message", "No object with id=" + id + " found.");

        return notFound(output);
    }

    protected static Result badRequestJson(String message) {
        ObjectNode output = Json.newObject();
        output.put("status", Http.Status.BAD_REQUEST);
        output.put("message", message);

        return badRequest(output);
    }

    protected static Result internalServerErrorJson() {
        ObjectNode output = Json.newObject();
        output.put("status", Http.Status.INTERNAL_SERVER_ERROR);
        output.put("message", "Internal server error.");

        return internalServerError(output);
    }
}
