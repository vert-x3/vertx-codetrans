package io.vertx.codetrans;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ThisExpressionTest extends ConversionTestBase {

  public static Object obj;

  @Test
  public void testInstanceSelectInvocation() throws Exception {
    runAll("expression/This", "evalThis", () -> {
      assertNotNull(obj);
      obj = null;
    });
  }
}
