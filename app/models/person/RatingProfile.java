package models.person;

import com.avaje.ebean.validation.NotEmpty;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Bo Gotthardt
 */
@Embeddable
public class RatingProfile {
    // The rating ID used on the rating site
    private int ratingId;

    private int rating;

    @Nullable
    @NotEmpty
    private String nickname;

    @Nullable // Should be not null, but a nullable embedded with a notnull field does not seem to work
    @Temporal(TemporalType.TIMESTAMP)
    private DateTime updated;

    public RatingProfile(int ratingId, int rating) {
        this.ratingId = ratingId;
        this.rating = rating;
        this.nickname = null;
        updated();
    }

    public int getRatingId() {
        return ratingId;
    }

    public int getRating() {
        return rating;
    }


    @Nullable
    public String getNickname() {
        return nickname;
    }

    @Nullable
    public DateTime getUpdated() {
        return updated;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        updated();
    }

    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
        updated();
    }

    public void setRating(int rating) {
        this.rating = rating;
        updated();
    }

    private void updated() {
        this.updated = new DateTime();
    }
}
