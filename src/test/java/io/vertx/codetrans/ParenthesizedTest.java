package io.vertx.codetrans;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ParenthesizedTest extends ConversionTestBase {

  public static Object a;
  public static Object b;

  @Test
  public void testParenthesizedJavaScript() throws Exception {
    a = b = null;
    runJavaScript("parenthesized/Parenthesized");
    Assert.assertEquals(15, a);
    Assert.assertEquals(9, b);
  }

  @Test
  public void testParenthesizedGroovy() throws Exception {
    a = b = null;
    runGroovy("parenthesized/Parenthesized");
    Assert.assertEquals(15, a);
    Assert.assertEquals(9, b);
  }
}
