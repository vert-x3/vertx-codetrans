package io.vertx.codetrans;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class VariableTest extends ConversionTestBase {

  public static final String constant = "foo";
  public static String o;

  @Test
  public void testDeclareVariable() throws Exception {
    runAll("variable/Variable", "declare", () -> {
      Assert.assertEquals("foo", o);
      o = null;
    });
  }

  @Test
  public void testGlobalExpression() throws Exception {
    runAll("variable/Variable", "globalExpression", Collections.singletonMap("global", "the_global_value"), () -> {
      Assert.assertEquals("the_global_value", o);
      o = null;
    });
  }
}
