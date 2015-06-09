package io.vertx.codetrans;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SystemTest extends ConversionTestBase {

  @Test
  public void testOutPrintln() {
    testPrintln(() -> System.out, System::setOut, "outPrintln");
  }

  @Test
  public void testErrPrintln() {
    testPrintln(() -> System.err, System::setErr, "errPrintln");
  }

  private void testPrintln(Supplier<PrintStream> getter, Consumer<PrintStream> setter, String method) {
    PrintStream prev = getter.get();
    ByteArrayOutputStream writer = new ByteArrayOutputStream();
    try {
      PrintStream next = new PrintStream(writer);
      setter.accept(next);
      runAll("api/SystemApi", method, () -> {
        next.flush();
        assertEquals("hello" + System.getProperty("line.separator"), writer.toString());
        writer.reset();
      });
    } finally {
      setter.accept(prev);
    }
  }
}
