package io.vertx.codetrans;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MethodInvocationExpressionTest extends ConversionTestBase {

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
}
