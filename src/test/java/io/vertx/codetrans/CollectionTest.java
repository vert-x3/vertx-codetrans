package io.vertx.codetrans;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CollectionTest extends ConversionTestBase {

  public static Object o;

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
}
