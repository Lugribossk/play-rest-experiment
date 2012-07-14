package util.fest;

import com.google.common.net.MediaType;
import org.fest.assertions.api.AbstractAssert;
import org.junit.ComparisonFailure;
import play.libs.Json;
import play.mvc.Result;
import play.test.Helpers;

/**
 * Custom FEST assertions for {@link Result}.
 *
 * @author Bo Gotthardt
 */
public class ResultAssert extends AbstractAssert<ResultAssert, Result>{

    /**
     * Constructor.
     * @param actual the actual value
     */
    public ResultAssert(Result actual) {
        super(actual, ResultAssert.class);
    }

    /**
     * Assert that the result has the specified HTTP status code.
     *
     * @param status the status code
     * @return this, for chaining
     * @see play.mvc.Http.Status
     */
    public ResultAssert hasStatus(int status) {
        isNotNull();

        if (Helpers.status(actual) != status) {
            throw new ComparisonFailure("Status", String.valueOf(status), String.valueOf(Helpers.status(actual)));
        }

        return this;
    }

    /**
     * Assert that the result has the specified content type.
     *
     * @param type the content type
     * @return this, for chaining
     * @see MediaType
     */
    public ResultAssert hasContentType(MediaType type) {
        isNotNull();

        // TODO Play does not include the UTF-8 part, why?
        String expectedType = type.toString().substring(0, type.toString().indexOf(";"));
        if (!Helpers.contentType(actual).equals(expectedType)) {
            throw new ComparisonFailure("Content type", expectedType, Helpers.contentType(actual));
        }

        return this;
    }

    /**
     * Assert that the result's content is a JSON object equal to a JSON object created from the specified object.
     *
     * @param expected the object
     * @return this, for chaining
     */
    public ResultAssert hasJSONContent(Object expected) {
        isNotNull();
        hasContentType(MediaType.JSON_UTF_8);

        String jsonStringExpected = Json.toJson(expected).toString();
        if (!Helpers.contentAsString(actual).equals(jsonStringExpected)) {
            throw new ComparisonFailure("JSON content", jsonStringExpected, Helpers.contentAsString(actual));
        }

        return this;
    }
}
