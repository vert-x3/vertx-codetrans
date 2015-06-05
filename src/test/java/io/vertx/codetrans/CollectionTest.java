package io.vertx.codetrans;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CollectionTest extends ConversionTestBase {

  public static Object o;
  public static Map sharedMap;

  @Test
  public void testMapGetOnVariable() {
    runAll("collection/MapGet", "getOnVariable", () -> {
      assertEquals("foo_value", o);
    });
  }

  @Test
  public void testMapGetOnMethodReturn() {
    runAll("collection/MapGet", "getOnMethodReturn", () -> {
      assertEquals("foo_value", o);
    });
  }

  @Test
  public void testMapForEach() {
    runAll("collection/MapForEach", "forEach", () -> {
      assertEquals("foo -> foo_value", o.toString());
    });
  }

  @Test
  public void testMapPut() {
    sharedMap = new HashMap<>();
    runGroovy("collection/MapPut", "put");
    assertEquals(Collections.singletonMap("foo", "foo_value"), sharedMap);
    sharedMap = new HashMap<>();
    runJavaScript("collection/MapPut", "put");
    assertEquals(Collections.singletonMap("foo", "foo_value"), sharedMap);
    sharedMap = new HashMap<>();
    runRuby("collection/MapPut", "put");
    assertEquals(Collections.singletonMap("foo", "foo_value"), sharedMap);
  }
}
