package expression;

import io.vertx.codetrans.ExpressionTest;
import io.vertx.core.AbstractVerticle;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.ControlTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class PrefixDecrement extends AbstractVerticle {

  @Override
  @CodeTranslate
  public void start() throws Exception {
    int a = 3;
    ExpressionTest.result = --a;
    ExpressionTest.result2 = a;
  }
}
