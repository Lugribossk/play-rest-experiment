package models.person;

import com.avaje.ebean.validation.NotNull;
import models.tournament.Tournament;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author Bo Gotthardt
 */
@Entity
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
