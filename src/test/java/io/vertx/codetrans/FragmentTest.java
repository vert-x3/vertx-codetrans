package io.vertx.codetrans;

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
    Result.Source s = (Result.Source) convert(new GroovyLang(), "fragment/Fragment", "fragment/Fragment_empty.groovy");
    assertEquals("def a = null\n", s.getValue());
  }

  @Test
  public void testLine() {
    Result.Source s = (Result.Source) convert(new GroovyLang(), "fragment/Fragment", "fragment/Fragment_lineComment.groovy");
    assertEquals("" +
        "// foo\n" +
        "def t = null\n" +
        "// bar\n" +
        "def s = null\n" +
        "// juu\n", s.getValue());
  }

  @Test
  public void testMultiLine() {
    Result.Source s = (Result.Source) convert(new GroovyLang(), "fragment/Fragment", "fragment/Fragment_multiLineComment.groovy");
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
        " */\n", s.getValue());
  }
}
