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

  public static Object result;

  @Before
  public void before() {
    result = null;
  }

  @Test
  public void testLiteralString() throws Exception {
    runAll("expression/LiteralString", "value", () -> {
      assertEquals("foobar", result);
    });
    runAll("expression/LiteralString", "concat", () -> {
      assertEquals("_3_", result.toString());
    });
    runAll("expression/LiteralString", "concat1", () -> {
      assertEquals("1_", result.toString());
    });
    runAll("expression/LiteralString", "concat2", () -> {
      assertEquals("3_", result.toString());
    });
    // Can't pass in Ruby : a + ("#{b}_") -> Fixnum
    run(new GroovyLang(), "expression/LiteralString", "concat3");
    assertEquals("3_", result.toString());
    run(new JavaScriptLang(), "expression/LiteralString", "concat3");
    assertEquals("3_", result.toString());
    // Can't pass in Ruby
    run(new GroovyLang(), "expression/LiteralString", "concat4");
    assertEquals("12_", result.toString());
    run(new JavaScriptLang(), "expression/LiteralString", "concat4");
    assertEquals("12_", result.toString());
    runAll("expression/LiteralString", "concat5", () -> {
      assertEquals("_1", result.toString());
    });
    runAll("expression/LiteralString", "concat6", () -> {
      assertEquals("_12", result.toString());
    });
    // Can't pass in Ruby : ("_#{a}") + b -> Fixnum
    run(new GroovyLang(), "expression/LiteralString", "concat7");
    assertEquals("_12", result.toString());
    run(new JavaScriptLang(), "expression/LiteralString", "concat7");
    assertEquals("_12", result.toString());
    runAll("expression/LiteralString", "concat8", () -> {
      assertEquals("_3", result.toString());
    });
    runAll("expression/LiteralString", "concat9", () -> {
      assertEquals("_2_", result.toString());
    });
    runAll("expression/LiteralString", "concat10", () -> {
      assertEquals("\n2", result.toString());
    });
    String expected = "\n\r\t\f\b\"\\'\u0000\u0041\u007F";
    runAll("expression/LiteralString", "escape", () -> {
      assertEquals(expected, result.toString());
    });
    for (Lang lang : langs()) {
      CodeWriter writer = lang.codeBuilder().newWriter();
      writer.renderChars(expected);
      assertEquals("\\n\\r\\t\\f\\b\\\"\\\\'\\u0000A\\u007F", writer.getBuffer().toString());
    }
  }

  @Test
  public void testLiteralChar() throws Exception {
    runAll("expression/LiteralChar", () -> {
      assertEquals("a", result);
    });
  }

  @Test
  public void testLiteralNull() throws Exception {
    runAll("expression/LiteralNull", () -> {
      assertEquals(null, result);
    });
  }

  @Test
  public void testLiteralInteger() throws Exception {
    runAll("expression/LiteralInteger", "positiveValue", () -> {
      assertEquals(4, ((Number)result).intValue());
    });
    runAll("expression/LiteralInteger", "negativeValue", () -> {
      assertEquals(-4,  ((Number)result).intValue());
    });
  }

  @Test
  public void testLiteralLong() throws Exception {
    runAll("expression/LiteralLong", "positiveValue", () -> {
      assertEquals(4L, ((Number)result).longValue());
    });
    runAll("expression/LiteralLong", "negativeValue", () -> {
      assertEquals(-4L, ((Number)result).longValue());
    });
  }

  @Test
  public void testLiteralBoolean() throws Exception {
    runAll("expression/LiteralBoolean", "trueValue", () -> {
      assertEquals(true, result);
    });
    runAll("expression/LiteralBoolean", "falseValue", () -> {
      assertEquals(false, result);
    });
  }

  @Test
  public void testLiteralFloat() throws Exception {
    runAll("expression/LiteralFloat", "positiveValue", () -> {
      assertEquals(4.0f, ((Number)result).floatValue(), 0.001);
    });
    runAll("expression/LiteralFloat", "negativeValue", () -> {
      assertEquals(-4.0f, ((Number)result).floatValue(), 0.001);
    });
  }

  @Test
  public void testLiteralDouble() throws Exception {
    runAll("expression/LiteralDouble", "positiveValue", () -> {
      assertEquals(4.0d, ((Number)result).doubleValue(), 0.001);
    });
    runAll("expression/LiteralDouble", "negativeValue", () -> {
      assertEquals(-4.0d, ((Number)result).doubleValue(), 0.001);
    });
  }

  @Test
  public void testEnumConstant() {
    runJavaScript("expression/LiteralEnum", "enumConstant");
    assertTrue(result instanceof String);
    assertEquals("THE_CONSTANT", result);
    runGroovy("expression/LiteralEnum", "enumConstant");
    assertEquals(TheEnum.THE_CONSTANT, result);
    runRuby("expression/LiteralEnum", "enumConstant");
    assertEquals("THE_CONSTANT", ((RubySymbol) result).asJavaString());
  }

  @Test
  public void testEnumConstantInString() {
    runJavaScript("expression/LiteralEnum", "enumConstantInString");
    assertTrue(result instanceof String);
    assertEquals("->THE_CONSTANT<-", result);
    runGroovy("expression/LiteralEnum", "enumConstantInString");
    assertTrue(result instanceof GString);
    assertEquals("->THE_CONSTANT<-", result.toString());
    runRuby("expression/LiteralEnum", "enumConstantInString");
    assertTrue(result instanceof String);
    assertEquals("->THE_CONSTANT<-", result);
  }
}
