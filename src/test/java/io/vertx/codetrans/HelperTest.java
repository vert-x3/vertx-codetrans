package io.vertx.codetrans;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class HelperTest {

//  @Test
//  public void testExtractComment() {
//    // At the moment we drop multiline comment
//    assertEquals(Arrays.<String>asList(), Helper.extractComments(""));
//    assertEquals(Arrays.<String>asList(), Helper.extractComments("/**/"));
//    assertEquals(Arrays.<String>asList(), Helper.extractComments("/*a\nb*/"));
//    assertEquals(Arrays.<String>asList("a"), Helper.extractComments("//a\n"));
//  }

  @Test
  public void testFoo() {
    assertFoo("", "");
    assertFoo("", " ");
    assertFoo("\n", "\n");
    assertFoo("<a>", "//a");
    assertFoo("<a>\n", "//a\n");
    assertFoo("<a>\n", " //a\n");
    assertFoo("/*a*/", "/*a*/");
    assertFoo("/*a*/", "/*a*/");
    assertFoo("/***/", "/***/");
    assertFoo("/****/", "/****/");
    assertFoo("/*\n*/",
        "/*\n" +
        "*/");
    assertFoo("/*\n*/",
        " /*\n" +
        " */");
    assertFoo("/*\n */",
        " /*\n" +
        "  */");
    assertFoo("/*\na */",
        " /*\n" +
        "a */");
    assertFoo("/* \n*/",
        " /* \n" +
        "*/");
  }

  private void assertFoo(String expected, String s) {
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

  public static class CommentParser implements FragmentParser {

    StringBuilder sb = new StringBuilder();

    @Override
    public void onNewline() {
      sb.append('\n');
    }

    @Override
    public void onComment(char c) {
      sb.append(c);
    }

    @Override
    public void onBeginComment(boolean multiline) {
      sb.append(multiline ? "/*" : "//");
    }

    @Override
    public void onEndComment(boolean multiline) {
      sb.append(multiline ? "*/" : "\n");
    }
  }

}
