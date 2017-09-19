package io.vertx.codetrans;

import io.vertx.codetrans.lang.groovy.GroovyLang;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MethodExpressionTest extends ConversionTestBase {

  private static int countValue;
  public static Object state;

  public static Runnable counter = () -> countValue++;

  @Before
  public void before() {
    countValue = 0;
    state = null;
  }

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
  public void testThisInvocation() throws Exception {
    runAll("expression/MethodInvocation", "thisInvocation", () -> {
      assertEquals(1, countValue);
      countValue = 0;
    });
  }

  @Test
  public void testThisInvocationWithParam() throws Exception {
    run(new GroovyLang(), "expression/MethodInvocation", "thisInvocationWithParam");
    assertEquals("the_arg", state);
  }

  public static Object helloworld;

  @Test
  public void testMethodReference() throws Exception {
    runAll("expression/MethodReference", "methodReference", () -> {
      assertEquals("hellocallback_value", helloworld);
      helloworld = null;
    });
  }

  @Test
  public void testThisMethodReference() throws Exception {
    runAll("expression/MethodReference", "thisMethodReference", () -> {
      assertEquals("hellocallback_value", helloworld);
      helloworld = null;
    });
  }

  @Test
  public void testThisMethodReferenceNotLast() throws Exception {
    runAll("expression/MethodReference", "thisMethodReferenceNotLast", () -> {
      assertEquals("hellothe_last_value", helloworld);
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

  @Test
  public void testBooleanApiGetter() throws Exception {
    runAll("expression/MethodNaming", "booleanApiGetter", () -> {
    });
  }

  @Test
  public void testBooleanApiMethod() throws Exception {
    runAll("expression/MethodNaming", "booleanApiMethod", () -> {
    });
  }

  @Test
  public void testInvokeMethodWithBooleanReturn() throws Exception {
    runAll("expression/MethodInvocation", "invokeMethodWithBooleanReturn", () -> {
      assertEquals(1, countValue);
      countValue = 0;
    });
  }

  @Test
  public void testInvokeMethodWithIntReturn() throws Exception {
    runAll("expression/MethodInvocation", "invokeMethodWithIntReturn", () -> {
      assertEquals(1, countValue);
      countValue = 0;
    });
  }

  @Test
  public void testInvokeMethodWithIntegerReturn() throws Exception {
    runAll("expression/MethodInvocation", "invokeMethodWithIntegerReturn", () -> {
      assertEquals(1, countValue);
      countValue = 0;
    });
  }
}
