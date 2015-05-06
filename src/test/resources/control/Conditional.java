package control;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.ControlTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Conditional {

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

  @CodeTranslate
  public void evalThenSkipElse() throws Exception {
    if (true) {
      ControlTest.o = "inThen";
    } else {
      ControlTest.o = "inElse";
    }
  }

  @CodeTranslate
  public void skipThenEvalElse() throws Exception {
    if (false) {
      ControlTest.o = "inThen";
    } else {
      ControlTest.o = "inElse";
    }
  }
}
