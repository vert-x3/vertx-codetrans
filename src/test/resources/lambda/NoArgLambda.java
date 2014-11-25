package lambda;

import io.vertx.core.AbstractVerticle;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.LambdaTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class NoArgLambda extends AbstractVerticle {

  @Override
  @CodeTranslate
  public void start() throws Exception {
    LambdaTest.callback(() -> {
      LambdaTest.count();
    });
  }
}
