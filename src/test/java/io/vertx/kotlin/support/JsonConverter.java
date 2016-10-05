package io.vertx.kotlin.support;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by Sergey Mashkov
 */
public class JsonConverter {

  public static JsonObject toJsonObject(Object obj) {
    return (JsonObject) obj;
  }

  public static JsonArray toJsonArray(Object arr) {
    return (JsonArray) arr;
  }

  public static JsonObject fromJsonObject(JsonObject obj) {
    return obj;
  }

  public static JsonArray fromJsonArray(JsonArray arr) {
    return arr;
  }
}
