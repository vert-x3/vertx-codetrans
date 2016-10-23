package expression;

import io.vertx.codetrans.LiteralExpressionTest;
import io.vertx.codetrans.annotations.CodeTranslate;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class LiteralBoolean {

  @CodeTranslate
  public void falseValue() throws Exception {
    LiteralExpressionTest.bool = false;
  }

  @CodeTranslate
  public void trueValue() throws Exception {
    LiteralExpressionTest.bool = true;
  }
}
