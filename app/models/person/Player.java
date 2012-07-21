package models.person;

import com.avaje.ebean.validation.NotNull;
import lombok.Getter;
import lombok.Setter;
import models.tournament.Tournament;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * A {@link Person}'s participation in a specific {@link  Tournament}.
 *
 * @author Bo Gotthardt
 */
@Entity
@Getter
@Setter
public class Player extends Model {
    @Id
    private int id;

    @NotNull
    @ManyToOne
    private Person person;

    @NotNull
    @ManyToOne
    private Tournament tournament;
}
