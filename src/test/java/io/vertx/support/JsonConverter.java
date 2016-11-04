package io.vertx.support;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface JsonConverter {

  static JsonObject toJsonObject(JsonObject obj) {
    return obj;
  }

  static JsonArray toJsonArray(JsonArray arr) {
    return arr;
  }

  static JsonObject fromJsonObject(JsonObject obj) {
    return obj;
  }

  static JsonArray fromJsonArray(JsonArray arr) {
    return arr;
  }
}
