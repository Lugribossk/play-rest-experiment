package models.person;

import com.avaje.ebean.validation.NotNull;
import play.db.ebean.Model;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * @author Bo Gotthardt
 */
@Entity
public class Person extends Model {
    @Id
    private int id;

    @NotNull
    private String name;

    @Nullable
    @OneToOne
    private RatingProfile ratingProfile;

    public static Finder<Integer, Person> find = new Finder<>(Integer.class, Person.class);

    public Person(String name) {
        this.name = name;
    }

    // update(object, id) requires this to be public
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
