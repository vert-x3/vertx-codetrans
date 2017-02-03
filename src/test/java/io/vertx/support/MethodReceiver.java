package io.vertx.support;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface MethodReceiver {

  static boolean isRed() {
    return true;
  }

  static boolean blue() {
    return false;
  }

  static <T> void parameterizedMethod(Handler<T> handler) {
    handler.handle((T)"hello");
  }
}
