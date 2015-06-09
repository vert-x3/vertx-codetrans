package io.vertx.codetrans;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SystemTest extends ConversionTestBase {

  @Test
  public void testOutPrintln() {
    PrintStream prevOut = System.out;
    ByteArrayOutputStream writer = new ByteArrayOutputStream();
    try {
      PrintStream nextOut = new PrintStream(writer);
      System.setOut(nextOut);
      runAll("system/Out", "println", () -> {
        nextOut.flush();
        assertEquals("hello" + System.getProperty("line.separator"), writer.toString());
        writer.reset();
      });
    } finally {
      System.setOut(prevOut);
    }
  }
}
