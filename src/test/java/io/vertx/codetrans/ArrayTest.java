package io.vertx.codetrans;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class ArrayTest extends ConversionTestBase {

  public static int[] ia;
  public static String[] sa;

  @Before
  public void before() {
    ia = null;
    sa = null;
  }

  @Test
  public void testPrimitiveArrayDeclare() {
    runAll("expression/NewArray", "primitiveArrayDeclare", () -> {
      assertArrayEquals(new int[]{1, 2}, ia);
    });
  }

  @Test
  public void testArrayDeclare() {
    runAll("expression/NewArray", "arrayDeclare", () -> {
      assertArrayEquals(new String[]{"foo", "bar"}, sa);
    });
  }

  @Test
  public void testNewPrimitiveArrayExpression() {
    runAll("expression/NewArray", "newPrimitiveArrayExpression", () -> {
      assertArrayEquals(new int[]{1, 2}, ia);
    });
  }

  @Test
  public void testNewArrayExpression() {
    runAll("expression/NewArray", "newArrayExpression", () -> {
      assertArrayEquals(new String[]{"foo", "bar"}, sa);
    });
  }
}
