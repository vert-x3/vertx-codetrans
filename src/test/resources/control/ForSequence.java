package control;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.ControlTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ForSequence {

  @CodeTranslate
  public void start() throws Exception {
    for (int i = 0;i < 10;i++) {
      ControlTest.invoke("" + i);
    }
  }
}
