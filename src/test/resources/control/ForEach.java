package control;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.ControlTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ForEach {

  @CodeTranslate()
  public void start() throws Exception {
    for (String s : ControlTest.list) {
      ControlTest.invoke(s);
    }
  }
}
