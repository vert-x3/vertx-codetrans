package control;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.ControlTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ForLoop {

  @CodeTranslate
  public void start() throws Exception {
    int i = 0;
    for (i = 0;i < 10;i++) {
      ControlTest.invoke("" + i);
    }
  }
}
