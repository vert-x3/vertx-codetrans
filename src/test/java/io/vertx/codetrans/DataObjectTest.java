package io.vertx.codetrans;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.support.ServerOptions;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class DataObjectTest extends ConversionTestBase {

  public static Object o;

  @Test
  public void testEmpty() throws Exception {
    JsonObject expected = new JsonObject();
    o = null;
    runJavaScript("dataobject/DataObject", "empty");
    Assert.assertEquals(expected, unwrapJsonObject((ScriptObjectMirror) o));
    o = null;
    runGroovy("dataobject/DataObject", "empty");
    Assert.assertEquals(expected, unwrapJsonObject((Map<String, Object>) o));
    o = null;
    runRuby("dataobject/DataObject", "empty");
    Assert.assertEquals(expected, unwrapJsonObject((Map<String, Object>) o));
//    o = null;
//    runScala("dataobject/DataObject", "empty");
//    Assert.assertEquals(new JsonObject(), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testSetFromConstructor() throws Exception {
    JsonObject expected = new JsonObject().put("host", "localhost").put("port", 8080);
    o = null;
    runJavaScript("dataobject/DataObject", "setFromConstructor");
    Assert.assertEquals(expected, unwrapJsonObject((ScriptObjectMirror) o));
    o = null;
    runGroovy("dataobject/DataObject", "setFromConstructor");
    Assert.assertEquals(expected, unwrapJsonObject((Map<String, Object>) o));
    o = null;
    runRuby("dataobject/DataObject", "setFromConstructor");
    Assert.assertEquals(expected, unwrapJsonObject((Map<String, Object>) o));
//    o = null;
//    runScala("dataobject/DataObject", "setFromConstructor");
//    Assert.assertEquals(new JsonObject().put("host", "localhost").put("port", 8080), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testSetFromIdentifier() throws Exception {
    JsonObject expected = new JsonObject().put("host", "localhost").put("port", 8080);
    o = null;
    runJavaScript("dataobject/DataObject", "setFromIdentifier");
    Assert.assertEquals(expected, unwrapJsonObject((ScriptObjectMirror) o));
    o = null;
    runGroovy("dataobject/DataObject", "setFromIdentifier");
    Assert.assertEquals(expected, unwrapJsonObject((Map<String, Object>) o));
    o = null;
    runRuby("dataobject/DataObject", "setFromIdentifier");
    Assert.assertEquals(expected, unwrapJsonObject((Map<String, Object>) o));
    o = null;
//    runScala("dataobject/DataObject", "setFromIdentifier");
//    Assert.assertEquals(new JsonObject().put("host", "localhost").put("port", 8080), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testgetFromIdentifier() throws Exception {
    String expected = "localhost";
    o = null;
    runJavaScript("dataobject/DataObject", "getFromIdentifier");
    Assert.assertEquals(expected, o);
    o = null;
    runGroovy("dataobject/DataObject", "getFromIdentifier");
    Assert.assertEquals(expected, o);
    o = null;
    runRuby("dataobject/DataObject", "getFromIdentifier");
    Assert.assertEquals(expected, o);
    o = null;
//    runScala("dataobject/DataObject", "getFromIdentifier");
//    Assert.assertEquals("localhost", o);
  }

  @Test
  public void testNested() throws Exception {
    JsonObject expected = new JsonObject().put("keyStore", new JsonObject().put("path", "/mystore.jks").put("password", "secret"));
    o = null;
    runJavaScript("dataobject/DataObject", "nested");
    Assert.assertEquals(expected, unwrapJsonObject((ScriptObjectMirror) o));
    o = null;
    runGroovy("dataobject/DataObject", "nested");
    Assert.assertEquals(expected, unwrapJsonObject((Map<String, Object>) o));
    o = null;
    runRuby("dataobject/DataObject", "nested");
    Assert.assertEquals(expected, unwrapJsonObject((Map<String, Object>) o));
    o = null;
//    runScala("dataobject/DataObject", "nested");
//    Assert.assertEquals(new JsonObject().put("keyStoreOptions", new JsonObject().put("path", "/mystore.jks").put("password", "secret")), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testAddToList() throws Exception {
    HashSet<String> expected = new HashSet<>();
    expected.add("foo");
    expected.add("bar");
    o = null;
    runJavaScript("dataobject/DataObject", "addToList");
    HttpServerOptions actual = new HttpServerOptions(unwrapJsonObject((ScriptObjectMirror) o));
    Assert.assertEquals(expected, actual.getEnabledCipherSuites());
    o = null;
    runGroovy("dataobject/DataObject", "addToList");
    actual = new HttpServerOptions(unwrapJsonObject((Map<String, Object>) o));
    Assert.assertEquals(expected, actual.getEnabledCipherSuites());
    o = null;
    runRuby("dataobject/DataObject", "addToList");
    actual = new HttpServerOptions(unwrapJsonObject((Map<String, Object>) o));
    Assert.assertEquals(expected, actual.getEnabledCipherSuites());
    o = null;
//    runScala("dataobject/DataObject", "addToList");
//    actual = new HttpServerOptions(unwrapJsonObject((Map<String, Object>) o));
//    expected = new HashSet<>();
//    expected.add("foo");
//    expected.add("bar");
//    Assert.assertEquals(expected, actual.getEnabledCipherSuites());
  }

  @Test
  public void testAddToMap() throws Exception {
    HashSet<String> expectedKeys = new HashSet<>();
    expectedKeys.add("foo");
    expectedKeys.add("bar");
    Consumer<DeliveryOptions> check = actual -> {
      Assert.assertEquals(expectedKeys, actual.getHeaders().names());
      Assert.assertEquals("foo_value", actual.getHeaders().get("foo"));
      Assert.assertEquals("bar_value", actual.getHeaders().get("bar"));
    };
    o = null;
    runJavaScript("dataobject/DataObject", "addToMap");
    DeliveryOptions actual = new DeliveryOptions(unwrapJsonObject((ScriptObjectMirror) o));
    check.accept(actual);
    o = null;
    runGroovy("dataobject/DataObject", "addToMap");
    actual = new DeliveryOptions(unwrapJsonObject((Map<String, Object>) o));
    check.accept(actual);
    o = null;
    runRuby("dataobject/DataObject", "addToMap");
    actual = new DeliveryOptions(unwrapJsonObject((Map<String, Object>) o));
    check.accept(actual);
//    o = null;
//    runScala("dataobject/DataObject", "addToMap");
//    actual = new DeliveryOptions(unwrapJsonObject((Map<String, Object>) o));
//    Assert.assertEquals(expectedKeys, actual.getHeaders().names());
//    Assert.assertEquals("foo_value", actual.getHeaders().get("foo"));
//    Assert.assertEquals("bar_value", actual.getHeaders().get("bar"));
  }

  @Test
  public void testEnumValueFromIdentifier() throws Exception {
    JsonObject expected = new JsonObject().put("protocolVersion", "HTTP_2");
    o = null;
    runJavaScript("dataobject/DataObject", "enumValueFromIdentifier");
    Assert.assertEquals(expected, unwrapJsonObject((ScriptObjectMirror) o));
    o = null;
    runGroovy("dataobject/DataObject", "enumValueFromIdentifier");
    Assert.assertEquals(expected, unwrapJsonObject((Map<String, Object>) o));
    o = null;
    runRuby("dataobject/DataObject", "enumValueFromIdentifier");
    Assert.assertEquals(expected, unwrapJsonObject((Map<String, Object>) o));
//    o = null;
//    runScala("dataobject/DataObject", "enumValueFromIdentifier");
//    Assert.assertEquals(new JsonObject().put("protocolVersion", "HTTP_2"), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testEnumValueFromConstructor() throws Exception {
    JsonObject expected = new JsonObject().put("protocolVersion", "HTTP_2");
    o = null;
    runJavaScript("dataobject/DataObject", "enumValueFromConstructor");
    Assert.assertEquals(expected, unwrapJsonObject((ScriptObjectMirror) o));
    o = null;
    runGroovy("dataobject/DataObject", "enumValueFromConstructor");
    Assert.assertEquals(expected, unwrapJsonObject((Map<String, Object>) o));
    o = null;
    runRuby("dataobject/DataObject", "enumValueFromConstructor");
    Assert.assertEquals(expected, unwrapJsonObject((Map<String, Object>) o));
//    o = null;
//    runScala("dataobject/DataObject", "enumValueFromConstructor");
//    Assert.assertEquals(new JsonObject().put("protocolVersion", "HTTP_2"), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testToJson() throws Exception {
    JsonObject expected = new JsonObject()
      .put("protocolVersion", "HTTP_2")
      .put("port", 8080)
      .put("keyStore", new JsonObject().put("path", "/keystore").put("password", "r00t"));
    o = null;
    runJavaScript("dataobject/DataObject", "toJson");
    Assert.assertEquals(new JsonObject().put("result", expected), unwrapJsonObject((ScriptObjectMirror) o));
    o = null;
    runGroovy("dataobject/DataObject", "toJson");
    Assert.assertEquals(new JsonObject().put("result", expected), unwrapJsonObject((Map<String, Object>) o));
    o = null;
    runRuby("dataobject/DataObject", "toJson");
    Assert.assertEquals(new JsonObject().put("result", expected), unwrapJsonObject((Map<String, Object>) o));
    o = null;
//    runScala("dataobject/DataObject", "toJson");
//    Assert.assertEquals(expected, unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testLiteralJsonObjectConstructor() throws Exception {
    JsonObject expected = new JsonObject().put("host", "localhost").put("port", 8080);
    o = null;
    runJavaScript("dataobject/DataObject", "literalJsonObjectConstructor");
    Assert.assertEquals(expected, unwrapJsonObject((ScriptObjectMirror) o));
    o = null;
    runGroovy("dataobject/DataObject", "literalJsonObjectConstructor");
    Assert.assertEquals(expected, unwrapJsonObject((Map<String, Object>) o));
    o = null;
    runRuby("dataobject/DataObject", "literalJsonObjectConstructor");
    Assert.assertEquals(expected, unwrapJsonObject((Map<String, Object>) o));
    o = null;
  }

  @Test
  public void testJsonObjectConstructor() throws Exception {
    JsonObject expected = new JsonObject().put("host", "localhost").put("port", 8080);
    o = null;
    runJavaScript("dataobject/DataObject", "jsonObjectConstructor");
    Assert.assertEquals(expected, unwrapJsonObject((ScriptObjectMirror) o));
    o = null;
    runGroovy("dataobject/DataObject", "jsonObjectConstructor");
    Assert.assertEquals(expected, unwrapJsonObject((Map<String, Object>) o));
    o = null;
    runRuby("dataobject/DataObject", "jsonObjectConstructor");
    Assert.assertEquals(expected, unwrapJsonObject((Map<String, Object>) o));
    o = null;
    runKotlin("dataobject/DataObject", "jsonObjectConstructor");
    Assert.assertEquals(new ServerOptions(expected), o);
    /*
    JsonObject expected = new JsonObject().put("host", "localhost").put("port", 8080);
    o = null;
    runJavaScript("dataobject/DataObject", "literalJsonObjectConstructor");
    Assert.assertEquals(expected, unwrapJsonObject((ScriptObjectMirror) o));
    o = null;
    runGroovy("dataobject/DataObject", "literalJsonObjectConstructor");
    Assert.assertEquals(expected, unwrapJsonObject((Map<String, Object>) o));
    o = null;
    runRuby("dataobject/DataObject", "literalJsonObjectConstructor");
    Assert.assertEquals(expected, unwrapJsonObject((Map<String, Object>) o));
    o = null;
    */
  }
}
