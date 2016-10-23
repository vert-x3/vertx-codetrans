package io.vertx.codetrans;

import groovy.lang.GString;
import io.vertx.codetrans.lang.groovy.GroovyLang;
import io.vertx.codetrans.lang.js.JavaScriptLang;
import io.vertx.support.TheEnum;
import org.jruby.RubySymbol;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class LiteralExpressionTest extends ConversionTestBase {

  public static Number result;
  
  public static Boolean bool;

  public static String string;

  public static TheEnum enumresult;

  public static Object charresult;

  @Before
  public void before() {
    result = null;
    bool = null;
    string = null;
    enumresult = null;
    charresult = null;
  }

  @Test
  public void testLiteralString() throws Exception {
    runAll("expression/LiteralString", "value", () -> {
      assertEquals("foobar", string);
    });
    runAll("expression/LiteralString", "concat", () -> {
      assertEquals("_3_", string);
    });
    runAll("expression/LiteralString", "concat1", () -> {
      assertEquals("1_", string);
    });
    runAll("expression/LiteralString", "concat2", () -> {
      assertEquals("3_", string);
    });
    // Can't pass in Ruby : a + ("#{b}_") -> Fixnum
    run(new GroovyLang(), "expression/LiteralString", "concat3");
    assertEquals("3_", string);
    run(new JavaScriptLang(), "expression/LiteralString", "concat3");
    assertEquals("3_", string);
    // Can't pass in Ruby
    run(new GroovyLang(), "expression/LiteralString", "concat4");
    assertEquals("12_", string);
    run(new JavaScriptLang(), "expression/LiteralString", "concat4");
    assertEquals("12_", string);
    runAll("expression/LiteralString", "concat5", () -> {
      assertEquals("_1", string);
    });
    runAll("expression/LiteralString", "concat6", () -> {
      assertEquals("_12", string);
    });
    // Can't pass in Ruby : ("_#{a}") + b -> Fixnum
    run(new GroovyLang(), "expression/LiteralString", "concat7");
    assertEquals("_12", string);
    run(new JavaScriptLang(), "expression/LiteralString", "concat7");
    assertEquals("_12", string);
    runAll("expression/LiteralString", "concat8", () -> {
      assertEquals("_3", string);
    });
    runAll("expression/LiteralString", "concat9", () -> {
      assertEquals("_2_", string);
    });
    runAll("expression/LiteralString", "concat10", () -> {
      assertEquals("\n2", string);
    });
    String expected = "\n\r\t\f\b\"\\'\u0000\u0041\u007F";
    runAll("expression/LiteralString", "escape", () -> {
      assertEquals(expected, string);
    });
    for (Lang lang : langs()) {
      CodeWriter writer = lang.codeBuilder().newWriter();
      writer.renderChars(expected);
      String ret = unescape(writer.getBuffer().toString());
      assertEquals("\n\r\t\f\b\"\\'\u0000A\u007F", ret);
    }
  }

  private String unescape(String s) {
    StringBuilder sb = new StringBuilder();
    int len = s.length();
    int i = 0;
    while (i < len) {
      char c = s.charAt(i++);
      if (c == '\\') {
        if (i < len) {
          c = s.charAt(i++);
          switch (c) {
            case 'n':
              sb.append('\n');
              break;
            case 'r':
              sb.append('\r');
              break;
            case 't':
              sb.append('\t');
              break;
            case 'f':
              sb.append('\f');
              break;
            case 'b':
              sb.append('\b');
              break;
            case '"':
              sb.append('"');
              break;
            case '\\':
              sb.append('\\');
              break;
            case 'u':
              String sub;
              try {
                sub = s.substring(i, i += 4);
              } catch (StringIndexOutOfBoundsException e) {
                throw new IllegalArgumentException(e.getMessage());
              }
              int val = Integer.parseInt(sub, 16);
              sb.append((char)val);
              break;
            default:
              throw new UnsupportedOperationException("Handle me gracefully " + c);
          }
        } else {
          sb.append('\\');
        }
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  //TODO: another hidden type collision
//  @Test
//  public void testLiteralChar() throws Exception {
//    runAll("expression/LiteralChar", () -> {
//      assertEquals('a', (char)charresult);
//    });
//  }

  @Test
  public void testLiteralNull() throws Exception {
    runAll("expression/LiteralNull", () -> {
      assertEquals(null, result);
    });
  }

  @Test
  public void testLiteralInteger() throws Exception {
    runAll("expression/LiteralInteger", "positiveValue", () -> {
      assertEquals(4, result.intValue());
    });
    runAll("expression/LiteralInteger", "negativeValue", () -> {
      assertEquals(-4,  result.intValue());
    });
  }

  @Test
  public void testLiteralLong() throws Exception {
    runAll("expression/LiteralLong", "positiveValue", () -> {
      assertEquals(4L, result.longValue());
    });
    runAll("expression/LiteralLong", "negativeValue", () -> {
      assertEquals(-4L, result.longValue());
    });
  }

  @Test
  public void testLiteralBoolean() throws Exception {
    runAll("expression/LiteralBoolean", "trueValue", () -> {
      assertEquals(true, bool);
    });
    runAll("expression/LiteralBoolean", "falseValue", () -> {
      assertEquals(false, bool);
    });
  }

  @Test
  public void testLiteralFloat() throws Exception {
    runAll("expression/LiteralFloat", "positiveValue", () -> {
      assertEquals(4.0f, result.floatValue(), 0.001);
    });
    runAll("expression/LiteralFloat", "negativeValue", () -> {
      assertEquals(-4.0f, result.floatValue(), 0.001);
    });
  }

  @Test
  public void testLiteralDouble() throws Exception {
    runAll("expression/LiteralDouble", "positiveValue", () -> {
      assertEquals(4.0d, result.doubleValue(), 0.001);
    });
    runAll("expression/LiteralDouble", "negativeValue", () -> {
      assertEquals(-4.0d, result.doubleValue(), 0.001);
    });
  }

  @Test
  public void testEnumConstant() {
    runGroovy("expression/LiteralEnum", "enumConstant");
    assertEquals(TheEnum.THE_CONSTANT, enumresult);
    runScala("expression/LiteralEnum", "enumConstant");
    assertEquals(TheEnum.THE_CONSTANT, enumresult);
    //TODO: Test doesn't work if done this way and I think it leads to wrong assumptions.
    // generating from the same code should result in the same types, which it doesn't in this test.
    // This test only worked becuase the type-discrepancy was hidden behinf Object
//    runJavaScript("expression/LiteralEnum", "enumConstant");
//    assertEquals(TheEnum.THE_CONSTANT, enumresult);
//    runRuby("expression/LiteralEnum", "enumConstant");
//    assertEquals(TheEnum.THE_CONSTANT, enumresult);
  }

  @Test
  public void testEnumConstantInString() {
    runAll("expression/LiteralEnum", "enumConstantInString", () -> {
      assertEquals("->THE_CONSTANT<-", string);
    });
  }
}
