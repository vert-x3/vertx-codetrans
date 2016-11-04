package io.vertx.support;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface HandlerInvoker {

  static void invokeStringHandler(Handler<String> handler) {
    handler.handle("callback_value");
  }

  static void invokeStringHandlerFirstParam(Handler<String> handler, String other) {
    handler.handle(other);
  }

  static void invokeStringHandlerLastParam(String other, Handler<String> handler) {
    handler.handle(other);
  }

  static void invokeAsyncResultHandlerSuccess(Handler<AsyncResult<String>> callback) {
    callback.handle(Future.succeededFuture("hello"));
  }

  static void invokeAsyncResultHandlerFailure(Handler<AsyncResult<String>> callback) {
    callback.handle(Future.failedFuture("oh no"));
  }

}
