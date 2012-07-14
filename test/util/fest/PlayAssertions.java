package util.fest;


import org.fest.assertions.api.Assertions;
import play.mvc.Result;

/**
 * Entry point for custom FEST assertions.
 * Static import this instead of org.fest.assertions.api.Assertions.
 *
 * @author Bo Gotthardt
 */
public class PlayAssertions extends Assertions {
    public static ResultAssert assertThat(Result actual) {
        return new ResultAssert(actual);
    }
}
