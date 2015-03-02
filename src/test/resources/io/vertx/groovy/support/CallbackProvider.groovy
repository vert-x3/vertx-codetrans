package io.vertx.groovy.support

import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class CallbackProvider {

  static void callbackWithSuccess(Handler<AsyncResult<String>> callback) {
    callback.handle(Future.succeededFuture("hello"));
  }

  static void callbackWithFailure(Handler<AsyncResult<String>> callback) {
    callback.handle(Future.failedFuture("oh no"));
  }
}
