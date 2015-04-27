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
    throw new AssertionError("stub");
  }

  static JsonArray toJsonArray(JsonArray arr) {
    throw new AssertionError("stub");
  }

  static JsonArray fromJsonArray(JsonArray arr) {
    throw new AssertionError("stub");
  }

}
