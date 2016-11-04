package io.vertx.groovy.support;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import io.vertx.support.JsonConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class GroovyStaticExtension {

  public static JsonObject toJsonObject(JsonConverter converter, Map<String, Object> obj) {
    return new JsonObject(obj);
  }

  public static JsonArray toJsonArray(JsonConverter converter, List<Object> arr) {
    return new JsonArray(arr);
  }

  public static Map<String, Object> fromJsonObject(JsonConverter converter, JsonObject obj) {
    Map<String, Object> m = new HashMap<>();
    obj.forEach(entry -> {
      if (entry.getValue() instanceof JsonObject) {
        m.put(entry.getKey(), fromJsonObject(converter, (JsonObject) entry.getValue()));
      } else if (entry.getValue() instanceof JsonArray) {
        m.put(entry.getKey(), fromJsonArray(converter, (JsonArray) entry.getValue()));
      } else {
        m.put(entry.getKey(), entry.getValue());
      }
    });
    return m;
  }

  public static List<Object> fromJsonArray(JsonConverter converter, JsonArray arr) {
    List<Object> m = new ArrayList<>();
    arr.forEach( value -> {
      if (value instanceof JsonObject) {
        m.add(fromJsonObject(converter, (JsonObject) value));
      } else if (value instanceof JsonArray) {
        m.add(fromJsonArray(converter, (JsonArray) value));
      } else {
        m.add(value);
      }
    });
    return m;
  }
}
