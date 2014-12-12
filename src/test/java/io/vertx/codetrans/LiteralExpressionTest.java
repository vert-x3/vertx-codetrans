package io.vertx.codetrans;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class LiteralExpressionTest extends ConversionTestBase {

  public static Object result;

  @Before
  public void before() {
    result = null;
  }


  @Test
  public void testLiteralString() throws Exception {
    runAll("expression/LiteralString", "value", () -> {
      assertEquals("foobar", result);
    });
    runAll("expression/LiteralString", "concat", () -> {
      assertEquals("_3_", result.toString());
    });
    runAll("expression/LiteralString", "escape", () -> {
      assertEquals("\n\"\\'", result.toString());
    });
  }

  @Test
  public void testLiteralChar() throws Exception {
    runAll("expression/LiteralChar", () -> {
      assertEquals("a", result);
    });
  }

  @Test
  public void testLiteralNull() throws Exception {
    runAll("expression/LiteralNull", () -> {
      assertEquals(null, result);
    });
  }

  @Test
  public void testLiteralInteger() throws Exception {
    runAll("expression/LiteralInteger", "positiveValue", () -> {
      assertEquals(4, result);
    });
    runAll("expression/LiteralInteger", "negativeValue", () -> {
      assertEquals(-4, result);
    });
  }

  @Test
  public void testLiteralBoolean() throws Exception {
    runAll("expression/LiteralBoolean", "trueValue", () -> {
      assertEquals(true, result);
    });
    runAll("expression/LiteralBoolean", "falseValue", () -> {
      assertEquals(false, result);
    });
  }
}
