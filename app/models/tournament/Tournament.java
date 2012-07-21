package models.tournament;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import models.person.Player;
import models.person.User;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * @author Bo Gotthardt
 */
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Tournament extends Model {
    @Id
    private int id;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @ManyToMany
    private Set<User> organizers = Sets.newHashSet();

    @NotNull
    @OneToMany
    private Set<Player> players = Sets.newHashSet();
}