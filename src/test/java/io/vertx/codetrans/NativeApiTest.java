package io.vertx.codetrans;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class NativeApiTest extends ConversionTestBase {

  public static Object isEquals;
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
    runGroovy("nativeapi/NativeMethods", "objectEquals");
    Assert.assertEquals(true, isEquals);
    Assert.assertFalse(called);
  }

  @Test
  public void testObjectEqualsJavaScript() throws Exception {
    runJavaScript("nativeapi/NativeMethods", "objectEquals");
    Assert.assertEquals(true, isEquals);
    Assert.assertFalse(called);
  }

  @Test
  public void testObjectEqualsRuby() throws Exception {
    runRuby("nativeapi/NativeMethods", "objectEquals");
    Assert.assertEquals(true, isEquals);
    Assert.assertTrue(called);
  }

  @Test
  public void testStringEquals() throws Exception {
    runAll("nativeapi/NativeMethods", "stringEquals", () -> {
      Assert.assertEquals(true, isEquals);
      isEquals = false;
    });
  }

  @Test
  public void testStringNotEquals() throws Exception {
    runAll("nativeapi/NativeMethods", "stringNotEquals", () -> {
      Assert.assertEquals(false, isEquals);
    });
  }

  @Test
  public void testStringStartsWithTrue() throws Exception {
    runAll("nativeapi/NativeMethods", "stringStartsWithTrue", () -> {
      Assert.assertEquals(true, isEquals);
    });
  }

  @Test
  public void testStringStartsWithFalse() throws Exception {
    runAll("nativeapi/NativeMethods", "stringStartsWithFalse", () -> {
      Assert.assertEquals(false, isEquals);
    });
  }
}
