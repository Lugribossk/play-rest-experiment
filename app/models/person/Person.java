package models.person;

import com.avaje.ebean.validation.Email;
import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import play.db.ebean.Model;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * A person, the base object for keeping track of people. Enables reuse of data that does not change between tournaments.
 *
 * @author Bo Gotthardt
 */
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Person extends Model {
    // update(object, id) requires this to have a public setter
    /**
     * Database ID.
     */
    @Id
    private int id;

    /**
     * Full name.
     */
    @NotNull
    @NotEmpty
    private String name;

    /**
     * Email address, possibly null.
     */
    @Nullable
    @Email
    private String email;

    @NotNull
    @OneToMany
    private Set<Player> players = Sets.newHashSet();

//    @Nullable
//    @Embedded
//    private RatingProfile ratingProfile;

    public static Finder<Integer, Person> find = new Finder<>(Integer.class, Person.class);

    public Person(String name) {
        this.name = name;
    }
}