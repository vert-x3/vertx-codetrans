package io.vertx.codetrans;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ParenthesizedExpressionTest extends ConversionTestBase {

  public static Number result;
  public static Number result2;

  @Before
  public void before() {
    result = result2 = null;
  }

  @Test
  public void testParenthesized() throws Exception {
    runAll("expression/Parenthesized", () -> {
      assertEquals(15, result.intValue());
      assertEquals(9, result2.intValue());
    });
  }
}
