package control;

import io.vertx.core.AbstractVerticle;
import io.vertx.examples.annotations.CodeTranslate;
import io.vertx.examples.ControlTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ForEach extends AbstractVerticle {

  @Override
  @CodeTranslate
  public void start() throws Exception {
    for (String s : ControlTest.list) {
      ControlTest.invoke(s);
    }
  }
}
