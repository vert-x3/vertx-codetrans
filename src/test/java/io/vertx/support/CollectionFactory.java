package io.vertx.support;

import io.vertx.codegen.annotations.VertxGen;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface CollectionFactory {

  static Map<String, String> createMap() {
    Map<String, String> map = new HashMap<>();
    map.put("foo", "foo_value");
    return map;
  }

  static Map<String, String> wrapMap(Map<String, String> map) {
    return map;
  }
}
