package io.vertx.codetrans;

import io.vertx.codetrans.lang.js.JavaScriptLang;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test checking that the generated JavaScript code is compliant with the ECMA specification.
 */
public class JavaScriptSyntaxTest {

  @Test
  public void testThatConditionalStructuresDoNotHaveTrailingSemiColon() throws Exception {
    JavaScriptLang lang = new JavaScriptLang();

    // For Loop
    Script script = ConversionTestBase.script(lang, "control/ForLoop", "start");
    Assert.assertFalse("Unnecessary semicolon after for loop", script.getSource().contains("};"));

    // For Each - it's not a conditional structure, but worth testing.
    script = ConversionTestBase.script(lang, "control/ForEach", "start");
    Assert.assertFalse("Unnecessary semicolon after for each loop", script.getSource().contains("};"));
    Assert.assertTrue("Check embedded lamdba closing for each loop", script.getSource().contains("});"));

    // If Then Else
    script = ConversionTestBase.script(lang, "control/Conditional", "evalThen");
    Assert.assertFalse("Unnecessary semicolon after if - then - else structure", script.getSource().contains("};"));

    script = ConversionTestBase.script(lang, "control/Conditional", "skipThen");
    Assert.assertFalse("Unnecessary semicolon after if - then - else structure", script.getSource().contains("};"));

    script = ConversionTestBase.script(lang, "control/Conditional", "evalThenSkipElse");
    Assert.assertFalse("Unnecessary semicolon after if - then - else structure", script.getSource().contains("};"));

    script = ConversionTestBase.script(lang, "control/Conditional", "skipThenEvalElse");
    Assert.assertFalse("Unnecessary semicolon after if - then - else structure", script.getSource().contains("};"));

    script = ConversionTestBase.script(lang, "control/Conditional", "evalThenSkipElseIfSkipElse");
    Assert.assertFalse("Unnecessary semicolon after if - then - else structure", script.getSource().contains("};"));

    script = ConversionTestBase.script(lang, "control/Conditional", "skipThenEvalElseIfSkipElse");
    Assert.assertFalse("Unnecessary semicolon after if - then - else structure", script.getSource().contains("};"));

    script = ConversionTestBase.script(lang, "control/Conditional", "skipThenSkipElseIfEvalElse");
    Assert.assertFalse("Unnecessary semicolon after if - then - else structure", script.getSource().contains("};"));
  }

}
