package io.vertx.groovy.support

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class CollectionFactory {
  public static Map createMap() {
    HashMap map = new HashMap<>()
    map.put("foo", "foo_value")
    return map;
  }
}
