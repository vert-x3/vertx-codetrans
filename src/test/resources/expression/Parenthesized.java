package expression;

import io.vertx.codetrans.ParenthesizedExpressionTest;
import io.vertx.codetrans.annotations.CodeTranslate;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Parenthesized {

  @CodeTranslate
  public void start() throws Exception {
    ParenthesizedExpressionTest.result = (3 + 2) * 3;
    ParenthesizedExpressionTest.result2 = 3 + (2 * 3);
  }
}
