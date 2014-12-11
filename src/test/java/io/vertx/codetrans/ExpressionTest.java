package io.vertx.codetrans;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ExpressionTest extends ConversionTestBase {

  public static Object result;
  public static Object result2;

  @Before
  public void before() {
    result = result2 = null;
  }

  @Test
  public void testLiteralTrue() throws Exception {
    runAll("expression/LiteralTrue", () -> {
      assertEquals(true, result);
    });
  }

  @Test
  public void testLiteralFalse() throws Exception {
    runAll("expression/LiteralFalse", () -> {
      assertEquals(false, result);
    });
  }

  @Test
  public void testConditionalAnd() throws Exception {
    runAll("expression/ConditionalAnd", () -> {
      assertEquals(false, result);
    });
  }

  @Test
  public void testConditionalOr() throws Exception {
    runAll("expression/ConditionalOr", () -> {
      assertEquals(true, result);
    });
  }

  @Test
  public void testNegate() throws Exception {
    runAll("expression/LogicalComplement", () -> {
      assertEquals(false, result);
    });
  }

  @Test
  public void testEqualTo() throws Exception {
    runAll("expression/EqualTo", () -> {
      assertEquals(false, result);
    });
  }

  @Test
  public void testNotEqualTo() throws Exception {
    runAll("expression/NotEqualTo", () -> {
      assertEquals(true, result);
    });
  }

  @Test
  public void testLessThan() throws Exception {
    runAll("expression/LessThan", () -> {
      assertEquals(true, result);
    });
  }

  @Test
  public void testLessThanEqual() throws Exception {
    runAll("expression/LessThanEqual", () -> {
      assertEquals(true, result);
    });
  }

  @Test
  public void testGreaterThan() throws Exception {
    runAll("expression/GreaterThan", () -> {
      assertEquals(false, result);
    });
  }

  @Test
  public void testGreaterThanEqual() throws Exception {
    runAll("expression/GreaterThanEqual", () -> {
      assertEquals(false, result);
    });
  }

  @Test
  public void testAnd() throws Exception {
    runAll("expression/And", () -> {
      assertEquals(2, result);
    });
  }

  @Test
  public void testOr() throws Exception {
    runAll("expression/Or", () -> {
      assertEquals(7, result);
    });
  }

  @Test
  public void testXor() throws Exception {
    runAll("expression/Xor", () -> {
      assertEquals(5, result);
    });
  }

  @Test
  public void testLiteralPositiveInteger() throws Exception {
    runAll("expression/LiteralPositiveInteger", () -> {
      assertEquals(4, result);
    });
  }

  @Test
  public void testLiteralNegativeInteger() throws Exception {
    runAll("expression/LiteralNegativeInteger", () -> {
      assertEquals(-4, result);
    });
  }

  @Test
  public void testUnaryMinus() throws Exception {
    runAll("expression/UnaryMinus", () -> {
      assertEquals(-4, result);
    });
  }

  @Test
  public void testUnaryPlus() throws Exception {
    runAll("expression/UnaryPlus", () -> {
      assertEquals(4, result);
    });
  }

  @Test
  public void testPostfixIncrement() throws Exception {
    runAll("expression/PostfixIncrement", () -> {
      assertEquals(3, ((Number) result).intValue());
      assertEquals(4, ((Number) result2).intValue());
    });
  }

  @Test
  public void testPostfixDecrement() throws Exception {
    runAll("expression/PostfixDecrement", () -> {
      assertEquals(3, ((Number) result).intValue());
      assertEquals(2, ((Number) result2).intValue());
    });
  }

  @Test
  public void testPrefixIncrement() throws Exception {
    runAll("expression/PrefixIncrement", () -> {
      assertEquals(4, ((Number) result).intValue());
      assertEquals(4, ((Number) result2).intValue());
    });
  }

  @Test
  public void testPrefixDecrement() throws Exception {
    runAll("expression/PrefixDecrement", () -> {
      assertEquals(2, ((Number) result).intValue());
      assertEquals(2, ((Number) result2).intValue());
    });
  }

  @Test
  public void testPlus() throws Exception {
    runAll("expression/Plus", () -> {
      assertEquals(5, result);
    });
  }

  @Test
  public void testMinus() throws Exception {
    runAll("expression/Minus", () -> {
      assertEquals(-1, result);
    });
  }

  @Test
  public void testMultiply() throws Exception {
    runAll("expression/Multiply", () -> {
      assertEquals(6, result);
    });
  }

  @Test
  public void testDivide() throws Exception {
    runAll("expression/Divide", () -> {
      assertEquals(3, ((Number)result).intValue());
    });
  }

  @Test
  public void testRemainder() throws Exception {
    runAll("expression/Remainder", () -> {
      assertEquals(1, result);
    });
  }

  @Test
  public void testLiteralString() throws Exception {
    runAll("expression/LiteralString", () -> {
      assertEquals("foobar", result);
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
  public void testParenthesizedJavaScript() throws Exception {
    runJavaScript("expression/Parenthesized");
    assertEquals(15, result);
    assertEquals(9, result2);
  }

  @Test
  public void testParenthesizedGroovy() throws Exception {
    runGroovy("expression/Parenthesized");
    assertEquals(15, result);
    assertEquals(9, result2);
  }

  @Test
  public void testStringConcat() throws Exception {
    runAll("expression/LiteralStringConcat", () -> {
      assertEquals("_3_", result.toString());
    });
  }

  @Test
  public void testStringEscape() throws Exception {
    runAll("expression/LiteralStringEscape", () -> {
      assertEquals("\n\"\\'", result.toString());
    });
  }
}
