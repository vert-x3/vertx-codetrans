package io.vertx.codetrans;

import org.junit.Assert;
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

  @Test
  public void testNoArg() throws Exception {
    invoked = 0;
    runJavaScript("lambda/Lambda", "noArg");
    Assert.assertEquals(1, invoked);
    invoked = 0;
    runGroovy("lambda/Lambda", "noArg");
    Assert.assertEquals(1, invoked);
  }

  @Test
  public void testOneArg() throws Exception {
    o = null;
    runJavaScript("lambda/Lambda", "oneArg");
    Assert.assertEquals("foo", o);
    o = null;
    runGroovy("lambda/Lambda", "oneArg");
    Assert.assertEquals("foo", o);
  }
}
