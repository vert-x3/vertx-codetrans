package control;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.ControlTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Return {

  @CodeTranslate
  public void returnVoid() throws Exception {
    returningVoid();
  }

  void returningVoid() {
    boolean a = true;
    if (a) {
      return;
    }
    ControlTest.o = "not_returned";
  }

  @CodeTranslate
  public void returnValue() throws Exception {
    ControlTest.o = returningValue();
  }

  String returningValue() {
    return "the_returned_value";
  }
}
