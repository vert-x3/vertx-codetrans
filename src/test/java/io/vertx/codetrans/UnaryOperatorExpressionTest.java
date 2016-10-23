package io.vertx.codetrans;

import io.vertx.codetrans.lang.groovy.GroovyLang;
import io.vertx.codetrans.lang.js.JavaScriptLang;
import io.vertx.codetrans.lang.ruby.RubyLang;
import io.vertx.codetrans.lang.scala.ScalaLang;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class UnaryOperatorExpressionTest extends ConversionTestBase {

  public static Number result;
  public static Number result2;
  public static Boolean result3;

  @Before
  public void before() {
    result = result2 = 0;
    result3 = null;
  }

  @Test
  public void testLogicalComplement() throws Exception {
    runAll("expression/LogicalComplement", () -> {
      assertEquals(false, result3);
    });
  }

  @Test
  public void testUnaryMinus() throws Exception {
    runAll("expression/UnaryMinus", () -> {
      assertEquals(-4, result.intValue());
    });
  }

  @Test
  public void testUnaryPlus() throws Exception {
    runAll("expression/UnaryPlus", () -> {
      assertEquals(4, result.intValue());
    });
  }

  @Test
  public void testPostfixIncrement() throws Exception {
    run(new GroovyLang(), "expression/PostfixIncrement");
    assertEquals(3, result.intValue());
    assertEquals(4, result2.intValue());
    run(new JavaScriptLang(), "expression/PostfixIncrement");
    assertEquals(3, result.intValue());
    assertEquals(4, result2.intValue());
    run(new RubyLang(), "expression/PostfixIncrement");
    assertEquals(4, result.intValue()); // YEAH!
    assertEquals(4, result2.intValue());
    //There is no pre/postic increment in Scala
  }

  @Test
  public void testPostfixDecrement() throws Exception {
    run(new GroovyLang(), "expression/PostfixDecrement");
    assertEquals(3, result.intValue());
    assertEquals(2, result2.intValue());
    run(new JavaScriptLang(), "expression/PostfixDecrement");
    assertEquals(3, result.intValue());
    assertEquals(2, result2.intValue());
    run(new RubyLang(), "expression/PostfixDecrement");
    assertEquals(2, result.intValue()); // YEAH
    assertEquals(2, result2.intValue());
    //There is no pre/postic decrement in Scala
  }

  @Test
  public void testPrefixIncrement() throws Exception {
    runAllExcept("expression/PrefixIncrement", ScalaLang.class, () -> {
      assertEquals(4, result.intValue());
      assertEquals(4, result2.intValue());
    });
  }

  @Test
  public void testPrefixDecrement() throws Exception {
    runAllExcept("expression/PrefixDecrement", ScalaLang.class, () -> {
      assertEquals(2, result.intValue());
      assertEquals(2, result2.intValue());
    });
  }
}
