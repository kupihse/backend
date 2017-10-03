import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Andreyko0 on 03/10/2017.
 */
public class App extends AbstractVerticle {

  public static void main(String[] args) throws Exception {
    BlockingQueue<AsyncResult<String>> q = new ArrayBlockingQueue<>(1);
    Vertx.vertx().deployVerticle(new App(), q::offer);
    AsyncResult<String> result = q.take();
    if (result.failed()) {
      throw new RuntimeException(result.cause());
    }
  }

  @Override
  public void start() throws Exception {
    Router router = Router.router(vertx);
    router.get("/hello/:name").handler(ctx -> {
      ctx.response().end("Hello "+ctx.pathParam("name"));
    });
    router.get("/hello").handler(ctx -> {
      ctx.response().end("Hello");
    });
    router.get("/json").handler(ctx -> {
      ctx.response().end(Json.encodePrettily(new User()));
    });
    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }
}
