package io.vertx.groovy.support

import io.vertx.codetrans.MethodExpressionTest
import io.vertx.core.Handler

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class SubHandler implements Handler<String> {

  public static void classHandler(Handler<String> handler) {
    handler.handle("hello_class")
  }

  public static SubHandler create() {
    return new SubHandler();
  }

  public void handle(String event) {
    MethodExpressionTest.event = event;
  }

  public static void instanceHandler(Handler<String> handler) {
    handler.handle("hello_instance")
  }
}
