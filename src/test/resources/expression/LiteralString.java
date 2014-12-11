package expression;

import io.vertx.codetrans.ExpressionTest;
import io.vertx.core.AbstractVerticle;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.ControlTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class LiteralString extends AbstractVerticle {

  @Override
  @CodeTranslate
  public void start() throws Exception {
    ExpressionTest.result = "foobar";
  }
}
