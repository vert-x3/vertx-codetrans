package io.vertx.codetrans;

import org.jruby.embed.EvalFailedException;
import org.junit.Test;

import javax.script.ScriptException;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ThrowingTest extends ConversionTestBase {

  @Test
  public void testThrowRuntimeExceptionNoArg() throws Exception {
    try {
      callable(new GroovyLang(), "throwing/Throwing", "throwRuntimeExceptionNoArg").call();
      fail();
    } catch (RuntimeException e) {
      assertEquals(null, e.getMessage());
    }
    try {
      callable(new JavaScriptLang(), "throwing/Throwing", "throwRuntimeExceptionNoArg").call();
      fail();
    } catch (ScriptException e) {
    }
    try {
      callable(new RubyLang(), "throwing/Throwing", "throwRuntimeExceptionNoArg").call();
      fail();
    } catch (EvalFailedException e) {
    }
  }

  @Test
  public void testThrowRuntimeExceptionStringArg() throws Exception {
    try {
      callable(new GroovyLang(), "throwing/Throwing", "throwRuntimeExceptionStringArg").call();
      fail();
    } catch (RuntimeException e) {
      assertEquals("foobar", e.getMessage());
    }
    try {
      callable(new JavaScriptLang(), "throwing/Throwing", "throwRuntimeExceptionStringArg").call();
      fail();
    } catch (ScriptException e) {
      assertTrue(e.getMessage().contains("foobar"));
    }
    try {
      callable(new RubyLang(), "throwing/Throwing", "throwRuntimeExceptionStringArg").call();
      fail();
    } catch (EvalFailedException e) {
      assertTrue(e.getMessage().contains("foobar"));
    }
  }
}
