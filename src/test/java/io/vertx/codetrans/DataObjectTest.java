package io.vertx.codetrans;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.ScriptObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class DataObjectTest extends ConversionTestBase {

  public static Object o;

  @Before
  public void before() {
    o = null;
  }

  @Test
  public void testEmpty() throws Exception {
    o = null;
    runJavaScript("dataobject/DataObject", "empty");
    Assert.assertEquals(new JsonObject(), unwrapJsonObject((ScriptObjectMirror) o));
    o = null;
    runGroovy("dataobject/DataObject", "empty");
    Assert.assertEquals(new JsonObject(), unwrapJsonObject((Map<String, Object>) o));
    o = null;
    runRuby("dataobject/DataObject", "empty");
    Assert.assertEquals(new JsonObject(), unwrapJsonObject((Map<String, Object>) o));
    o = null;
//    runScala("dataobject/DataObject", "empty");
//    Assert.assertEquals(new JsonObject(), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testSetFromConstructor() throws Exception {
    o = null;
    runJavaScript("dataobject/DataObject", "setFromConstructor");
    Assert.assertEquals(new JsonObject().put("host", "localhost").put("port", 8080), unwrapJsonObject((ScriptObjectMirror) o));
    o = null;
    runGroovy("dataobject/DataObject", "setFromConstructor");
    Assert.assertEquals(new JsonObject().put("host", "localhost").put("port", 8080), unwrapJsonObject((Map<String, Object>) o));
    o = null;
    runRuby("dataobject/DataObject", "setFromConstructor");
    Assert.assertEquals(new JsonObject().put("host", "localhost").put("port", 8080), unwrapJsonObject((Map<String, Object>) o));
    o = null;
//    runScala("dataobject/DataObject", "setFromConstructor");
//    Assert.assertEquals(new JsonObject().put("host", "localhost").put("port", 8080), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testSetFromIdentifier() throws Exception {
    o = null;
    runJavaScript("dataobject/DataObject", "setFromIdentifier");
    Assert.assertEquals(new JsonObject().put("host", "localhost").put("port", 8080), unwrapJsonObject((ScriptObjectMirror) o));
    o = null;
    runGroovy("dataobject/DataObject", "setFromIdentifier");
    Assert.assertEquals(new JsonObject().put("host", "localhost").put("port", 8080), unwrapJsonObject((Map<String, Object>) o));
    o = null;
    runRuby("dataobject/DataObject", "setFromIdentifier");
    Assert.assertEquals(new JsonObject().put("host", "localhost").put("port", 8080), unwrapJsonObject((Map<String, Object>) o));
    o = null;
//    runScala("dataobject/DataObject", "setFromIdentifier");
//    Assert.assertEquals(new JsonObject().put("host", "localhost").put("port", 8080), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testgetFromIdentifier() throws Exception {
    o = null;
    runJavaScript("dataobject/DataObject", "getFromIdentifier");
    Assert.assertEquals("localhost", o);
    o = null;
    runGroovy("dataobject/DataObject", "getFromIdentifier");
    Assert.assertEquals("localhost", o);
    o = null;
    runRuby("dataobject/DataObject", "getFromIdentifier");
    Assert.assertEquals("localhost", o);
    o = null;
//    runScala("dataobject/DataObject", "getFromIdentifier");
//    Assert.assertEquals("localhost", o);
  }

  @Test
  public void testNested() throws Exception {
    o = null;
    runJavaScript("dataobject/DataObject", "nested");
    Assert.assertEquals(new JsonObject().put("keyStoreOptions", new JsonObject().put("path", "/mystore.jks").put("password", "secret")), unwrapJsonObject((ScriptObjectMirror) o));
    o = null;
    runGroovy("dataobject/DataObject", "nested");
    Assert.assertEquals(new JsonObject().put("keyStoreOptions", new JsonObject().put("path", "/mystore.jks").put("password", "secret")), unwrapJsonObject((Map<String, Object>) o));
    o = null;
    runRuby("dataobject/DataObject", "nested");
    Assert.assertEquals(new JsonObject().put("keyStoreOptions", new JsonObject().put("path", "/mystore.jks").put("password", "secret")), unwrapJsonObject((Map<String, Object>) o));
    o = null;
//    runScala("dataobject/DataObject", "nested");
//    Assert.assertEquals(new JsonObject().put("keyStoreOptions", new JsonObject().put("path", "/mystore.jks").put("password", "secret")), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testAddToList() throws Exception {
    o = null;
    runJavaScript("dataobject/DataObject", "addToList");
    HttpServerOptions actual = new HttpServerOptions(unwrapJsonObject((ScriptObjectMirror) o));
    HashSet<String> expected = new HashSet<>();
    expected.add("foo");
    expected.add("bar");
    Assert.assertEquals(expected, actual.getEnabledCipherSuites());
    o = null;
    runGroovy("dataobject/DataObject", "addToList");
    actual = new HttpServerOptions(unwrapJsonObject((Map<String, Object>) o));
    expected = new HashSet<>();
    expected.add("foo");
    expected.add("bar");
    Assert.assertEquals(expected, actual.getEnabledCipherSuites());
    o = null;
    runRuby("dataobject/DataObject", "addToList");
    actual = new HttpServerOptions(unwrapJsonObject((Map<String, Object>) o));
    expected = new HashSet<>();
    expected.add("foo");
    expected.add("bar");
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
    o = null;
    runJavaScript("dataobject/DataObject", "addToMap");
    DeliveryOptions actual = new DeliveryOptions(unwrapJsonObject((ScriptObjectMirror) o));
    Assert.assertEquals(expectedKeys, actual.getHeaders().names());
    Assert.assertEquals("foo_value", actual.getHeaders().get("foo"));
    Assert.assertEquals("bar_value", actual.getHeaders().get("bar"));
    o = null;
    runGroovy("dataobject/DataObject", "addToMap");
    actual = new DeliveryOptions(unwrapJsonObject((Map<String, Object>) o));
    Assert.assertEquals(expectedKeys, actual.getHeaders().names());
    Assert.assertEquals("foo_value", actual.getHeaders().get("foo"));
    Assert.assertEquals("bar_value", actual.getHeaders().get("bar"));
    o = null;
    runRuby("dataobject/DataObject", "addToMap");
    actual = new DeliveryOptions(unwrapJsonObject((Map<String, Object>) o));
    Assert.assertEquals(expectedKeys, actual.getHeaders().names());
    Assert.assertEquals("foo_value", actual.getHeaders().get("foo"));
    Assert.assertEquals("bar_value", actual.getHeaders().get("bar"));
    o = null;
//    runScala("dataobject/DataObject", "addToMap");
//    actual = new DeliveryOptions(unwrapJsonObject((Map<String, Object>) o));
//    Assert.assertEquals(expectedKeys, actual.getHeaders().names());
//    Assert.assertEquals("foo_value", actual.getHeaders().get("foo"));
//    Assert.assertEquals("bar_value", actual.getHeaders().get("bar"));
  }

  @Test
  public void testEnumValueFromIdentifier() throws Exception {
    o = null;
    runJavaScript("dataobject/DataObject", "enumValueFromIdentifier");
    Assert.assertEquals(new JsonObject().put("protocolVersion", "HTTP_2"), unwrapJsonObject((ScriptObjectMirror) o));
    o = null;
    runGroovy("dataobject/DataObject", "enumValueFromIdentifier");
    Assert.assertEquals(new JsonObject().put("protocolVersion", "HTTP_2"), unwrapJsonObject((Map<String, Object>) o));
    o = null;
    runRuby("dataobject/DataObject", "enumValueFromIdentifier");
    Assert.assertEquals(new JsonObject().put("protocolVersion", "HTTP_2"), unwrapJsonObject((Map<String, Object>) o));
    o = null;
//    runScala("dataobject/DataObject", "enumValueFromIdentifier");
//    Assert.assertEquals(new JsonObject().put("protocolVersion", "HTTP_2"), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testEnumValueFromConstructor() throws Exception {
    o = null;
    runJavaScript("dataobject/DataObject", "enumValueFromConstructor");
    Assert.assertEquals(new JsonObject().put("protocolVersion", "HTTP_2"), unwrapJsonObject((ScriptObjectMirror) o));
    o = null;
    runGroovy("dataobject/DataObject", "enumValueFromConstructor");
    Assert.assertEquals(new JsonObject().put("protocolVersion", "HTTP_2"), unwrapJsonObject((Map<String, Object>) o));
    o = null;
    runRuby("dataobject/DataObject", "enumValueFromConstructor");
    Assert.assertEquals(new JsonObject().put("protocolVersion", "HTTP_2"), unwrapJsonObject((Map<String, Object>) o));
    o = null;
//    runScala("dataobject/DataObject", "enumValueFromConstructor");
//    Assert.assertEquals(new JsonObject().put("protocolVersion", "HTTP_2"), unwrapJsonObject((Map<String, Object>) o));
  }
}
