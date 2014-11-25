package classexpression;

import io.vertx.core.AbstractVerticle;
import io.vertx.examples.ClassExpressionTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AccessStaticFieldVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    ClassExpressionTest.field = "foo";
  }
}
