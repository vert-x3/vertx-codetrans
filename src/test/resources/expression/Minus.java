package expression;

import io.vertx.codetrans.BinaryOperatorExpressionTest;
import io.vertx.codetrans.annotations.CodeTranslate;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Minus {

  @CodeTranslate
  public void start() throws Exception {
    BinaryOperatorExpressionTest.numResult = 2 - 3;
  }
}
