package io.vertx.codetrans;

import io.vertx.codetrans.lang.ruby.RubyLang;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Thomas Segismont
 */
public class RubyPredicateTest {

  @Test
  public void testMethodWithBooleanReturn() {
    RubyLang lang = new RubyLang();

    Script script = ConversionTestBase.script(lang, "expression/MethodNaming", "booleanApiGetter");
    assertTrue("Invalid predicate name", script.getSource().contains("Support::MethodReceiver.red?()"));

    Script script1 = ConversionTestBase.script(lang, "expression/MethodNaming", "booleanApiMethod");
    assertTrue("Invalid predicate name", script1.getSource().contains("Support::MethodReceiver.blue?()"));
  }
}
