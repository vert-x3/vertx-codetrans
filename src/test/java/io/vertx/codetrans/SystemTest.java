package io.vertx.codetrans;

import io.vertx.codetrans.lang.scala.ScalaLang;
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
      runAllExcept("api/SystemApi", method, ScalaLang.class, () -> {
        next.flush();
        assertEquals("hello\n", line2unix(writer.toString()));
        writer.reset();
      });
    } finally {
      setter.accept(prev);
    }
  }

  // fix ruby line ending comparison in Windows
  // (https://github.com/vert-x3/vertx-codetrans/issues/10)
  private String line2unix(String text) {
    if (System.getProperty("line.separator").equals("\n")) {
      return text;
    } else {
      return text.replace(System.getProperty("line.separator"), "\n");
    }
  }
}
