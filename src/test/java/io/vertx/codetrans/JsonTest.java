package io.vertx.codetrans;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsonTest extends ConversionTestBase {

  public static Object o;

  @Test
  public void testJsonObjectInstantiate() {
    runAll("json/JsObject", "instantiate", () -> Assert.assertEquals(new JsonObject(), o));
  }

  @Test
  public void testJsonObjectPutStringFromConstructor() {
    runAll("json/JsObject", "putStringFromConstructor", () -> {
      Assert.assertEquals(new JsonObject().put("foo", "foo_value"), o);
    });
  }

  @Test
  public void testJsonObjectPutStringFromIdentifier() {
    runAll("json/JsObject", "putStringFromIdentifier", () -> {
      Assert.assertEquals(new JsonObject().put("foo", "foo_value"), o);
    });
  }

  @Test
  public void testJsonObjectGetStringFromIdentifier() {
    runAll("json/JsObject", "getStringFromIdentifier", () -> {
        Assert.assertEquals("foo_value", o);
    });
  }

  @Test
  public void testJsObjectPutBooleanFromConstructor() {
    runAll("json/JsObject", "putBooleanFromConstructor", () -> {
      Assert.assertEquals(new JsonObject().put("_true", true).put("_false", false), o);
    });
  }

  @Test
  public void testJsObjectPutBooleanFromIdentifier() {
    runAll("json/JsObject", "putBooleanFromIdentifier", () -> {
      Assert.assertEquals(new JsonObject().put("_true", true).put("_false", false), o);
    });
  }

  @Test
  public void testJsonObjectGetBooleanFromIdentifier() {
    runAll("json/JsObject", "getBooleanFromIdentifier", () -> {
      Assert.assertEquals(true, o);
    });
  }

  @Test
  public void testJsonObjectPutNumberFromConstructor() {
    runAll("json/JsObject", "putNumberFromConstructor", () -> {
      Assert.assertEquals(new JsonObject().put("port", 8080), o);
    });
  }

  @Test
  public void testJsonObjectPutNumberFromIdentifier() {
    runAll("json/JsObject", "putNumberFromIdentifier", () -> {
      Assert.assertEquals(new JsonObject().put("port", 8080), o);
    });
  }

  @Test
  public void testJsonObjectGetNumberFromIdentifier() {
    runAll("json/JsObject", "getIntegerFromIdentifier", () -> {
      Assert.assertEquals(8080, ((Number)o).intValue());
    });
    runAll("json/JsObject", "getLongFromIdentifier", () -> {
      Assert.assertEquals(8080, ((Number) o).longValue());
    });
    runAll("json/JsObject", "getFloatFromIdentifier", () -> {
      Assert.assertEquals(8080f, ((Number)o).floatValue(), 0.1);
    });
    Assert.assertEquals(8080d, o);
    runAll("json/JsObject", "getDoubleFromIdentifier", () -> {
      Assert.assertEquals(8080d, ((Number)o).doubleValue(), 0.1);
    });
  }

  @Test
  public void testJsonObjectPutObjectFromConstructor() {
    runAll("json/JsObject", "putObjectFromConstructor", () -> {
      Assert.assertEquals(new JsonObject().put("nested", new JsonObject().put("foo", "bar")), o);
    });
  }

  @Test
  public void testJsonObjectPutObjectFromIdentifier() {
    runAll("json/JsObject", "putObjectFromIdentifier", () -> {
      Assert.assertEquals(new JsonObject().put("nested", new JsonObject().put("foo", "bar")), o);
    });
  }

  @Test
  public void testJsonObjectGetObjectFromIdentifier() {
    runAll("json/JsObject", "getObjectFromIdentifier", () -> {
      Assert.assertEquals(new JsonObject().put("foo", "bar"), o);
    });
  }

  public static JsonObject object;

  @Test
  public void testGetJsonObject() {
    JsonObject expected = new JsonObject().put("bar", "juu");
    object = new JsonObject().put("foo", expected);
    runAll("json/JsObject", "getJsonObject", () -> {
      Assert.assertEquals(expected, new JsonObject((String) o));
    });
  }

  @Test
  public void testGetJsonArray() {
    JsonArray expected = new JsonArray().add(4).add(5).add(6);
    object = new JsonObject().put("foo", expected);
    runAll("json/JsObject", "getJsonArray", () -> {
      Assert.assertEquals(expected, new JsonArray((String) o));
    });
  }

  @Test
  public void testJsonObjectPutArrayFromConstructor() {
    runAll("json/JsObject", "putArrayFromConstructor", () -> {
      Assert.assertEquals(new JsonObject().put("nested", new JsonArray().add("foo")), o);
    });
  }

  @Test
  public void testJsonObjectPutArrayFromIdentifier() {
    runAll("json/JsObject", "putArrayFromIdentifier", () -> {
      Assert.assertEquals(new JsonObject().put("nested", new JsonArray().add("foo")), o);
    });
  }

  @Test
  public void testJsonObjectGetArrayFromIdentifier() {
    runAll("json/JsObject", "getArrayFromIdentifier", () -> {
      Assert.assertEquals(new JsonArray().add("foo"), o);
    });
  }

  @Test
  public void testJsonObjectGetValueFromIdentifier() {
    runAll("json/JsObject", "getValueFromIdentifier", () -> {
      Assert.assertEquals("foo_value", o);
    });
  }

  @Test
  public void testJsonObjectEncodePrettily() {
    runJavaScript("json/JsObject", "encodePrettily");
    Assert.assertEquals("{\"foo\":\"foo_value\"}", o);
    runGroovy("json/JsObject", "encodePrettily");
    Assert.assertEquals("{\"foo\":\"foo_value\"}", o);
    runRuby("json/JsObject", "encodePrettily");
    Assert.assertEquals("{\"foo\":\"foo_value\"}", o);
  }

  // **

  @Test
  public void testJsonArrayInstantiate() {
    runAll("json/JsArray", "instantiate", () -> {
      Assert.assertEquals(new JsonArray(), o);
    });
  }

  @Test
  public void testJsonArrayAddString() {
    runAll("json/JsArray", "addString", () -> {
      Assert.assertEquals(new JsonArray().add("foo"), o);
    });
  }

  @Test
  public void testJsonArrayAddBoolean() {
    runAll("json/JsArray", "addBoolean", () -> {
      Assert.assertEquals(new JsonArray().add(true).add(false), o);
    });
  }

  @Test
  public void testJsonArrayAddNumber() {
    runAll("json/JsArray", "addNumber", () -> {
      Assert.assertEquals(new JsonArray().add(8080), o);
    });
  }

  @Test
  public void testJsonArrayAddArray() {
    runAll("json/JsArray", "addArray", () -> {
      Assert.assertEquals(new JsonArray().add(new JsonArray().add("foo")), o);
    });
  }

  @Test
  public void testJsonArrayAddObject() {
    runAll("json/JsArray", "addObject", () -> {
      Assert.assertEquals(new JsonArray().add(new JsonObject().put("foo", "foo_value")), o);
    });
  }

  public static JsonArray array;

  @Test
  public void testJsonArrayGetString() {
    array = new JsonArray().add("string_value");
    runAll("json/JsArray", "getString", () -> {
      Assert.assertEquals("string_value", o);
    });
  }

  @Test
  public void testJsonArrayGetBoolean() {
    array = new JsonArray().add(true);
    runAll("json/JsArray", "getBoolean", () -> {
      Assert.assertEquals(true, o);
    });
  }

  @Test
  public void testJsonArrayGetDouble() {
    array = new JsonArray().add(0.5D);
    runAll("json/JsArray", "getDouble", () -> {
      Assert.assertEquals(0.5D, ((Number)o).doubleValue(), 0);
    });
  }

  @Test
  public void testJsonArrayGetFloat() {
    array = new JsonArray().add(0.5F);
    runAll("json/JsArray", "getFloat", () -> {
      Assert.assertEquals(0.5F, ((Number)o).floatValue(), 0);
    });
  }

  @Test
  public void testJsonArrayGetInteger() {
    array = new JsonArray().add(1234);
    runAll("json/JsArray", "getInteger", () -> {
      Assert.assertEquals(1234, ((Number) o).intValue());
    });
  }

  @Test
  public void testJsonArrayGetLong() {
    array = new JsonArray().add(12345L);
    runAll("json/JsArray", "getLong", () -> {
      Assert.assertEquals(12345L, ((Number) o).longValue());
    });
  }

  @Test
  public void testJsonArrayGetJsonObject() {
    JsonObject expected = new JsonObject().put("foo", "bar");
    array = new JsonArray().add(expected);
    runAll("json/JsArray", "getJsonObject", () -> {
      Assert.assertEquals(expected, new JsonObject((String) o));
    });
  }

  @Test
  public void testJsonArrayGetJsonArray() {
    JsonArray expected = new JsonArray().add(1).add(2).add(3);
    array = new JsonArray().add(expected);
    runAll("json/JsArray", "getJsonArray", () -> {
      Assert.assertEquals(expected, new JsonArray((String) o));
    });
  }
}
