package lambda;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.LambdaTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Lambda {

  @CodeTranslate
  public void oneArg() throws Exception {
    LambdaTest.invoke((obj) -> {
      LambdaTest.o = obj;
    });
  }

  @CodeTranslate
  public void noArg() throws Exception {
    LambdaTest.callback(() -> {
      LambdaTest.count();
    });
  }
}
