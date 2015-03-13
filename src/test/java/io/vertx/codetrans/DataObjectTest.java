package io.vertx.codetrans;

import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.ScriptObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class DataObjectTest extends ConversionTestBase {

  public static Object o;

  @Test
  public void testEmpty() throws Exception {
    o = null;
    runJavaScript("dataobject/DataObject", "empty");
    Assert.assertEquals(new JsonObject(), unwrapJsonObject((ScriptObjectMirror) o));
    o = null;
    runGroovy("dataobject/DataObject", "empty");
    Assert.assertEquals(new JsonObject(), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testSetFromConstructor() throws Exception {
    o = null;
    runJavaScript("dataobject/DataObject", "setFromConstructor");
    Assert.assertEquals(new JsonObject().put("host", "localhost").put("port", 8080), unwrapJsonObject((ScriptObjectMirror) o));
    o = null;
    runGroovy("dataobject/DataObject", "setFromConstructor");
    Assert.assertEquals(new JsonObject().put("host", "localhost").put("port", 8080), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testSetFromIdentifier() throws Exception {
    o = null;
    runJavaScript("dataobject/DataObject", "setFromIdentifier");
    Assert.assertEquals(new JsonObject().put("host", "localhost").put("port", 8080), unwrapJsonObject((ScriptObjectMirror) o));
    o = null;
    runGroovy("dataobject/DataObject", "setFromIdentifier");
    Assert.assertEquals(new JsonObject().put("host", "localhost").put("port", 8080), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testgetFromIdentifier() throws Exception {
    o = null;
    runJavaScript("dataobject/DataObject", "getFromIdentifier");
    Assert.assertEquals("localhost", o);
    o = null;
    runGroovy("dataobject/DataObject", "getFromIdentifier");
    Assert.assertEquals("localhost", o);
  }

  @Test
  public void testNested() throws Exception {
    o = null;
    runJavaScript("dataobject/DataObject", "nested");
    Assert.assertEquals(new JsonObject().put("keyStoreOptions", new JsonObject().put("path", "/mystore.jks").put("password", "secret")), unwrapJsonObject((ScriptObjectMirror) o));
    o = null;
    runGroovy("dataobject/DataObject", "nested");
    Assert.assertEquals(new JsonObject().put("keyStoreOptions", new JsonObject().put("path", "/mystore.jks").put("password", "secret")), unwrapJsonObject((Map<String, Object>) o));
  }

  @Test
  public void testAdd() throws Exception {
    o = null;
    runJavaScript("dataobject/DataObject", "add");
    HttpServerOptions actual = new HttpServerOptions(unwrapJsonObject((ScriptObjectMirror) o));
    HashSet<String> expected = new HashSet<>();
    expected.add("foo");
    expected.add("bar");
    Assert.assertEquals(expected, actual.getEnabledCipherSuites());
    o = null;
    runGroovy("dataobject/DataObject", "add");
    actual = new HttpServerOptions(unwrapJsonObject((Map<String, Object>) o));
    expected = new HashSet<>();
    expected.add("foo");
    expected.add("bar");
    Assert.assertEquals(expected, actual.getEnabledCipherSuites());
  }
}
