package io.vertx.kotlin.support;

import io.vertx.codetrans.MethodExpressionTest;
import io.vertx.core.Handler;

/**
 * Created by Sergey Mashkov
 */
public class SubHandler implements Handler<String> {

  public static void classHandler(Handler<String> handler) {
    handler.handle("hello_class");
  }

  public static SubHandler create() {
    return new SubHandler();
  }

  public void handle(String event) {
    MethodExpressionTest.event = event;
  }

  public void instanceHandler(Handler<String> handler) {
    handler.handle("hello_instance");
  }
}
