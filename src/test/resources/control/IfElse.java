package control;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.ControlTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class IfElse {

  @CodeTranslate
  public void evalThen() throws Exception {
    if (true) {
      ControlTest.o = "inThen";
    } else {
      ControlTest.o = "inElse";
    }
  }

  @CodeTranslate
  public void evalElse() throws Exception {
    if (false) {
      ControlTest.o = "inThen";
    } else {
      ControlTest.o = "inElse";
    }
  }
}
