package io.vertx.codetrans;

import static org.junit.Assert.*;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
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
    runGroovy("collection/ListNew", "newArrayList");
    assertEquals(Collections.emptyList(), o);
    o = null;
    runJavaScript("collection/ListNew", "newArrayList");
    ScriptObjectMirror so = (ScriptObjectMirror) o;
    assertEquals(0, so.size());
    o = null;
    runRuby("collection/ListNew", "newArrayList");
    assertEquals(Collections.emptyList(), o);
    o = null;
  }

  @Test
  public void testListAdd() {
    runGroovy("collection/ListAdd", "addToList");
    assertEquals(Collections.singletonList("foo"), o);
    o = null;
    runJavaScript("collection/ListAdd", "addToList");
    ScriptObjectMirror so = (ScriptObjectMirror) o;
    assertEquals(1, so.size());
    assertEquals("foo", so.get(0));
    o = null;
    runRuby("collection/ListAdd", "addToList");
    assertEquals(Collections.singletonList("foo"), o);
    o = null;
  }

  @Test
  public void testListSize() {
    runAll("collection/ListSize", "size", () -> {
      assertEquals(1, ((Number) o).intValue());
      o = null;
    });
  }
}
