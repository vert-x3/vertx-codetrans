package io.vertx.support;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface SubHandler extends Handler<String> {

  static void classHandler(Handler<String> handler) {
    throw new UnsupportedOperationException("stub");
  }

  static SubHandler create() {
    return event -> {
      throw new UnsupportedOperationException("stub");
    };
  }

  default void instanceHandler(Handler<String> handler) {
    throw new UnsupportedOperationException("stub");
  }
}
