package io.vertx.codetrans;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ClassIdentifierExpressionTest extends ConversionTestBase {

  private static int invoked;
  private static final ArrayList<Object> args = new ArrayList<>();
  public static Object field;

  public static void noArg() {
    invoked++;
  }
  public static void arg(Object o) {
    args.add(o);
  }

  @Before
  public void before() {
    invoked = 0;
    args.clear();
    field = null;
  }

  @Test
  public void testInvokeStaticMethod() throws Exception {
    invoked = 0;
    args.clear();
    runJavaScript("expression/ClassIdentifier", "invokeStaticMethod");
    Assert.assertEquals(1, invoked);
    Assert.assertEquals(Collections.<Object>singletonList("foo"), args);
    invoked = 0;
    args.clear();
    runGroovy("expression/ClassIdentifier", "invokeStaticMethod");
    Assert.assertEquals(1, invoked);
    Assert.assertEquals(Collections.<Object>singletonList("foo"), args);
    invoked = 0;
    args.clear();
    runScala("expression/ClassIdentifier", "invokeStaticMethod");
    Assert.assertEquals(1, invoked);
    Assert.assertEquals(Collections.<Object>singletonList("foo"), args);
  }

  @Test
  public void testAccessStaticField() throws Exception {
    field = null;
    runJavaScript("expression/ClassIdentifier", "accessStaticField");
    Assert.assertEquals("foo", field);
    field = null;
    runGroovy("expression/ClassIdentifier", "accessStaticField");
    Assert.assertEquals("foo", field);
    field = null;
    runScala("expression/ClassIdentifier", "accessStaticField");
    Assert.assertEquals("foo", field);
  }
}
