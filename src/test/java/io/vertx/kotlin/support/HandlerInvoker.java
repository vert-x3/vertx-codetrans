package io.vertx.kotlin.support;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

/**
 * Author: Sergey Mashkov
 */
public class HandlerInvoker {

  public static void invokeStringHandler(Handler<String> handler) {
    handler.handle("callback_value");
  }

  public static void invokeStringHandlerFirstParam(Handler<String> handler, String other) {
    handler.handle(other);
  }

  public static void invokeStringHandlerLastParam(String other, Handler<String> handler) {
    handler.handle(other);
  }

  public static void invokeAsyncResultHandlerSuccess(Handler<AsyncResult<String>> callback) {
    callback.handle(Future.succeededFuture("hello"));
  }

  public static void invokeAsyncResultHandlerFailure(Handler<AsyncResult<String>> callback) {
    callback.handle(Future.failedFuture("oh no"));
  }

}
