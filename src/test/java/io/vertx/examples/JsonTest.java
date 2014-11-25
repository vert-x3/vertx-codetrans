package io.vertx.examples;

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
  public void testNewJsonObjectJavaScript() {
    runJavaScript("json/NewJsonObject");
    Assert.assertEquals(new JsonObject(), unwrapJsonObject((ScriptObject) o));
  }

  @Test
  public void testNewJsonObjectGroovy() {
    runGroovy("json/NewJsonObject");
    Assert.assertEquals(new JsonObject(), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testJsonObjectPutStringJavaScript() {
    runJavaScript("json/JsonObjectPutString");
    Assert.assertEquals(new JsonObject().put("foo", "foo_value"), unwrapJsonObject((ScriptObject) o));
  }

  @Test
  public void testJsonObjectPutStringGroovy() {
    runGroovy("json/JsonObjectPutString");
    Assert.assertEquals(new JsonObject().put("foo", "foo_value"), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testJsonObjectPutBooleanJavaScript() {
    runJavaScript("json/JsonObjectPutBoolean");
    Assert.assertEquals(new JsonObject().put("_true", true).put("_false", false), unwrapJsonObject((ScriptObject) o));
  }

  @Test
  public void testJsonObjectPutBooleanGroovy() {
    runGroovy("json/JsonObjectPutBoolean");
    Assert.assertEquals(new JsonObject().put("_true", true).put("_false", false), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testJsonObjectPutNumberJavaScript() {
    runJavaScript("json/JsonObjectPutNumber");
    Assert.assertEquals(new JsonObject().put("port", 8080), unwrapJsonObject((ScriptObject) o));
  }

  @Test
  public void testJsonObjectPutNumberGroovy() {
    runGroovy("json/JsonObjectPutNumber");
    Assert.assertEquals(new JsonObject().put("port", 8080), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testJsonObjectPutObjectJavaScript() {
    runJavaScript("json/JsonObjectPutObject");
    Assert.assertEquals(new JsonObject().put("nested", new JsonObject().put("foo", "bar")), unwrapJsonObject((ScriptObject) o));
  }

  @Test
  public void testJsonObjectPutObjectGroovy() {
    runGroovy("json/JsonObjectPutObject");
    Assert.assertEquals(new JsonObject().put("nested", new JsonObject().put("foo", "bar")), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testJsonObjectPutArrayJavaScript() {
    runJavaScript("json/JsonObjectPutArray");
    Assert.assertEquals(new JsonObject().put("nested", new JsonArray().add("foo")), unwrapJsonObject((ScriptObject) o));
  }

  @Test
  public void testJsonObjectPutArrayGroovy() {
    runGroovy("json/JsonObjectPutArray");
    Assert.assertEquals(new JsonObject().put("nested", new JsonArray().add("foo")), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testNewJsonArrayJavaScript() {
    runJavaScript("json/NewJsonArray");
    Assert.assertEquals(new JsonArray(), unwrapJsonArray((ScriptObject) o));
  }

  @Test
  public void testNewJsonArrayGroovy() {
    runGroovy("json/NewJsonArray");
    Assert.assertEquals(new JsonArray(), unwrapJsonArray((List<Object>) o));
  }

  @Test
  public void testJsonArrayAddStringJavaScript() {
    runJavaScript("json/JsonArrayAddString");
    Assert.assertEquals(new JsonArray().add("foo"), unwrapJsonArray((ScriptObject) o));
  }

  @Test
  public void testsonArrayAddStringGroovy() {
    runGroovy("json/JsonArrayAddString");
    Assert.assertEquals(new JsonArray().add("foo"), unwrapJsonArray((List<Object>) o));
  }

  @Test
  public void testJsonArrayAddBooleanJavaScript() {
    runJavaScript("json/JsonArrayAddBoolean");
    Assert.assertEquals(new JsonArray().add(true).add(false), unwrapJsonArray((ScriptObject) o));
  }

  @Test
  public void testsonArrayAddBooleanGroovy() {
    runGroovy("json/JsonArrayAddBoolean");
    Assert.assertEquals(new JsonArray().add(true).add(false), unwrapJsonArray((List<Object>) o));
  }

  @Test
  public void testJsonArrayAddNumberJavaScript() {
    runJavaScript("json/JsonArrayAddNumber");
    Assert.assertEquals(new JsonArray().add(8080), unwrapJsonArray((ScriptObject) o));
  }

  @Test
  public void testsonArrayAddNumberGroovy() {
    runGroovy("json/JsonArrayAddNumber");
    Assert.assertEquals(new JsonArray().add(8080), unwrapJsonArray((List<Object>) o));
  }

  @Test
  public void testJsonArrayAddArrayJavaScript() {
    runJavaScript("json/JsonArrayAddArray");
    Assert.assertEquals(new JsonArray().add(new JsonArray().add("foo")), unwrapJsonArray((ScriptObject) o));
  }

  @Test
  public void testsonArrayAddArrayGroovy() {
    runGroovy("json/JsonArrayAddArray");
    Assert.assertEquals(new JsonArray().add(new JsonArray().add("foo")), unwrapJsonArray((List<Object>) o));
  }

  @Test
  public void testJsonArrayAddObjectJavaScript() {
    runJavaScript("json/JsonArrayAddObject");
    Assert.assertEquals(new JsonArray().add(new JsonObject().put("foo", "foo_value")), unwrapJsonArray((ScriptObject) o));
  }

  @Test
  public void testsonArrayAddObjectGroovy() {
    runGroovy("json/JsonArrayAddObject");
    Assert.assertEquals(new JsonArray().add(new JsonObject().put("foo", "foo_value")), unwrapJsonArray((List<Object>) o));
  }
}
