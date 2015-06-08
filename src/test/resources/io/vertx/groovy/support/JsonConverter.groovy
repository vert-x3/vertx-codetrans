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
    def m = [:]
    obj.forEach { entry ->
      if (entry.value instanceof JsonObject) {
        m[entry.key] = fromJsonObject(entry.value)
      } else if (entry.value instanceof JsonArray) {
        m[entry.key] = fromJsonArray(entry.value)
      } else {
        m[entry.key] = entry.value
      }
    }
    return m;
  }

  public static List<Object> fromJsonArray(JsonArray arr) {
    def m = [];
    arr.forEach { value ->
      if (value instanceof JsonObject) {
        m << fromJsonObject(value)
      } else if (value instanceof JsonArray) {
        m << fromJsonArray(value)
      } else {
        m << value
      }
    }
    return m;
  }
}
