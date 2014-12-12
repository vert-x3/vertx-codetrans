package io.vertx.codetrans;

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
  public void testEmpty() throws Exception {
    options = null;
    runJavaScript("options/Options", "empty");
    Assert.assertEquals(new JsonObject(), unwrapJsonObject((ScriptObject) options));
    options = null;
    runGroovy("options/Options", "empty");
    Assert.assertEquals(new JsonObject(), unwrapJsonObject((Map<String, Object>) options));
  }

  @Test
  public void testSet() throws Exception {
    options = null;
    runJavaScript("options/Options", "set");
    Assert.assertEquals(new JsonObject().put("host", "localhost").put("port", 8080), unwrapJsonObject((ScriptObject) options));
    options = null;
    runGroovy("options/Options", "set");
    Assert.assertEquals(new JsonObject().put("host", "localhost").put("port", 8080), unwrapJsonObject((Map<String, Object>) options));
  }

  @Test
  public void testNested() throws Exception {
    options = null;
    runJavaScript("options/Options", "nested");
    Assert.assertEquals(new JsonObject().put("keyStoreOptions", new JsonObject().put("path", "/mystore.jks").put("password", "secret")), unwrapJsonObject((ScriptObject) options));
    options = null;
    runGroovy("options/Options", "nested");
    Assert.assertEquals(new JsonObject().put("keyStoreOptions", new JsonObject().put("path", "/mystore.jks").put("password", "secret")), unwrapJsonObject((Map<String, Object>) options));
  }

  @Test
  public void testAdd() throws Exception {
    options = null;
    runJavaScript("options/Options", "add");
    HttpServerOptions actual = new HttpServerOptions(unwrapJsonObject((ScriptObject) options));
    HashSet<String> expected = new HashSet<>();
    expected.add("foo");
    expected.add("bar");
    Assert.assertEquals(expected, actual.getEnabledCipherSuites());
    options = null;
    runGroovy("options/Options", "add");
    actual = new HttpServerOptions(unwrapJsonObject((Map<String, Object>) options));
    expected = new HashSet<>();
    expected.add("foo");
    expected.add("bar");
    Assert.assertEquals(expected, actual.getEnabledCipherSuites());
  }
}
