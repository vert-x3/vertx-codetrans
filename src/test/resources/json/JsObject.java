package json;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.JsonTest;
import io.vertx.support.JsonConverter;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsObject {

  @CodeTranslate
  public void instantiate() throws Exception {
    JsonObject obj = new JsonObject();
    JsonTest.o = JsonConverter.toJsonObject(obj);
  }

  @CodeTranslate
  public void putArrayFromConstructor() throws Exception {
    JsonObject obj = new JsonObject().put("nested", new JsonArray().add("foo"));
    JsonTest.o = JsonConverter.toJsonObject(obj);
  }

  @CodeTranslate
  public void putArrayFromIdentifier() throws Exception {
    JsonObject obj = new JsonObject();
    obj.put("nested", new JsonArray().add("foo"));
    JsonTest.o = JsonConverter.toJsonObject(obj);
  }

  @CodeTranslate
  public void getArrayFromIdentifier() throws Exception {
    JsonObject obj = new JsonObject().put("nested", new JsonArray().add("foo"));
    JsonArray arr = obj.getJsonArray("nested");
    JsonTest.o = JsonConverter.toJsonArray(arr);
  }

  @CodeTranslate
  public void putBooleanFromConstructor() throws Exception {
    JsonObject obj = new JsonObject().put("_true", true).put("_false", false);
    JsonTest.o = JsonConverter.toJsonObject(obj);
  }

  @CodeTranslate
  public void putBooleanFromIdentifier() throws Exception {
    JsonObject obj = new JsonObject();
    obj.put("_true", true);
    obj.put("_false", false);
    JsonTest.o = JsonConverter.toJsonObject(obj);
  }

  @CodeTranslate
  public void getBooleanFromIdentifier() throws Exception {
    JsonObject obj = new JsonObject().put("_true", true);
    JsonTest.o = obj.getBoolean("_true");
  }

  @CodeTranslate
  public void putNumberFromConstructor() throws Exception {
    JsonObject obj = new JsonObject().put("port", 8080);
    JsonTest.o = JsonConverter.toJsonObject(obj);
  }

  @CodeTranslate
  public void putNumberFromIdentifier() throws Exception {
    JsonObject obj = new JsonObject();
    obj.put("port", 8080);
    JsonTest.o = JsonConverter.toJsonObject(obj);
  }

  @CodeTranslate
  public void getIntegerFromIdentifier() throws Exception {
    JsonObject obj = new JsonObject().put("port", 8080);
    JsonTest.o = obj.getInteger("port");
  }

  @CodeTranslate
  public void getLongFromIdentifier() throws Exception {
    JsonObject obj = new JsonObject().put("port", 8080l);
    JsonTest.o = obj.getLong("port");
  }

  @CodeTranslate
  public void getFloatFromIdentifier() throws Exception {
    JsonObject obj = new JsonObject().put("port", 8080f);
    JsonTest.o = obj.getFloat("port");
  }

  @CodeTranslate
  public void getDoubleFromIdentifier() throws Exception {
    JsonObject obj = new JsonObject().put("port", 8080d);
    JsonTest.o = obj.getDouble("port");
  }

  @CodeTranslate
  public void putObjectFromConstructor() throws Exception {
    JsonObject obj = new JsonObject().put("nested", new JsonObject().put("foo", "bar"));
    JsonTest.o = JsonConverter.toJsonObject(obj);
  }

  @CodeTranslate
  public void putObjectFromIdentifier() throws Exception {
    JsonObject obj = new JsonObject();
    obj.put("nested", new JsonObject().put("foo", "bar"));
    JsonTest.o = JsonConverter.toJsonObject(obj);
  }

  @CodeTranslate
  public void getObjectFromIdentifier() throws Exception {
    JsonObject obj = new JsonObject().put("nested", new JsonObject().put("foo", "bar"));
    JsonObject nested = obj.getJsonObject("nested");
    JsonTest.o = JsonConverter.toJsonObject(nested);
  }

  @CodeTranslate
  public void putStringFromConstructor() throws Exception {
    JsonObject obj = new JsonObject().put("foo", "foo_value");
    JsonTest.o = JsonConverter.toJsonObject(obj);
  }

  @CodeTranslate
  public void putStringFromIdentifier() throws Exception {
    JsonObject obj = new JsonObject();
    obj.put("foo", "foo_value");
    JsonTest.o = JsonConverter.toJsonObject(obj);
  }

  @CodeTranslate
  public void getStringFromIdentifier() throws Exception {
    JsonObject obj = new JsonObject().put("foo", "foo_value");
    JsonTest.o = obj.getString("foo");
  }

  @CodeTranslate
  public void getValueFromIdentifier() throws Exception {
    JsonObject obj = new JsonObject().put("foo", "foo_value");
    JsonTest.o = obj.getValue("foo");
  }

  @CodeTranslate
  public void encodePrettily() throws Exception {
    JsonObject obj = new JsonObject().put("foo", "foo_value");
    JsonTest.o = obj.encodePrettily();
  }

  @CodeTranslate
  public void encode() throws Exception {
    JsonObject obj = new JsonObject().put("foo", "foo_value");
    JsonTest.o = obj.encode();
  }

  @CodeTranslate
  public void getJsonObject() {
    JsonObject obj = JsonTest.object;
    obj = JsonConverter.fromJsonObject(obj);
    JsonTest.o = obj.getJsonObject("foo").encodePrettily();
  }

  @CodeTranslate
  public void getJsonArray() {
    JsonObject obj = JsonTest.object;
    obj = JsonConverter.fromJsonObject(obj);
    JsonTest.o = obj.getJsonArray("foo").encodePrettily();
  }
}
