package io.vertx.codetrans;

import io.vertx.codetrans.lang.groovy.GroovyLang;
import io.vertx.codetrans.lang.js.JavaScriptLang;
import io.vertx.codetrans.lang.ruby.RubyLang;
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
      script(new GroovyLang(), "throwing/Throwing", "throwRuntimeExceptionNoArg").run();
      fail();
    } catch (RuntimeException e) {
      assertEquals(null, e.getMessage());
    }
    try {
      script(new JavaScriptLang(), "throwing/Throwing", "throwRuntimeExceptionNoArg").run();
      fail();
    } catch (ScriptException e) {
    }
    try {
      script(new RubyLang(), "throwing/Throwing", "throwRuntimeExceptionNoArg").run();
      fail();
    } catch (EvalFailedException e) {
    }
  }

  @Test
  public void testThrowRuntimeExceptionStringArg() throws Exception {
    try {
      script(new GroovyLang(), "throwing/Throwing", "throwRuntimeExceptionStringArg").run();
      fail();
    } catch (RuntimeException e) {
      assertEquals("foobar", e.getMessage());
    }
    try {
      script(new JavaScriptLang(), "throwing/Throwing", "throwRuntimeExceptionStringArg").run();
      fail();
    } catch (ScriptException e) {
      assertTrue(e.getMessage().contains("foobar"));
    }
    try {
      script(new RubyLang(), "throwing/Throwing", "throwRuntimeExceptionStringArg").run();
      fail();
    } catch (EvalFailedException e) {
      assertTrue(e.getMessage().contains("foobar"));
    }
  }
}
