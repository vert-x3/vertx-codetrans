package io.vertx.codetrans;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ObjectTest extends ConversionTestBase {

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
    runGroovy("object/Equals", "objectEquals");
    Assert.assertEquals(true, isEquals);
    Assert.assertFalse(called);
  }

  @Test
  public void testObjectEqualsJavaScript() throws Exception {
    runJavaScript("object/Equals", "objectEquals");
    Assert.assertEquals(true, isEquals);
    Assert.assertFalse(called);
  }

  @Test
  public void testObjectEqualsRuby() throws Exception {
    runRuby("object/Equals", "objectEquals");
    Assert.assertEquals(true, isEquals);
    Assert.assertTrue(called);
  }

  @Test
  public void testStringEquals() throws Exception {
    runAll("object/Equals", "stringEquals", () -> {
      Assert.assertEquals(true, isEquals);
      isEquals = false;
    });
  }

  @Test
  public void testStringNotEquals() throws Exception {
    runAll("object/Equals", "stringNotEquals", () -> {
      Assert.assertEquals(false, isEquals);
    });
  }
}
