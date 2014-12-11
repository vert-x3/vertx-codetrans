package expression;

import io.vertx.core.AbstractVerticle;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.ExpressionTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class LiteralStringEscape extends AbstractVerticle {

  @Override
  @CodeTranslate
  public void start() throws Exception {
    ExpressionTest.result = "\n\"\\'";
  }
}
