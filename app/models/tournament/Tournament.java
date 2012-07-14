package models.tournament;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Bo Gotthardt
 */
@Entity
public class Tournament extends Model {
    @Id
    private int id;
}
