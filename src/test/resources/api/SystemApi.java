package api;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.ControlTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SystemApi {

  @CodeTranslate
  public void outPrintln() throws Exception {
    System.out.println("hello");
  }

  @CodeTranslate
  public void errPrintln() throws Exception {
    System.err.println("hello");
  }
}
