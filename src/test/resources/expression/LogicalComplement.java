package expression;

import io.vertx.codetrans.UnaryOperatorExpressionTest;
import io.vertx.codetrans.annotations.CodeTranslate;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class LogicalComplement {

  @CodeTranslate
  public void start() throws Exception {
    UnaryOperatorExpressionTest.result3 = !(true);
  }
}
