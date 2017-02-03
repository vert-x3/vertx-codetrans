package io.vertx.support;

import io.vertx.codegen.annotations.VertxGen;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Parameterized<T> {

  static <T> Parameterized<T> create(T t) {
    return new Parameterized<T>() {

      T val = t;

      @Override
      public T get() {
        return val;
      }

      @Override
      public void set(T t) {
        val = t;
      }
    };
  }

  T get();

  void set(T t);
}
