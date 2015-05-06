package expression;

import io.vertx.codetrans.MethodExpressionTest;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.support.MethodReceiver;
import io.vertx.support.SubHandler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MethodNaming {

  @CodeTranslate
  public void booleanApiGetter() throws Exception {
    boolean b = MethodReceiver.isRed();
  }

  @CodeTranslate
  public void booleanApiMethod() throws Exception {
    boolean b = MethodReceiver.blue();
  }
}
