package models.person;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * A user, i.e. a {@link Person} that has an account to log into the system.
 *
 * @author Bo Gotthardt
 */
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class User {
    /**
     * Database ID.
     */
    @Id
    private int id;

    /**
     * Username.
     */
    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String username;

    /**
     * Hashed password.
     */
    @JsonIgnore
    @NotNull
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private String hashedPassword;


    /**
     * Constructor.
     *
     * @param username the username
     */
    public User(String username) {
        this.username = username;
    }

    /**
     * Sets this user's password from the specified plaintext string.
     *
     * @param plaintext the new password in plaintext
     */
    public void setPlaintextPassword(String plaintext) {
        // Use a hashing algorithm designed for passwords.
        hashedPassword = BCrypt.hashpw(plaintext, BCrypt.gensalt());
    }

    /**
     * Returns whether the specified plaintext string is identical to this user's password.
     *
     * @param possiblePlaintext the plaintext string
     * @return whether the specified plaintext string is identical to this user's password
     */
    public boolean checkPassword(String possiblePlaintext) {
        return BCrypt.checkpw(possiblePlaintext, hashedPassword);
    }
}