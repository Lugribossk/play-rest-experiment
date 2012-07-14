package models.person;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Bo Gotthardt
 */
@Entity
public class RatingProfile {
    @Id
    private int id;

    private int rating;

    private String name;
}
