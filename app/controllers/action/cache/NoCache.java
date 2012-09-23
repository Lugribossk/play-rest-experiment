package controllers.action.cache;

import play.mvc.With;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for instructing the client to not cache the response from the annotated action.
 *
 * @see ClientCache
 * @author Bo Gotthardt
 */
@With(NoCacheAction.class)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoCache {
}
