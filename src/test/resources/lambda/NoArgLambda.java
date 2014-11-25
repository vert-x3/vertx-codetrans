package lambda;

import io.vertx.core.AbstractVerticle;
import io.vertx.examples.ClassExpressionTest;
import io.vertx.examples.CodeTrans;
import io.vertx.examples.LambdaTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class NoArgLambda extends AbstractVerticle {

  @Override
  @CodeTrans
  public void start() throws Exception {
    LambdaTest.callback(() -> {
      LambdaTest.count();
    });
  }
}
