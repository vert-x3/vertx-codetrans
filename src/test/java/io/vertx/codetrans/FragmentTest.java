package io.vertx.codetrans;

import io.vertx.codetrans.lang.groovy.GroovyLang;
import io.vertx.core.Handler;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class FragmentTest extends ConversionTestBase {

  @Test
  public void testFragmentParser() {
    assertFragment("", "");
    assertFragment("", " ");
    assertFragment("\n", "\n");
    assertFragment("<a>", "//a");
    assertFragment("<a>\n", "//a\n");
    assertFragment("<a>\n", " //a\n");
    assertFragment("/*a*/", "/*a*/");
    assertFragment("/*a*/", "/*a*/");
    assertFragment("/***/", "/***/");
    assertFragment("/****/", "/****/");
    assertFragment("/*\n*/",
        "/*\n" +
        "*/");
    assertFragment("/*\n*/",
        " /*\n" +
        " */");
    assertFragment("/*\n */",
        " /*\n" +
        "  */");
    assertFragment("/*\na */",
        " /*\n" +
        "a */");
    assertFragment("/* \n*/",
        " /* \n" +
        "*/");
  }

  private void assertFragment(String expected, String s) {
    StringBuilder test = new StringBuilder();
    new FragmentParser() {
      @Override
      public void onNewline() {
        test.append('\n');
      }
      @Override
      public void onComment(char c) {
        test.append(c);
      }
      @Override
      public void onBeginComment(boolean multiline) {
        test.append(multiline ? "/*" : "<");
      }
      @Override
      public void onEndComment(boolean multiline) {
        test.append(multiline ? "*/" : ">");
      }
    }.parse(s);
    assertEquals(expected, test.toString());
  }

  @Test
  public void testEmpty() {
    Script s = loadScript(new GroovyLang(), "fragment/Fragment", "empty");
    assertEquals("def a = null\n", s.getSource());
  }

  @Test
  public void testOnlyLineComments() {
    Script s = loadScript(new GroovyLang(), "fragment/Fragment", "onlyLineComments");
    assertEquals("" +
        "// foo\n" +
        "// bar\n" +
        "// juu\n", s.getSource());
  }

  @Test
  public void testLineComments() {
    Script s = loadScript(new GroovyLang(), "fragment/Fragment", "lineComments");
    assertEquals("" +
        "// foo\n" +
        "def t = null\n" +
        "// bar\n" +
        "def s = null\n" +
        "// juu\n", s.getSource());
  }

  @Test
  public void testMultiLineComments() {
    Script s = loadScript(new GroovyLang(), "fragment/Fragment", "multiLineComments");
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
        " */\n", s.getSource());
  }

  public static void someMethod(Handler<String> handler) {
  }

  @Test
  public void testLineCommentsInLambda() {
    Script s = loadScript(new GroovyLang(), "fragment/Fragment", "lineCommentsInLamba");
    assertEquals("" +
        "io.vertx.codetrans.FragmentTest.someMethod({ arg ->\n" +
        "  // foo\n" +
        "  def t = null\n" +
        "  // bar\n" +
        "  def s = null\n" +
        "  // juu\n" +
        "})\n", s.getSource());
  }
}
