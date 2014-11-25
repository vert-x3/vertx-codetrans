package io.vertx.examples;

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
  public void testNoArgLambdaJavaScript() throws Exception {
    invoked = 0;
    runJavaScript("lambda/NoArgLambda");
    Assert.assertEquals(1, invoked);
  }

  @Test
  public void testNoArgLambdaGroovy() throws Exception {
    invoked = 0;
    runGroovy("lambda/NoArgLambda");
    Assert.assertEquals(1, invoked);
  }

  @Test
  public void testLambdaJavaScript() throws Exception {
    o = null;
    runJavaScript("lambda/Lambda");
    Assert.assertEquals("foo", o);
  }

  @Test
  public void testLambdaGroovy() throws Exception {
    o = null;
    runGroovy("lambda/Lambda");
    Assert.assertEquals("foo", o);
  }
}
