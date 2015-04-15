package io.vertx.codetrans;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ControlTest extends ConversionTestBase {

  public static String o;

  @Test
  public void testIfElseEvalElse() throws Exception {
    runAll("control/IfElse", "evalElse", () -> {
      Assert.assertEquals("inElse", o);
      o = null;
    });
  }

  @Test
  public void testIfElseEvalThen() throws Exception {
    runAll("control/IfElse", "evalThen", () -> {
      Assert.assertEquals("inThen", o);
      o = null;
    });
  }

  @Test
  public void testIfEvalThen() throws Exception {
    runAll("control/If", "evalThen", () -> {
      Assert.assertEquals("inThen", o);
      o = null;
    });
  }

  @Test
  public void testIfSkipThen() throws Exception {
    runAll("control/If", "skipThen", () -> {
      Assert.assertEquals(null, o);
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
    runAll("control/ForEach", () -> {
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
}
