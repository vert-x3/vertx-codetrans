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

  public static Object obj;

  @Test
  public void testCallParameterizedMethodWithGivenTypeArgument() {
    obj = null;
    runAllExcept("generics/Generics", "callParameterizedMethodWithGivenTypeArgument", ScalaLang.class, () -> {
      assertEquals("hello", obj);
      obj = null;
    });
  }

  @Test
  public void testCallParameterizedMethodWithoutTypeArgument() {
    obj = null;
    runAllExcept("generics/Generics", "callParameterizedMethodWithoutTypeArgument", ScalaLang.class, () -> {
      assertEquals("hello", obj);
      obj = null;
    });
  }

  @Test
  public void testCallParameterizedMethodWithTypeArgumentInferedByType() {
    obj = null;
    runAllExcept("generics/Generics", "callParameterizedMethodWithTypeArgumentInferedByType", ScalaLang.class, () -> {
      assertEquals("the_string", obj);
      obj = null;
    });
  }

  @Test
  public void testCallParameterizedMethodWithTypeArgumentInferedByTypeArgument() {
    obj = null;
    runAllExcept("generics/Generics", "callParameterizedMethodWithTypeArgumentInferedByTypeArgument", ScalaLang.class, () -> {
      assertEquals("the_value", obj);
      obj = null;
    });
  }

  @Test
  public void testCallParameterizedMethodTypeArgumentInferedByInheritedTypeArgument() {
    obj = null;
    runAllExcept("generics/Generics", "callParameterizedMethodTypeArgumentInferedByInheritedTypeArgument", ScalaLang.class, () -> {
      assertEquals("the_value", obj);
      obj = null;
    });
  }
}
