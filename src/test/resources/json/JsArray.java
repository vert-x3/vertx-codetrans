package json;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.JsonTest;
import io.vertx.support.JsonConverter;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsArray {

  @CodeTranslate
  public void instantiate() throws Exception {
    JsonArray arr = new JsonArray();
    JsonTest.o = JsonConverter.toJsonArray(arr);
  }

  @CodeTranslate
  public void addArrayFromConstructor() throws Exception {
    JsonArray arr = new JsonArray().add(new JsonArray().add("foo"));
    JsonTest.o = JsonConverter.toJsonArray(arr);
  }

  @CodeTranslate
  public void addBooleanFromConstructor() throws Exception {
    JsonArray arr = new JsonArray().add(true).add(false);
    JsonTest.o = JsonConverter.toJsonArray(arr);
  }

  @CodeTranslate
  public void addNumberFromConstructor() throws Exception {
    JsonArray arr = new JsonArray().add(8080);
    JsonTest.o = JsonConverter.toJsonArray(arr);
  }

  @CodeTranslate
  public void addObjectFromConstructor() throws Exception {
    JsonArray arr = new JsonArray().add(new JsonObject().put("foo", "foo_value"));
    JsonTest.o = JsonConverter.toJsonArray(arr);
  }

  @CodeTranslate
  public void addStringFromConstructor() throws Exception {
    JsonArray arr = new JsonArray().add("foo");
    JsonTest.o = JsonConverter.toJsonArray(arr);
  }

  @CodeTranslate
  public void getString() throws Exception {
    JsonArray arr = JsonTest.array;
    arr = JsonConverter.fromJsonArray(arr);
    JsonTest.o = arr.getString(0);
  }

  @CodeTranslate
  public void getBoolean() throws Exception {
    JsonArray arr = JsonTest.array;
    arr = JsonConverter.fromJsonArray(arr);
    JsonTest.o = arr.getBoolean(0);
  }

  @CodeTranslate
  public void getDouble() throws Exception {
    JsonArray arr = JsonTest.array;
    arr = JsonConverter.fromJsonArray(arr);
    JsonTest.o = arr.getDouble(0);
  }

  @CodeTranslate
  public void getFloat() throws Exception {
    JsonArray arr = JsonTest.array;
    arr = JsonConverter.fromJsonArray(arr);
    JsonTest.o = arr.getFloat(0);
  }

  @CodeTranslate
  public void getInteger() throws Exception {
    JsonArray arr = JsonTest.array;
    arr = JsonConverter.fromJsonArray(arr);
    JsonTest.o = arr.getInteger(0);
  }

  @CodeTranslate
  public void getLong() throws Exception {
    JsonArray arr = JsonTest.array;
    arr = JsonConverter.fromJsonArray(arr);
    JsonTest.o = arr.getLong(0);
  }

  @CodeTranslate
  public void getArray() throws Exception {
    JsonArray arr = JsonTest.array;
    arr = JsonConverter.fromJsonArray(arr);
    JsonTest.o = arr.getJsonArray(0).getInteger(1);
  }

  @CodeTranslate
  public void getObject() throws Exception {
    JsonArray arr = JsonTest.array;
    arr = JsonConverter.fromJsonArray(arr);
    JsonTest.o = arr.getJsonObject(0).getString("foo");
  }

  @CodeTranslate
  public void addBoolean() throws Exception {
    JsonArray arr = new JsonArray();
    arr.add(true);
    JsonTest.o = JsonConverter.toJsonArray(arr);
  }

  @CodeTranslate
  public void addNumber() throws Exception {
    JsonArray arr = new JsonArray();
    arr.add(8080);
    JsonTest.o = JsonConverter.toJsonArray(arr);
  }

  @CodeTranslate
  public void addObject() throws Exception {
    JsonArray arr = new JsonArray();
    arr.add(new JsonObject().put("foo", "foo_value"));
    JsonTest.o = JsonConverter.toJsonArray(arr);
  }

  @CodeTranslate
  public void addString() throws Exception {
    JsonArray arr = new JsonArray();
    arr.add("the_string");
    JsonTest.o = JsonConverter.toJsonArray(arr);
  }

  @CodeTranslate
  public void addArray() throws Exception {
    JsonArray arr = new JsonArray();
    arr.add(new JsonArray().add("the_array"));
    JsonTest.o = JsonConverter.toJsonArray(arr);
  }

  @CodeTranslate
  public void encodePrettily() throws Exception {
    JsonArray arr = new JsonArray().add("foo").add("bar");
    JsonTest.o = arr.encodePrettily();
  }

  @CodeTranslate
  public void encode() throws Exception {
    JsonArray arr = new JsonArray().add("foo").add("bar");
    JsonTest.o = arr.encode();
  }

  @CodeTranslate
  public void addNull() throws Exception {
    JsonArray arr = new JsonArray();
    arr.addNull();
    JsonTest.o = JsonConverter.toJsonArray(arr);
  }

  @CodeTranslate
  public void size() throws Exception {
    JsonArray arr = new JsonArray().add(0).add(1).add(2);
    JsonTest.o = arr.size();
  }
}
