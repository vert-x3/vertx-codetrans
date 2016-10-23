package expression;

import io.vertx.codetrans.LiteralExpressionTest;
import io.vertx.codetrans.annotations.CodeTranslate;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class LiteralChar {

  @CodeTranslate
  public void start() throws Exception {
    LiteralExpressionTest.charresult = 'a';
  }
}
