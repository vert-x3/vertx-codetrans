package io.vertx.codetrans;

import org.junit.Test;

import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MethodExpressionTest extends ConversionTestBase {

  private static int countValue = 0;

  public static Runnable counter = () -> countValue++;

  public static void count() {
    countValue++;
  }

  @Test
  public void testInstanceSelectInvocation() throws Exception {
    runAll("expression/MethodInvocation", "instanceSelectInvocation", () -> {
      assertEquals(1, countValue);
      countValue = 0;
    });
  }

  @Test
  public void testClassSelectInvocation() throws Exception {
    runAll("expression/MethodInvocation", "classSelectInvocation", () -> {
      assertEquals(1, countValue);
      countValue = 0;
    });
  }

  @Test
  public void testIdentInvocation() throws Exception {
    Result.Failure result = (Result.Failure) convert(new GroovyLang(), "expression/MethodInvocation", "expression/MethodInvocation_instanceIdentInvocation.groovy");
    Throwable cause = result.getCause();
    assertTrue("Was expecting result to be an UnsupportedOperationException and not a " + cause.getClass().getName(),
        cause instanceof UnsupportedOperationException);
  }

  public static Object helloworld;

  @Test
  public void testMethodReference() throws Exception {
    runAll("expression/MethodReference", "lambdaisation", () -> {
      assertEquals("helloworld", helloworld);
      helloworld = null;
    });
  }

  public static Object event;

  @Test
  public void testInstanceHandlerSubtypeArgument() throws Exception {
    runAll("expression/MethodInvocation", "instanceHandlerSubtypeArgument", () -> {
      assertEquals("hello_instance", event);
      event = null;
    });
  }

  @Test
  public void testClassHandlerSubtypeArgument() throws Exception {
    runAll("expression/MethodInvocation", "classHandlerSubtypeArgument", () -> {
      assertEquals("hello_class", event);
      event = null;
    });
  }

  private static int nullCount = 0;

  public static void checkNull(String arg) {
    if (arg == null) {
      nullCount++;
    }
  }

  @Test
  public void testInvokeNullArgument() throws Exception {
    runAll("expression/MethodInvocation", "invokeNullArgument", () -> {
      assertEquals(1, nullCount);
      nullCount = 0;
    });
  }
}
