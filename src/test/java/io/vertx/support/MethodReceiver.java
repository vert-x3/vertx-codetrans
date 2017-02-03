package io.vertx.support;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;

import java.util.List;

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

  static <T> Object parameterizedMethodMatchingTypeVariableParameter(T arg) {
    return arg == null ? (T)"hello" : arg;
  }

  static <T> Object parameterizedMethodMatchingGenericParameter(Parameterized<T> arg) {
    return arg == null ? (T)"hello" : arg.get();
  }
}
