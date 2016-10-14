package expression;

import io.vertx.codetrans.BinaryOperatorExpressionTest;
import io.vertx.codetrans.annotations.CodeTranslate;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class And {

  @CodeTranslate
  public void start() {
    BinaryOperatorExpressionTest.numResult = 3 & 6;
  }
}
