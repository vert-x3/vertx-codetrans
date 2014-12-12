package expression;

import io.vertx.codetrans.ClassIdentifierExpressionTest;
import io.vertx.codetrans.annotations.CodeTranslate;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ClassIdentifier {

  @CodeTranslate
  public void accessStaticField() throws Exception {
    ClassIdentifierExpressionTest.field = "foo";
  }

  @CodeTranslate
  public void invokeStaticMethod() throws Exception {
    ClassIdentifierExpressionTest.noArg();
    ClassIdentifierExpressionTest.arg("foo");
  }
}
