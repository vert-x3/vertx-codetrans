package io.vertx.codetrans;

import io.vertx.codetrans.lang.groovy.GroovyLang;
import io.vertx.codetrans.lang.js.JavaScriptLang;
import io.vertx.codetrans.lang.ruby.RubyLang;
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
    s = loadScript(new JavaScriptLang(), "fragment/Fragment", "empty");
    assertEquals("var a = null;\n", s.getSource());
    s = loadScript(new RubyLang(), "fragment/Fragment", "empty");
    assertEquals("a = nil\n", s.getSource());
  }

  @Test
  public void testOnlyLineComments() {
    Script s = loadScript(new GroovyLang(), "fragment/Fragment", "onlyLineComments");
    assertEquals("" +
        "// foo\n" +
        "// bar\n" +
        "// juu\n", s.getSource());
    s = loadScript(new JavaScriptLang(), "fragment/Fragment", "onlyLineComments");
    assertEquals("" +
        "// foo\n" +
        "// bar\n" +
        "// juu\n", s.getSource());
    s = loadScript(new RubyLang(), "fragment/Fragment", "onlyLineComments");
    assertEquals("" +
        "# foo\n" +
        "# bar\n" +
        "# juu\n", s.getSource());
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
    s = loadScript(new JavaScriptLang(), "fragment/Fragment", "lineComments");
    assertEquals("" +
        "// foo\n" +
        "var t = null;\n" +
        "// bar\n" +
        "var s = null;\n" +
        "// juu\n", s.getSource());
    s = loadScript(new RubyLang(), "fragment/Fragment", "lineComments");
    assertEquals("" +
        "# foo\n" +
        "t = nil\n" +
        "# bar\n" +
        "s = nil\n" +
        "# juu\n", s.getSource());
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
    s = loadScript(new JavaScriptLang(), "fragment/Fragment", "multiLineComments");
    assertEquals("" +
        "/*a\n" +
        "  b\n" +
        " c\n" +
        "d\n" +
        "e\n" +
        "f\n" +
        " */\n" +
        "var t = null;\n" +
        "/*\n" +
        " * a\n" +
        " */\n", s.getSource());
    s = loadScript(new RubyLang(), "fragment/Fragment", "multiLineComments");
    assertEquals("" +
        "=begina\n" +
        "  b\n" +
        " c\n" +
        "d\n" +
        "e\n" +
        "f\n" +
        " =end\n" +
        "t = nil\n" +
        "=begin\n" +
        " * a\n" +
        " =end\n", s.getSource());
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
    s = loadScript(new JavaScriptLang(), "fragment/Fragment", "lineCommentsInLamba");
    assertEquals("" +
        "Java.type(\"io.vertx.codetrans.FragmentTest\").someMethod(function (arg) {\n" +
        "  // foo\n" +
        "  var t = null;\n" +
        "  // bar\n" +
        "  var s = null;\n" +
        "  // juu\n" +
        "});\n", s.getSource());
    s = loadScript(new RubyLang(), "fragment/Fragment", "lineCommentsInLamba");
    assertEquals("" +
        "Java::IoVertxCodetrans::FragmentTest.some_method() { |arg|\n" +
        "  # foo\n" +
        "  t = nil\n" +
        "  # bar\n" +
        "  s = nil\n" +
        "  # juu\n" +
        "}\n", s.getSource());
  }
}
