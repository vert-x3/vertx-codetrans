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
    throw new UnsupportedOperationException();
  }

  static boolean blue() {
    throw new UnsupportedOperationException();
  }
}
