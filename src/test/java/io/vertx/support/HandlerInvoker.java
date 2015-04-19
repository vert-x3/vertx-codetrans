package io.vertx.support;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface HandlerInvoker {

  static void invokeStringHandler(Handler<String> handler) {
    throw new UnsupportedOperationException();
  }

  static void invokeStringHandlerFirstParam(Handler<String> handler, String other) {
    throw new UnsupportedOperationException();
  }

  static void invokeStringHandlerLastParam(String other, Handler<String> handler) {
    throw new UnsupportedOperationException();
  }

  static void invokeAsyncResultHandlerSuccess(Handler<AsyncResult<String>> callback) {
    throw new AssertionError("stub");
  }

  static void invokeAsyncResultHandlerFailure(Handler<AsyncResult<String>> callback) {
    throw new AssertionError("stub");
  }

}
