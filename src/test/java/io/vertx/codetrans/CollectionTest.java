package io.vertx.codetrans;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CollectionTest extends ConversionTestBase {

  public static Object o;

  @Test
  public void testMap() {
    runAll("collection/MapGet", "start", () -> {
      assertEquals("foo_value", o);
    });
  }

}
