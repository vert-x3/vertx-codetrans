package classexpression;

import io.vertx.core.AbstractVerticle;
import io.vertx.examples.ClassExpressionTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class InvokeStaticMethodVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    ClassExpressionTest.noArg();
    ClassExpressionTest.arg("foo");
  }
}
