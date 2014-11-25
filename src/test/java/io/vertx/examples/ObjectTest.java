package io.vertx.examples;

import org.junit.Assert;
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

  @Test
  public void testEqualsJavaScript() throws Exception {
    isEquals = null;
    called = false;
    runJavaScript("object/Equals");
    Assert.assertEquals(true, isEquals);
    Assert.assertFalse(called);
  }

  @Test
  public void testEqualsGroovy() throws Exception {
    isEquals = null;
    called = false;
    runGroovy("object/Equals");
    Assert.assertEquals(true, isEquals);
    Assert.assertFalse(called);
  }
}
