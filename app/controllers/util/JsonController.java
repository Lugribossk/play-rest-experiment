package controllers.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;

/**
 * Abstract class for creating JSON-based controllers.
 *
 * @author Bo Gotthardt
 */
public abstract class JsonController extends Controller {
    // Reuse the same ObjectMapper instance for multiple calls, as this is how Jackson is designed to work.
    // Hopefully this will be fixed in a future version of Play.
    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static Status ok(Object data) {
        return ok(OBJECT_MAPPER.valueToTree(data));
    }

    public static Status notFound(int id) {
        ObjectNode output = Json.newObject();
        output.put("status", Http.Status.NOT_FOUND);
        output.put("message", "No object with id=" + id + " found.");

        return notFound(output);
    }

    public static Status badRequest(String message) {
        ObjectNode output = Json.newObject();
        output.put("status", Http.Status.BAD_REQUEST);
        output.put("message", message);

        return badRequest(output);
    }

    public static Status internalServerError() {
        ObjectNode output = Json.newObject();
        output.put("status", Http.Status.INTERNAL_SERVER_ERROR);
        output.put("message", "Internal server error.");

        return internalServerError(output);
    }
}
