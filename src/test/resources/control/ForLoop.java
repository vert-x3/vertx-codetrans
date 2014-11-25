package control;

import io.vertx.core.AbstractVerticle;
import io.vertx.examples.CodeTrans;
import io.vertx.examples.ControlTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ForLoop extends AbstractVerticle {

  @Override
  @CodeTrans
  public void start() throws Exception {
    for (int i = 0;i < 10;i++) {
      ControlTest.invoke("" + i);
    }
  }
}
