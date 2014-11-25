package io.vertx.codetrans;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ClassExpressionTest extends ConversionTestBase {

  private static int invoked;
  private static final ArrayList<Object> args = new ArrayList<>();
  public static Object field;

  public static void noArg() {
    invoked++;
  }
  public static void arg(Object o) {
    args.add(o);
  }

  @Test
  public void testInvokeStaticMethodJavaScript() throws Exception {
    invoked = 0;
    args.clear();
    runJavaScript("classexpression/InvokeStaticMethodVerticle");
    Assert.assertEquals(1, invoked);
    Assert.assertEquals(Collections.<Object>singletonList("foo"), args);
  }

  @Test
  public void testInvokeStaticMethodGroovy() throws Exception {
    invoked = 0;
    args.clear();
    runGroovy("classexpression/InvokeStaticMethodVerticle");
    Assert.assertEquals(1, invoked);
    Assert.assertEquals(Collections.<Object>singletonList("foo"), args);
  }

  @Test
  public void testAccessStaticFieldJavaScript() throws Exception {
    field = null;
    runJavaScript("classexpression/AccessStaticFieldVerticle");
    Assert.assertEquals("foo", field);
  }

  @Test
  public void testAccessStaticFieldGroovy() throws Exception {
    field = null;
    runGroovy("classexpression/AccessStaticFieldVerticle");
    Assert.assertEquals("foo", field);
  }
}
