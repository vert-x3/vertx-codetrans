package control;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.ControlTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class If {

  @CodeTranslate
  public void evalThen() throws Exception {
    if (true) {
      ControlTest.o = "inThen";
    }
  }

  @CodeTranslate
  public void skipThen() throws Exception {
    if (false) {
      ControlTest.o = "inThen";
    }
  }
}
