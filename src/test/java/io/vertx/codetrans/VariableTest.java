package io.vertx.codetrans;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class VariableTest extends ConversionTestBase {

  public static final String constant = "foo";
  public static Object o;

  @Test
  public void testDeclareVariable() throws Exception {
    runAll("variable/Variable", "declare", () -> {
      Assert.assertEquals("foo", o);
      o = null;
    });
  }

  @Test
  public void testGlobalExpression() throws Exception {
    runAll("variable/Variable", "globalExpression", Collections.singletonMap("vertx", "vertx_object"), () -> {
      Assert.assertEquals("vertx_object", o);
      o = null;
    });
  }

  @Test
  public void testMemberExpression() throws Exception {
    runAll("variable/Variable", "memberExpression", () -> {
      Assert.assertEquals("member_value", o);
      o = null;
    });
  }

  @Test
  public void testMemberExpressionAccessedByMethod() throws Exception {
    runAll("variable/Variable", "memberExpressionAccessedByMethod", () -> {
      Assert.assertEquals("member_value", o);
      o = null;
    });
  }
}
