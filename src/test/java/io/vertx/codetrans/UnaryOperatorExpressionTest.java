package io.vertx.codetrans;

import io.vertx.codetrans.lang.groovy.GroovyLang;
import io.vertx.codetrans.lang.js.JavaScriptLang;
import io.vertx.codetrans.lang.ruby.RubyLang;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class UnaryOperatorExpressionTest extends ConversionTestBase {

  public static Object result;
  public static Object result2;

  @Before
  public void before() {
    result = result2 = null;
  }

  @Test
  public void testLogicalComplement() throws Exception {
    runAll("expression/LogicalComplement", () -> {
      assertEquals(false, result);
    });
  }

  @Test
  public void testUnaryMinus() throws Exception {
    runAll("expression/UnaryMinus", () -> {
      assertEquals(-4, ((Number) result).intValue());
    });
  }

  @Test
  public void testUnaryPlus() throws Exception {
    runAll("expression/UnaryPlus", () -> {
      assertEquals(4, ((Number) result).intValue());
    });
  }

  @Test
  public void testPostfixIncrement() throws Exception {
    run(new GroovyLang(), "expression/PostfixIncrement");
    assertEquals(3, ((Number) result).intValue());
    assertEquals(4, ((Number) result2).intValue());
    run(new JavaScriptLang(), "expression/PostfixIncrement");
    assertEquals(3, ((Number) result).intValue());
    assertEquals(4, ((Number) result2).intValue());
    run(new RubyLang(), "expression/PostfixIncrement");
    assertEquals(4, ((Number) result).intValue()); // YEAH!
    assertEquals(4, ((Number) result2).intValue());
  }

  @Test
  public void testPostfixDecrement() throws Exception {
    run(new GroovyLang(), "expression/PostfixDecrement");
    assertEquals(3, ((Number) result).intValue());
    assertEquals(2, ((Number) result2).intValue());
    run(new JavaScriptLang(), "expression/PostfixDecrement");
    assertEquals(3, ((Number) result).intValue());
    assertEquals(2, ((Number) result2).intValue());
    run(new RubyLang(), "expression/PostfixDecrement");
    assertEquals(2, ((Number) result).intValue()); // YEAH
    assertEquals(2, ((Number) result2).intValue());
  }

  @Test
  public void testPrefixIncrement() throws Exception {
    runAll("expression/PrefixIncrement", () -> {
      assertEquals(4, ((Number) result).intValue());
      assertEquals(4, ((Number) result2).intValue());
    });
  }

  @Test
  public void testPrefixDecrement() throws Exception {
    runAll("expression/PrefixDecrement", () -> {
      assertEquals(2, ((Number) result).intValue());
      assertEquals(2, ((Number) result2).intValue());
    });
  }
}
