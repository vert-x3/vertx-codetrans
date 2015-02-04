package io.vertx.codetrans;

import org.junit.Test;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SystemTest extends ConversionTestBase {

  @Test
  public void testOutPrintln() {
    runAll("system/Out", "println", () -> {
      // We don't really test it uses the correct stuff but well...
    });
  }
}
