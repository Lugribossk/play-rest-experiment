package controllers.action.cache;

import play.mvc.With;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for instructing the client to cache the response from the annotated action for the specified duration.
 *
 * @see NoCache
 * @author Bo Gotthardt
 */
@With(ClientCacheAction.class)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ClientCache {
    /**
     * The number of hours to cache the response for.
     * Must be positive (for turning off caching, see {@link NoCache}).
     */
    long hours();
}
