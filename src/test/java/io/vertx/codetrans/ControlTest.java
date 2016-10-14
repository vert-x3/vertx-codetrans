package io.vertx.codetrans;

import io.vertx.codetrans.lang.scala.ScalaLang;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ControlTest extends ConversionTestBase {

  public static void throwRuntimeException() {
    throw new RuntimeException();
  }

  public static void throwCheckedException() throws Exception {
    throw new Exception();
  }

  public static String o;

  @Before
  public void before() {
    o = null;
  }

  @Test
  public void testConditionalSkipThenEvalElse() throws Exception {
    runAll("control/Conditional", "skipThenEvalElse", () -> {
      Assert.assertEquals("inElse", o);
      o = null;
    });
  }

  @Test
  public void testConditionalEvalThenSkipElse() throws Exception {
    runAll("control/Conditional", "evalThenSkipElse", () -> {
      Assert.assertEquals("inThen", o);
      o = null;
    });
  }

  @Test
  public void testConditionalEvalThen() throws Exception {
    runAll("control/Conditional", "evalThen", () -> {
      Assert.assertEquals("inThen", o);
      o = null;
    });
  }

  @Test
  public void testConditionalSkipThen() throws Exception {
    runAll("control/Conditional", "skipThen", () -> {
      Assert.assertEquals(null, o);
      o = null;
    });
  }

  @Test
  public void testEvalThenSkipElseIfSkipElse() throws Exception {
    runAll("control/Conditional", "evalThenSkipElseIfSkipElse", () -> {
      Assert.assertEquals("inThen", o);
      o = null;
    });
  }

  @Test
  public void testSkipThenEvalElseIfSkipElse() throws Exception {
    runAll("control/Conditional", "skipThenEvalElseIfSkipElse", () -> {
      Assert.assertEquals("inElseIf", o);
      o = null;
    });
  }

  @Test
  public void testSkipThenSkipElseIfEvalElse() throws Exception {
    runAll("control/Conditional", "skipThenSkipElseIfEvalElse", () -> {
      Assert.assertEquals("inElse", o);
      o = null;
    });
  }

  public static List<String> list = Collections.unmodifiableList(Arrays.asList("foo", "bar", "juu"));
  private static List<String> collected = new ArrayList<>();

  public static void invoke(String s) {
    collected.add(s);
  }


  @Test
  public void testForEach() throws Exception {
    runAllExcept("control/ForEach", ScalaLang.class, () -> {
      Assert.assertEquals(list, collected);
      collected.clear();
    });
  }

  @Test
  public void testForLoop() throws Exception {
    runAll("control/ForLoop", () -> {
      Assert.assertEquals(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"), collected);
      collected.clear();
    });
  }

  @Test
  public void testForSequence() throws Exception {
    runAll("control/ForSequence", () -> {
      Assert.assertEquals(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"), collected);
      collected.clear();
    });
  }

  @Test
  public void testReturnVoid() throws Exception {
    runAllExcept("control/Return", "returnVoid", ScalaLang.class, () -> {
      Assert.assertNull(o);
      o = null;
    });
  }

  @Test
  public void testReturnValue() throws Exception {
    o = null;
    runAll("control/Return", "returnValue", () -> {
      Assert.assertEquals("the_returned_value", o);
      o = null;
    });
  }

  @Test
  public void testCatchCheckedException() throws Exception {
    o = null;
    runAll("control/TryCatch", "catchCheckedException", () -> {
      Assert.assertEquals("caught", o);
      o = null;
    });
  }

  @Test
  public void testCatchRuntimeException() throws Exception {
    o = null;
    runAll("control/TryCatch", "catchRuntimeException", () -> {
      Assert.assertEquals("caught", o);
      o = null;
    });
  }
}
