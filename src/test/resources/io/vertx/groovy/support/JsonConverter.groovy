package io.vertx.groovy.support

import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class JsonConverter {

  public static JsonObject toJsonObject(Map<String, Object> obj) {
    return new JsonObject(obj);
  }

  public static JsonArray toJsonArray(List<Object> arr) {
    return new JsonArray(arr);
  }

  public static Map<String, Object> fromJsonObject(JsonObject obj) {
    return obj.map;
  }

  public static List<Object> fromJsonArray(JsonArray arr) {
    return arr.getList();
  }
}
