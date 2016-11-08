package io.vertx.kotlin.support;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Sergey Mashkov
 */
public class CollectionFactory {
  public static Map<String, String> createMap() {
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("foo", "foo_value");
    return map;
  }

  public static Map wrapMap(Map map) {
    return map;
  }
}
