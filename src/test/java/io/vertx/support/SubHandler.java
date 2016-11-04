package io.vertx.support;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.codetrans.MethodExpressionTest;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface SubHandler extends Handler<String> {

  static void classHandler(Handler<String> handler) {
    handler.handle("hello_class");
  }

  static SubHandler create() {
    return event -> {
      MethodExpressionTest.event = event;
    };
  }

  default void instanceHandler(Handler<String> handler) {
    handler.handle("hello_instance");
  }
}
