package io.vertx.codetrans;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class UnaryOperatorExpressionTest extends ConversionTestBase {

  public static Object result;
  public static Object result2;

  @Before
  public void before() {
    result = result2 = null;
  }

  @Test
  public void testLogicalComplement() throws Exception {
    runAll("expression/LogicalComplement", () -> {
      assertEquals(false, result);
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
}
