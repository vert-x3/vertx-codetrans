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
  public void testJsObjectInstantiate() {
    runJavaScript("json/JsObject", "instantiate");
    Assert.assertEquals(new JsonObject(), unwrapJsonObject((ScriptObject) o));
    runGroovy("json/JsObject", "instantiate");
    Assert.assertEquals(new JsonObject(), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testJsObjectPutString() {
    runJavaScript("json/JsObject", "putString");
    Assert.assertEquals(new JsonObject().put("foo", "foo_value"), unwrapJsonObject((ScriptObject) o));
    runGroovy("json/JsObject", "putString");
    Assert.assertEquals(new JsonObject().put("foo", "foo_value"), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testJsObjectPutBoolean() {
    runJavaScript("json/JsObject", "putBoolean");
    Assert.assertEquals(new JsonObject().put("_true", true).put("_false", false), unwrapJsonObject((ScriptObject) o));
    runGroovy("json/JsObject", "putBoolean");
    Assert.assertEquals(new JsonObject().put("_true", true).put("_false", false), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testJsObjectPutNumber() {
    runJavaScript("json/JsObject", "putNumber");
    Assert.assertEquals(new JsonObject().put("port", 8080), unwrapJsonObject((ScriptObject) o));
    runGroovy("json/JsObject", "putNumber");
    Assert.assertEquals(new JsonObject().put("port", 8080), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testJsObjectPutObject() {
    runJavaScript("json/JsObject", "putObject");
    Assert.assertEquals(new JsonObject().put("nested", new JsonObject().put("foo", "bar")), unwrapJsonObject((ScriptObject) o));
    runGroovy("json/JsObject", "putObject");
    Assert.assertEquals(new JsonObject().put("nested", new JsonObject().put("foo", "bar")), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testJsObjectPutArray() {
    runJavaScript("json/JsObject", "putArray");
    Assert.assertEquals(new JsonObject().put("nested", new JsonArray().add("foo")), unwrapJsonObject((ScriptObject) o));
    runGroovy("json/JsObject", "putArray");
    Assert.assertEquals(new JsonObject().put("nested", new JsonArray().add("foo")), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testJsArrayInstantiate() {
    runJavaScript("json/JsArray", "instantiate");
    Assert.assertEquals(new JsonArray(), unwrapJsonArray((ScriptObject) o));
    runGroovy("json/JsArray", "instantiate");
    Assert.assertEquals(new JsonArray(), unwrapJsonArray((List<Object>) o));
  }

  @Test
  public void testJsArrayAddString() {
    runJavaScript("json/JsArray", "addString");
    Assert.assertEquals(new JsonArray().add("foo"), unwrapJsonArray((ScriptObject) o));
    runGroovy("json/JsArray", "addString");
    Assert.assertEquals(new JsonArray().add("foo"), unwrapJsonArray((List<Object>) o));
  }

  @Test
  public void testJsArrayAddBoolean() {
    runJavaScript("json/JsArray", "addBoolean");
    Assert.assertEquals(new JsonArray().add(true).add(false), unwrapJsonArray((ScriptObject) o));
    runGroovy("json/JsArray", "addBoolean");
    Assert.assertEquals(new JsonArray().add(true).add(false), unwrapJsonArray((List<Object>) o));
  }

  @Test
  public void testJsArrayAddNumber() {
    runJavaScript("json/JsArray", "addNumber");
    Assert.assertEquals(new JsonArray().add(8080), unwrapJsonArray((ScriptObject) o));
    runGroovy("json/JsArray", "addNumber");
    Assert.assertEquals(new JsonArray().add(8080), unwrapJsonArray((List<Object>) o));
  }

  @Test
  public void testJsArrayAddArray() {
    runJavaScript("json/JsArray", "addArray");
    Assert.assertEquals(new JsonArray().add(new JsonArray().add("foo")), unwrapJsonArray((ScriptObject) o));
    runGroovy("json/JsArray", "addArray");
    Assert.assertEquals(new JsonArray().add(new JsonArray().add("foo")), unwrapJsonArray((List<Object>) o));
  }

  @Test
  public void testJsArrayAddObject() {
    runJavaScript("json/JsArray", "addObject");
    Assert.assertEquals(new JsonArray().add(new JsonObject().put("foo", "foo_value")), unwrapJsonArray((ScriptObject) o));
    runGroovy("json/JsArray", "addObject");
    Assert.assertEquals(new JsonArray().add(new JsonObject().put("foo", "foo_value")), unwrapJsonArray((List<Object>) o));
  }
}
