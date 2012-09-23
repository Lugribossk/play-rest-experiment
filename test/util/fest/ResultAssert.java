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
public class ResultAssert extends AbstractAssert<ResultAssert, Result> {

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

        compare(String.valueOf(status), String.valueOf(Helpers.status(actual)), "Status");

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
        compare(Helpers.contentType(actual), expectedType, "Content type");

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
        compare(Helpers.contentAsString(actual), jsonStringExpected, "JSON content");

        return this;
    }

    /**
     * Assert that the result has a header with the specified value.
     *
     * @param headerName the header name, from {@link play.mvc.Http.HeaderNames}
     * @param expected the header value
     * @return this, for chaining
     */
    public ResultAssert hasHeader(String headerName, String expected) {
        isNotNull();

        compare(Helpers.header(headerName, actual), expected, "Header value for '" + headerName + "'");

        return this;
    }


    /**
     * Compare the specified actual and expected values, and throw a ComparisonFailure if they are not equal.
     * The type of the values are Strings to allow us to use ComparisonFailure for IDE integration.
     *
     * @param actual the actual value
     * @param expected the expected value
     * @param message the message to show on failure
     */
    private static void compare(String actual, String expected, String message) {
        if (actual == null || !actual.equals(expected)) {
            throw new ComparisonFailure(message, expected, actual);
        }
    }
}
