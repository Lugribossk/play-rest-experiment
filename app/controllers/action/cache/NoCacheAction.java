package controllers.action.cache;

import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

/**
 * Action implementation of {@link NoCache}.
 *
 * @author Bo Gotthardt
 */
public class NoCacheAction extends Action<NoCache> {
    @Override
    public Result call(Http.Context ctx) throws Throwable {
        ctx.response().setHeader(Http.HeaderNames.CACHE_CONTROL, "no-cache");
        return delegate.call(ctx);
    }
}
