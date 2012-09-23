package controllers.action.cache;

import com.google.gdata.util.common.base.Preconditions;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.TimeUnit;

/**
 * Action implementation of {@link ClientCache}.
 *
 * @author Bo Gotthardt
 */
public class ClientCacheAction extends Action<ClientCache> {
    @Override
    public Result call(Http.Context ctx) throws Throwable {
        long seconds = TimeUnit.SECONDS.convert(configuration.hours(), TimeUnit.HOURS);
        Preconditions.checkArgument(seconds != 0, "Cache duration of 0 not allowed, use @NoCache instead.");
        Preconditions.checkArgument(seconds > 0, "Negative cache duration not allowed.");

        ctx.response().setHeader(Http.HeaderNames.CACHE_CONTROL, Long.toString(seconds));
        return delegate.call(ctx);
    }
}
