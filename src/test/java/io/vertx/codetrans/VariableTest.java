package io.vertx.codetrans;

import org.junit.Assert;
import org.junit.Test;

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
}
