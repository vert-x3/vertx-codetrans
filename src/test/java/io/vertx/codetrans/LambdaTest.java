package io.vertx.codetrans;

import io.vertx.codetrans.lang.scala.ScalaLang;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class LambdaTest extends ConversionTestBase {

  public static Object o;
  private static int invoked;
  public static void count() {
    invoked++;
  }

  public static void callback(Runnable runnable) {
    runnable.run();
  }

  public static void invoke(Consumer<String> consumer) {
    consumer.accept("foo");
  }

  @Before
  public void before() {
    o = null;
    invoked = 0;
  }

  @Test
  public void testNoArg() throws Exception {
    runAllExcept("lambda/Lambda", "noArg", ScalaLang.class, () -> {
      Assert.assertEquals(1, invoked);
      invoked = 0;
    });
  }

  @Test
  public void testOneArg() throws Exception {
    runAllExcept("lambda/Lambda", "oneArg", ScalaLang.class, () -> {
      Assert.assertEquals("foo", o);
      o = null;
    });
  }

  @Test
  public void testInvokeStringHandler() throws Exception {
    runAll("lambda/Lambda", "invokeStringHandler", () -> {
      Assert.assertEquals("callback_value", o);
      o = null;
    });
  }

  @Test
  public void testInvokeStringHandlerFirstParam() throws Exception {
    runAll("lambda/Lambda", "invokeStringHandlerFirstParam", () -> {
      Assert.assertEquals("the_other_value", o);
      o = null;
    });
  }

  @Test
  public void testInvokeStringHandlerLastParam() throws Exception {
    runAll("lambda/Lambda", "invokeStringHandlerLastParam", () -> {
      Assert.assertEquals("the_other_value", o);
      o = null;
    });
  }
}
