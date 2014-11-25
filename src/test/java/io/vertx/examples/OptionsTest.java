package io.vertx.examples;

import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import jdk.nashorn.internal.runtime.ScriptObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class OptionsTest extends ConversionTestBase {

  public static Object options;

  @Test
  public void testEmptyOptionsJavaScript() throws Exception {
    options = null;
    runJavaScript("options/EmptyOptions");
    Assert.assertEquals(new JsonObject(), unwrapJsonObject((ScriptObject) options));
  }

  @Test
  public void testEmptyOptionsGroovy() throws Exception {
    options = null;
    runGroovy("options/EmptyOptions");
    Assert.assertEquals(new JsonObject(), unwrapJsonObject((Map<String, Object>) options));
  }

  @Test
  public void testOptionsSetJavaScript() throws Exception {
    options = null;
    runJavaScript("options/OptionsSet");
    Assert.assertEquals(new JsonObject().put("host", "localhost").put("port", 8080), unwrapJsonObject((ScriptObject) options));
  }

  @Test
  public void testOptionsSetGroovy() throws Exception {
    options = null;
    runGroovy("options/OptionsSet");
    Assert.assertEquals(new JsonObject().put("host", "localhost").put("port", 8080), unwrapJsonObject((Map<String, Object>) options));
  }

  @Test
  public void testNestedOptionsJavaScript() throws Exception {
    options = null;
    runJavaScript("options/NestedOptions");
    Assert.assertEquals(new JsonObject().put("keyStoreOptions", new JsonObject().put("path", "/mystore.jks").put("password", "secret")), unwrapJsonObject((ScriptObject) options));
  }

  @Test
  public void testNestedOptionsGroovy() throws Exception {
    options = null;
    runGroovy("options/NestedOptions");
    Assert.assertEquals(new JsonObject().put("keyStoreOptions", new JsonObject().put("path", "/mystore.jks").put("password", "secret")), unwrapJsonObject((Map<String, Object>) options));
  }

  @Test
  public void testOptionsAddJavaScript() throws Exception {
    options = null;
    runJavaScript("options/OptionsAdd");
    HttpServerOptions actual = new HttpServerOptions(unwrapJsonObject((ScriptObject) options));
    HashSet<String> expected = new HashSet<>();
    expected.add("foo");
    expected.add("bar");
    Assert.assertEquals(expected, actual.getEnabledCipherSuites());
  }

  @Test
  public void testOptionsAddGroovy() throws Exception {
    options = null;
    runGroovy("options/OptionsAdd");
    HttpServerOptions actual = new HttpServerOptions(unwrapJsonObject((Map<String, Object>) options));
    HashSet<String> expected = new HashSet<>();
    expected.add("foo");
    expected.add("bar");
    Assert.assertEquals(expected, actual.getEnabledCipherSuites());
  }
}
