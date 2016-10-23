package io.vertx.codetrans;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class InterropTest extends ConversionTestBase {

  public static Boolean isEquals;
  private static boolean called;
  public final static Object o = new Object() {
    @Override
    public boolean equals(Object obj) {
      called = true;
      return obj == this;
    }
  };

  @Before
  public void before() {
    isEquals = null;
    called = false;
  }

  @Test
  public void testObjectEqualsGroovy() throws Exception {
    runGroovy("interrop/MethodInterrop", "objectEquals");
    Assert.assertEquals(true, isEquals);
    Assert.assertFalse(called);
  }

  @Test
  public void testObjectEqualsJavaScript() throws Exception {
    runJavaScript("interrop/MethodInterrop", "objectEquals");
    Assert.assertEquals(true, isEquals);
    Assert.assertFalse(called);
  }

  @Test
  public void testObjectEqualsRuby() throws Exception {
    runRuby("interrop/MethodInterrop", "objectEquals");
    Assert.assertEquals(true, isEquals);
    Assert.assertTrue(called);
  }

  @Test
  public void testStringEquals() throws Exception {
    runAll("interrop/MethodInterrop", "stringEquals", () -> {
      Assert.assertEquals(true, isEquals);
      isEquals = false;
    });
  }

  @Test
  public void testStringNotEquals() throws Exception {
    runAll("interrop/MethodInterrop", "stringNotEquals", () -> {
      Assert.assertEquals(false, isEquals);
    });
  }

  @Test
  public void testStringStartsWithTrue() throws Exception {
    runAll("interrop/MethodInterrop", "stringStartsWithTrue", () -> {
      Assert.assertEquals(true, isEquals);
    });
  }

  @Test
  public void testStringStartsWithFalse() throws Exception {
    runAll("interrop/MethodInterrop", "stringStartsWithFalse", () -> {
      Assert.assertEquals(false, isEquals);
    });
  }

  public static Number numericValue;

  @Test
  public void testReadConstant() throws Exception {
    runAll("interrop/FieldInterrop", "readConstant", () -> {
      Assert.assertEquals(DateFormat.SHORT, numericValue.intValue());
      numericValue = null;
    });
  }

  @Test
  public void testZeroArgConstructor() throws Exception {
    runAll("interrop/ConstructorInterrop", "zeroArgConstructor", () -> {
      Assert.assertEquals(1, numericValue.intValue());
      numericValue = null;
    });
  }

  @Test
  public void testOneArgConstructor() throws Exception {
    runAll("interrop/ConstructorInterrop", "oneArgConstructor", () -> {
      Assert.assertEquals(5, numericValue.intValue());
      numericValue = null;
    });
  }
}
