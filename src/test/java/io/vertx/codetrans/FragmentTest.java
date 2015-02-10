package io.vertx.codetrans;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class FragmentTest extends ConversionTestBase {

  @Test
  public void testEmpty() {
    String s = convert(new GroovyLang(), "fragment/Fragment", "fragment/Fragment_empty.groovy");
    assertEquals("def a = null\n", s);
  }

  @Test
  public void testLine() {
    String s = convert(new GroovyLang(), "fragment/Fragment", "fragment/Fragment_lineComment.groovy");
    assertEquals("" +
        "// foo\n" +
        "def t = null\n" +
        "// bar\n" +
        "def s = null\n" +
        "// juu\n", s);
  }

  @Test
  public void testMultiLine() {
    String s = convert(new GroovyLang(), "fragment/Fragment", "fragment/Fragment_multiLineComment.groovy");
    assertEquals("" +
        "/*a\n" +
        "  b\n" +
        " c\n" +
        "d\n" +
        "e\n" +
        "f\n" +
        " */\n" +
        "def t = null\n" +
        "/*\n" +
        " * a\n" +
        " */\n", s);
  }
}
