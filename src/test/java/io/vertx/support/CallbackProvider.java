package io.vertx.support;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface CallbackProvider {

  static void callbackWithSuccess(Handler<AsyncResult<String>> callback) {
    throw new AssertionError("stub");
  }

  public static void callbackWithFailure(Handler<AsyncResult<String>> callback) {
    throw new AssertionError("stub");
  }
}
