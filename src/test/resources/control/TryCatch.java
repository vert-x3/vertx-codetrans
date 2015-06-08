package control;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.ControlTest;
import io.vertx.support.HandlerInvoker;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TryCatch {

  @CodeTranslate
  public void catchRuntimeException() throws Exception {
    try {
      ControlTest.throwRuntimeException();
    } catch (Exception e) {
      ControlTest.o = "caught";
    }
  }

  @CodeTranslate
  public void catchCheckedException() throws Exception {
    try {
      ControlTest.throwCheckedException();
    } catch (Exception e) {
      ControlTest.o = "caught";
    }
  }
}
