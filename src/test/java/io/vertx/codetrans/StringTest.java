package io.vertx.codetrans;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class StringTest extends ConversionTestBase {

  public static String o;

  @Test
  public void testStringConcat() throws Exception {
    runAll("string/StringConcat", () -> {
      Assert.assertEquals("_3_", o);
      o = null;
    });
  }

  @Test
  public void testStringEscape() throws Exception {
    runAll("string/StringEscape", () -> {
      Assert.assertEquals("\n\"\\'", o);
      o = null;
    });
  }
}
