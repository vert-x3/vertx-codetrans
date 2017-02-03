package io.vertx.support;

import io.vertx.codegen.annotations.VertxGen;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface TypeParameterBound extends Parameterized<String> {

  static TypeParameterBound create(String s) {
    return new TypeParameterBound() {

      String val = s;

      @Override
      public String get() {
        return val;
      }

      @Override
      public void set(String s) {
        val = s;
      }
    };
  }
}
