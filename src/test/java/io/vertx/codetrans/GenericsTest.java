package io.vertx.codetrans;

import io.vertx.codetrans.lang.scala.ScalaLang;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class GenericsTest extends ConversionTestBase {

  public static String obj;

  @Test
  public void testCallParameterizedMethodWithTypeArgument() {
    runAllExcept("generics/Generics", "callParameterizedMethodWithTypeArgument", ScalaLang.class, () -> {
      assertEquals("hello", obj);
      obj = null;
    });
  }

  @Test
  public void testCallParameterizedMethodWithDefaultTypeArgument() {
    runAllExcept("generics/Generics", "callParameterizedMethodWithDefaultTypeArgument", ScalaLang.class, () -> {
      assertEquals("hello", obj);
      obj = null;
    });
  }
}
