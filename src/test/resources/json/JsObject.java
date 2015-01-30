package json;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.JsonTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsObject {

  @CodeTranslate
  public void instantiate() throws Exception {
    JsonTest.o = new JsonObject();
  }

  @CodeTranslate
  public void putArrayFromConstructor() throws Exception {
    JsonTest.o = new JsonObject().put("nested", new JsonArray().add("foo"));
  }

  @CodeTranslate
  public void putArrayFromIdentifier() throws Exception {
    JsonObject obj = new JsonObject();
    obj.put("nested", new JsonArray().add("foo"));
    JsonTest.o = obj;
  }

  @CodeTranslate
  public void getArrayFromIdentifier() throws Exception {
    JsonObject obj = new JsonObject().put("nested", new JsonArray().add("foo"));
    JsonTest.o = obj.getDouble("nested");
  }

  @CodeTranslate
  public void putBooleanFromConstructor() throws Exception {
    JsonTest.o = new JsonObject().put("_true", true).put("_false", false);
  }

  @CodeTranslate
  public void putBooleanFromIdentifier() throws Exception {
    JsonObject obj = new JsonObject();
    obj.put("_true", true);
    obj.put("_false", false);
    JsonTest.o = obj;
  }

  @CodeTranslate
  public void getBooleanFromIdentifier() throws Exception {
    JsonObject obj = new JsonObject().put("_true", true);
    JsonTest.o = obj.getBoolean("_true");
  }

  @CodeTranslate
  public void putNumberFromConstructor() throws Exception {
    JsonTest.o = new JsonObject().put("port", 8080);
  }

  @CodeTranslate
  public void putNumberFromIdentifier() throws Exception {
    JsonObject obj = new JsonObject();
    obj.put("port", 8080);
    JsonTest.o = obj;
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
    JsonTest.o = new JsonObject().put("nested", new JsonObject().put("foo", "bar"));
  }

  @CodeTranslate
  public void putObjectFromIdentifier() throws Exception {
    JsonObject obj = new JsonObject();
    obj.put("nested", new JsonObject().put("foo", "bar"));
    JsonTest.o = obj;
  }

  @CodeTranslate
  public void getObjectFromIdentifier() throws Exception {
    JsonObject obj = new JsonObject().put("nested", new JsonObject().put("foo", "bar"));
    JsonTest.o = obj.getJsonObject("nested");
  }

  @CodeTranslate
  public void putStringFromConstructor() throws Exception {
    JsonTest.o = new JsonObject().put("foo", "foo_value");
  }

  @CodeTranslate
  public void putStringFromIdentifier() throws Exception {
    JsonObject obj = new JsonObject();
    obj.put("foo", "foo_value");
    JsonTest.o = obj;
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
}
