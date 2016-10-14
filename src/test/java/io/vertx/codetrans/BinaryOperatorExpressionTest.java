package io.vertx.codetrans;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class BinaryOperatorExpressionTest extends ConversionTestBase {

  public static Boolean result;
  
  public static Number numResult;

  @Before
  public void before() {
    result = null;
    numResult = null;
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
      assertEquals(2, numResult.intValue());
    });
  }


  @Test
  public void testOr() throws Exception {
    runAll("expression/Or", () -> {
      assertEquals(7, numResult.intValue());
    });
  }

  @Test
  public void testXor() throws Exception {
    runAll("expression/Xor", () -> {
      assertEquals(5, numResult.intValue());
    });
  }

  @Test
  public void testPlus() throws Exception {
    runAll("expression/Plus", () -> {
      assertEquals(5, numResult.intValue());
    });
  }

  @Test
  public void testMinus() throws Exception {
    runAll("expression/Minus", () -> {
      assertEquals(-1, numResult.intValue());
    });
  }

  @Test
  public void testMultiply() throws Exception {
    runAll("expression/Multiply", () -> {
      assertEquals(6, numResult.intValue());
    });
  }

  @Test
  public void testDivide() throws Exception {
    runAll("expression/Divide", () -> {
      assertEquals(3, numResult.intValue());
    });
  }

  @Test
  public void testRemainder() throws Exception {
    runAll("expression/Remainder", () -> {
      assertEquals(1, numResult.intValue());
    });
  }

}
