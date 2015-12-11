package io.vertx.codetrans;

import static org.junit.Assert.*;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.junit.Test;

import java.util.Arrays;
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
      o = null;
    });
  }

  @Test
  public void testMapGetOnMethodReturn() {
    runAll("collection/MapGet", "getOnMethodReturn", () -> {
      assertEquals("foo_value", o);
      o = null;
    });
  }

  @Test
  public void testMapForEach() {
    runAll("collection/MapForEach", "forEach", () -> {
      assertEquals("foo -> foo_value", o.toString());
      o = null;
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

  @Test
  public void testMapNew() {
    runAll("collection/MapNew", "newMap", () -> {
      assertEquals(Collections.emptyMap(), o);
      o = null;
    });
  }

  @Test
  public void testListNew() {
    runGroovy("collection/ListApi", "newArrayList");
    assertEquals(Collections.emptyList(), o);
    o = null;
    runJavaScript("collection/ListApi", "newArrayList");
    ScriptObjectMirror so = (ScriptObjectMirror) o;
    assertEquals(0, so.size());
    o = null;
    runRuby("collection/ListApi", "newArrayList");
    assertEquals(Collections.emptyList(), o);
    o = null;
  }

  @Test
  public void testListAdd() {
    runGroovy("collection/ListApi", "add");
    assertEquals(Collections.singletonList("foo"), o);
    o = null;
    runJavaScript("collection/ListApi", "add");
    ScriptObjectMirror so = (ScriptObjectMirror) o;
    assertEquals(1, so.size());
    assertEquals("foo", so.get("0"));
    o = null;
    runRuby("collection/ListApi", "add");
    assertEquals(Collections.singletonList("foo"), o);
    o = null;
  }

  @Test
  public void testListSize() {
    runAll("collection/ListApi", "size", () -> {
      assertEquals(1, ((Number) o).intValue());
      o = null;
    });
  }

  @Test
  public void testListGet() {
    runAll("collection/ListApi", "get", () -> {
      assertEquals("foo", o);
      o = null;
    });
  }

  @Test
  public void testAsList() {
    runGroovy("collection/ListApi", "asList");
    assertEquals(Arrays.asList("foo", "bar", "juu"), o);
    o = null;
    runJavaScript("collection/ListApi", "asList");
    ScriptObjectMirror so = (ScriptObjectMirror) o;
    assertEquals(3, so.size());
    assertEquals("foo", so.get("0"));
    assertEquals("bar", so.get("1"));
    assertEquals("juu", so.get("2"));
    o = null;
    runRuby("collection/ListApi", "asList");
    assertEquals(Arrays.asList("foo", "bar", "juu"), o);
    o = null;
  }
}
