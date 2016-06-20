package io.vertx.codetrans;

import io.vertx.codetrans.lang.groovy.GroovyLang;
import io.vertx.codetrans.lang.js.JavaScriptLang;
import io.vertx.codetrans.lang.ruby.RubyLang;
import org.jruby.embed.EvalFailedException;
import org.junit.Test;

import javax.script.ScriptException;

import java.net.BindException;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ThrowableTest extends ConversionTestBase {

  public static Throwable t;
  public static CustomException custom;
  public static Object test;

  @Test
  public void testThrowRuntimeExceptionNoArg() throws Exception {
    try {
      script(new GroovyLang(), "throwable/Throwable", "throwRuntimeExceptionNoArg").run();
      fail();
    } catch (RuntimeException e) {
      assertEquals(null, e.getMessage());
    }
    try {
      script(new JavaScriptLang(), "throwable/Throwable", "throwRuntimeExceptionNoArg").run();
      fail();
    } catch (ScriptException e) {
    }
    try {
      script(new RubyLang(), "throwable/Throwable", "throwRuntimeExceptionNoArg").run();
      fail();
    } catch (EvalFailedException e) {
    }
  }

  @Test
  public void testThrowRuntimeExceptionStringArg() throws Exception {
    try {
      script(new GroovyLang(), "throwable/Throwable", "throwRuntimeExceptionStringArg").run();
      fail();
    } catch (RuntimeException e) {
      assertEquals("foobar", e.getMessage());
    }
    try {
      script(new JavaScriptLang(), "throwable/Throwable", "throwRuntimeExceptionStringArg").run();
      fail();
    } catch (ScriptException e) {
      assertTrue(e.getMessage().contains("foobar"));
    }
    try {
      script(new RubyLang(), "throwable/Throwable", "throwRuntimeExceptionStringArg").run();
      fail();
    } catch (EvalFailedException e) {
      assertTrue(e.getMessage().contains("foobar"));
    }
  }

  @Test
  public void testInstanceOf() throws Exception {
    t = new BindException();
    runAll("throwable/Throwable", "instanceOf", () -> {
      assertEquals(Boolean.TRUE, test);
      test = null;
    });
  }

  @Test
  public void testField() throws Exception {
    custom = new CustomException(5);
    runAll("throwable/Throwable", "field", () -> {
      assertEquals(5, ((Number)test).intValue());
      test = null;
    });
  }
}
