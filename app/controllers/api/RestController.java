package controllers.api;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.InvalidValue;
import com.avaje.ebean.Query;
import controllers.util.JsonController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import play.db.ebean.Model;
import play.mvc.Result;

import javax.annotation.Nullable;
import java.io.IOException;

/**
 * Abstract REST API with basic functionality.
 *
 * @author Bo Gotthardt
 */
@Slf4j
@RequiredArgsConstructor
public class RestController extends JsonController {
    private final Class<? extends Model> type;

    /**
     * Return the object with the specified ID if it exists.
     *
     * @param id the object ID
     * @return a JSON result with the object, or error 404 if it was not found
     */
    public Result one(int id) {
        Model item = Ebean.find(type, id);

        if (item != null) {
            return ok(item);
        } else {
            return notFound(id);
        }
    }

    /**
     * Return all objects (possibly only those that match a search query).
     *
     * @param searchQuery the search query, or null to get all objects
     * @param limit the maximum number of objects to return, or 0 for no limit
     * @param offset the offset into the result set to start returning from
     * @return a JSON result with the list (possibly empty) of objects, or error 400
     */
    public Result many(@Nullable String searchQuery, int limit, int offset) {
        if (limit < 0) {
            return badRequest("Limit must be >= 0, was " + limit + ". If set to 0, all objects are returned and offset is ignored. If not set, defaults to 10.");
        }

        if (offset < 0) {
            return badRequest("Offset must be >= 0 , was " + offset + ". If not set, defaults to 0.");
        }

        Query<? extends Model> dbQuery = Ebean.find(type).orderBy().asc("id");
        if (limit != 0) {
            dbQuery = dbQuery.setMaxRows(limit).setFirstRow(offset);
        }
        if (searchQuery != null) {
            dbQuery = dbQuery.where().like("name", "%" + searchQuery + "%").query();
        }

        return ok(dbQuery.findList());
    }

    /**
     * Create a new object, based on the JSON properties in the request body.
     *
     * @return a JSON result with the new object, or error 400 or 500
     */
    public Result create() {
        return createUpdate(null);
    }

    /**
     * Update an existing object, based on the JSON properties in the request body.
     * <br/>
     * Any ID property in the body will be ignored in favor of the specified one.
     *
     * @param id the object ID
     * @return a JSON result with the updated object, or error 400, 404 or 500
     */
    public Result update(int id) {
        return createUpdate(id);
    }

    /**
     * Utility method that either creates or updates an object as the logic is nearly the same.
     *
     * @param id the object ID to update, or null to create
     * @return a JSON result with the created/updated object, or error 400, 404 or 500
     */
    private Result createUpdate(@Nullable Integer id) {
        JsonNode json = request().body().asJson();

        // BodyParser.Json seems to still allow a null value sometimes, so lets not use it and do this instead.
        if (json == null) {
            log.debug("Create/update {}, input must be JSON.", type);
            return badRequest("Input data must be in JSON format.");
        }

        try {
            // TODO Disallow creating/updating sensitive properties via Jackson JsonViews
            Model item = OBJECT_MAPPER.treeToValue(json, type);
            InvalidValue validationErrors = Ebean.validate(item);

            if (validationErrors != null) {
                log.debug("Create/update {}, validation error: {}", type, validationErrors);
                return badRequest(createValidationErrorMessage(validationErrors));
            }

            if (id != null) {
                // Updating
                if (objectExists(id)) {
                    item.update(id);
                } else {
                    return notFound(id);
                }
            } else {
                // Creating
                item.save();
            }

            return ok(item);
        } catch (JsonMappingException e) {
            // Or disable FAIL_ON_UNKNOWN_PROPERTIES ?
            log.debug("Create/update {}, field mapping problem: ", type);
            return badRequest("Problem with field '" + e.getPath().get(0).getFieldName() + "'.");
        } catch (IOException e) {
            log.error("Error mapping JSON to create/update.", e);
            return internalServerError();
        }
    }

    /**
     * Delete the object with the specified ID if it exists.
     *
     * @param id the object ID
     * @return an empty result, or error 404
     */
    public Result delete(int id) {
        if (objectExists(id)) {
            Ebean.delete(type, id);
            return ok(); // TODO return some sort of JSON?
        } else {
            return notFound(id);
        }
    }


    /**
     * Check whether an object of the specified type with the specified ID exists.
     *
     * @param id the object ID
     * @return whether the object exists
     */
    private boolean objectExists(int id) {
        // Presumably this is (slightly) faster than retrieving the object.
        return Ebean.find(type).where().eq("id", id).findRowCount() == 1;
    }

    private static String createValidationErrorMessage(InvalidValue validationErrors) {
        InvalidValue[] errors = validationErrors.getErrors();

        return "There was a '" + errors[0].getValidatorKey() + "' problem with the '" + errors[0].getPropertyName() + "' property.";
    }
}
