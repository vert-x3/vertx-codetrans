package expression;

import io.vertx.codetrans.UnaryOperatorExpressionTest;
import io.vertx.codetrans.annotations.CodeTranslate;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class PrefixDecrement {

  @CodeTranslate
  public void start() throws Exception {
    int a = 3;
    UnaryOperatorExpressionTest.result = --a;
    UnaryOperatorExpressionTest.result2 = a;
  }
}
