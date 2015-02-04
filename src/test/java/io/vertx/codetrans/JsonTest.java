package io.vertx.codetrans;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import jdk.nashorn.internal.runtime.ScriptObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsonTest extends ConversionTestBase {

  public static Object o;

  @Test
  public void testJsonObjectInstantiate() {
    runJavaScript("json/JsObject", "instantiate");
    Assert.assertEquals(new JsonObject(), unwrapJsonObject((ScriptObject) o));
    runGroovy("json/JsObject", "instantiate");
    Assert.assertEquals(new JsonObject(), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testJsonObjectPutStringFromConstructor() {
    runJavaScript("json/JsObject", "putStringFromConstructor");
    Assert.assertEquals(new JsonObject().put("foo", "foo_value"), unwrapJsonObject((ScriptObject) o));
    runGroovy("json/JsObject", "putStringFromConstructor");
    Assert.assertEquals(new JsonObject().put("foo", "foo_value"), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testJsonObjectPutStringFromIdentifier() {
    runJavaScript("json/JsObject", "putStringFromIdentifier");
    Assert.assertEquals(new JsonObject().put("foo", "foo_value"), unwrapJsonObject((ScriptObject) o));
    runGroovy("json/JsObject", "putStringFromIdentifier");
    Assert.assertEquals(new JsonObject().put("foo", "foo_value"), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testJsonObjectGetStringFromIdentifier() {
    runJavaScript("json/JsObject", "getStringFromIdentifier");
    Assert.assertEquals("foo_value", o);
    runGroovy("json/JsObject", "getStringFromIdentifier");
    Assert.assertEquals("foo_value", o);
  }

  @Test
  public void testJsObjectPutBooleanFromConstructor() {
    runJavaScript("json/JsObject", "putBooleanFromConstructor");
    Assert.assertEquals(new JsonObject().put("_true", true).put("_false", false), unwrapJsonObject((ScriptObject) o));
    runGroovy("json/JsObject", "putBooleanFromConstructor");
    Assert.assertEquals(new JsonObject().put("_true", true).put("_false", false), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testJsObjectPutBooleanFromIdentifier() {
    runJavaScript("json/JsObject", "putBooleanFromIdentifier");
    Assert.assertEquals(new JsonObject().put("_true", true).put("_false", false), unwrapJsonObject((ScriptObject) o));
    runGroovy("json/JsObject", "putBooleanFromIdentifier");
    Assert.assertEquals(new JsonObject().put("_true", true).put("_false", false), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testJsonObjectGetBooleanFromIdentifier() {
    runJavaScript("json/JsObject", "getBooleanFromIdentifier");
    Assert.assertEquals(true, o);
    runGroovy("json/JsObject", "getBooleanFromIdentifier");
    Assert.assertEquals(true, o);
  }

  @Test
  public void testJsonObjectPutNumberFromConstructor() {
    runJavaScript("json/JsObject", "putNumberFromConstructor");
    Assert.assertEquals(new JsonObject().put("port", 8080), unwrapJsonObject((ScriptObject) o));
    runGroovy("json/JsObject", "putNumberFromConstructor");
    Assert.assertEquals(new JsonObject().put("port", 8080), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testJsonObjectPutNumberFromIdentifier() {
    runJavaScript("json/JsObject", "putNumberFromIdentifier");
    Assert.assertEquals(new JsonObject().put("port", 8080), unwrapJsonObject((ScriptObject) o));
    runGroovy("json/JsObject", "putNumberFromIdentifier");
    Assert.assertEquals(new JsonObject().put("port", 8080), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testJsonObjectGetNumberFromIdentifier() {
    runJavaScript("json/JsObject", "getIntegerFromIdentifier");
    Assert.assertEquals(8080, o);
    runJavaScript("json/JsObject", "getLongFromIdentifier");
    Assert.assertEquals(8080, o);
    runJavaScript("json/JsObject", "getFloatFromIdentifier");
    Assert.assertEquals(8080d, o);
    runJavaScript("json/JsObject", "getDoubleFromIdentifier");
    Assert.assertEquals(8080d, o);
    runGroovy("json/JsObject", "getIntegerFromIdentifier");
    Assert.assertEquals(8080, o);
    runGroovy("json/JsObject", "getLongFromIdentifier");
    Assert.assertEquals(8080l, o);
    runGroovy("json/JsObject", "getFloatFromIdentifier");
    Assert.assertEquals(8080f, o);
    runGroovy("json/JsObject", "getDoubleFromIdentifier");
    Assert.assertEquals(8080d, o);
  }

  @Test
  public void testJsonObjectPutObjectFromConstructor() {
    runJavaScript("json/JsObject", "putObjectFromConstructor");
    Assert.assertEquals(new JsonObject().put("nested", new JsonObject().put("foo", "bar")), unwrapJsonObject((ScriptObject) o));
    runGroovy("json/JsObject", "putObjectFromConstructor");
    Assert.assertEquals(new JsonObject().put("nested", new JsonObject().put("foo", "bar")), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testJsonObjectPutObjectFromIdentifier() {
    runJavaScript("json/JsObject", "putObjectFromIdentifier");
    Assert.assertEquals(new JsonObject().put("nested", new JsonObject().put("foo", "bar")), unwrapJsonObject((ScriptObject) o));
    runGroovy("json/JsObject", "putObjectFromIdentifier");
    Assert.assertEquals(new JsonObject().put("nested", new JsonObject().put("foo", "bar")), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testJsonObjectGetObjectFromIdentifier() {
    runJavaScript("json/JsObject", "getObjectFromIdentifier");
    Assert.assertEquals(new JsonObject().put("foo", "bar"), unwrapJsonObject((ScriptObject) o));
    runGroovy("json/JsObject", "getObjectFromIdentifier");
    Assert.assertEquals(new JsonObject().put("foo", "bar"), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testJsonObjectPutArrayFromConstructor() {
    runJavaScript("json/JsObject", "putArrayFromConstructor");
    Assert.assertEquals(new JsonObject().put("nested", new JsonArray().add("foo")), unwrapJsonObject((ScriptObject) o));
    runGroovy("json/JsObject", "putArrayFromConstructor");
    Assert.assertEquals(new JsonObject().put("nested", new JsonArray().add("foo")), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testJsonObjectPutArrayFromIdentifier() {
    runJavaScript("json/JsObject", "putArrayFromIdentifier");
    Assert.assertEquals(new JsonObject().put("nested", new JsonArray().add("foo")), unwrapJsonObject((ScriptObject) o));
    runGroovy("json/JsObject", "putArrayFromIdentifier");
    Assert.assertEquals(new JsonObject().put("nested", new JsonArray().add("foo")), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testJsonObjectGetArrayFromIdentifier() {
    runJavaScript("json/JsObject", "getArrayFromIdentifier");
    Assert.assertEquals(new JsonArray().add("foo"), unwrapJsonArray((ScriptObject) o));
    runGroovy("json/JsObject", "getArrayFromIdentifier");
    Assert.assertEquals(new JsonArray().add("foo"), unwrapJsonArray((List<String>) o));
  }

  @Test
  public void testJsonObjectGetValueFromIdentifier() {
    runJavaScript("json/JsObject", "getValueFromIdentifier");
    Assert.assertEquals("foo_value", o);
    runGroovy("json/JsObject", "getValueFromIdentifier");
    Assert.assertEquals("foo_value", o);
  }

  @Test
  public void testJsonObjectEncodePrettily() {
    runJavaScript("json/JsObject", "encodePrettily");
    Assert.assertEquals("{\"foo\":\"foo_value\"}", o);
    runGroovy("json/JsObject", "encodePrettily");
    Assert.assertEquals("[foo:foo_value]", o);
  }

  // **

  @Test
  public void testJsonArrayInstantiate() {
    runJavaScript("json/JsArray", "instantiate");
    Assert.assertEquals(new JsonArray(), unwrapJsonArray((ScriptObject) o));
    runGroovy("json/JsArray", "instantiate");
    Assert.assertEquals(new JsonArray(), unwrapJsonArray((List<Object>) o));
  }

  @Test
  public void testJsonArrayAddString() {
    runJavaScript("json/JsArray", "addString");
    Assert.assertEquals(new JsonArray().add("foo"), unwrapJsonArray((ScriptObject) o));
    runGroovy("json/JsArray", "addString");
    Assert.assertEquals(new JsonArray().add("foo"), unwrapJsonArray((List<Object>) o));
  }

  @Test
  public void testJsonArrayAddBoolean() {
    runJavaScript("json/JsArray", "addBoolean");
    Assert.assertEquals(new JsonArray().add(true).add(false), unwrapJsonArray((ScriptObject) o));
    runGroovy("json/JsArray", "addBoolean");
    Assert.assertEquals(new JsonArray().add(true).add(false), unwrapJsonArray((List<Object>) o));
  }

  @Test
  public void testJsonArrayAddNumber() {
    runJavaScript("json/JsArray", "addNumber");
    Assert.assertEquals(new JsonArray().add(8080), unwrapJsonArray((ScriptObject) o));
    runGroovy("json/JsArray", "addNumber");
    Assert.assertEquals(new JsonArray().add(8080), unwrapJsonArray((List<Object>) o));
  }

  @Test
  public void testJsonArrayAddArray() {
    runJavaScript("json/JsArray", "addArray");
    Assert.assertEquals(new JsonArray().add(new JsonArray().add("foo")), unwrapJsonArray((ScriptObject) o));
    runGroovy("json/JsArray", "addArray");
    Assert.assertEquals(new JsonArray().add(new JsonArray().add("foo")), unwrapJsonArray((List<Object>) o));
  }

  @Test
  public void testJsonArrayAddObject() {
    runJavaScript("json/JsArray", "addObject");
    Assert.assertEquals(new JsonArray().add(new JsonObject().put("foo", "foo_value")), unwrapJsonArray((ScriptObject) o));
    runGroovy("json/JsArray", "addObject");
    Assert.assertEquals(new JsonArray().add(new JsonObject().put("foo", "foo_value")), unwrapJsonArray((List<Object>) o));
  }
}
