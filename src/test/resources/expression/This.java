package expression;

import io.vertx.codetrans.BinaryOperatorExpressionTest;
import io.vertx.codetrans.ThisExpressionTest;
import io.vertx.codetrans.annotations.CodeTranslate;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class This {

  @CodeTranslate
  public void evalThis() {
    ThisExpressionTest.obj = this;
  }
}
