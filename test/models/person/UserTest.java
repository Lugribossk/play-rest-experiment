package models.person;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import static util.fest.PlayAssertions.assertThat;

/**
 * Tests for {@link User}.
 *
 * @author Bo Gotthardt
 */
public class UserTest {
    @Test
    public void testPasswordCheck() {
        User user = new User("test");
        user.setPlaintextPassword("mypass");

        assertThat(user.checkPassword("mypass")).isTrue();
    }


    @Test
    public void testPasswordNotJsonSerialized() {
        User user = new User("test");
        user.setPlaintextPassword("mypass");

        JsonNode json = new ObjectMapper().valueToTree(user);

        assertThat(json.toString()).doesNotContain("mypass")
                                   .doesNotContain(BCrypt.hashpw("mypass", BCrypt.gensalt()));
    }
}
