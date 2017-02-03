package generics;

import io.vertx.codetrans.GenericsTest;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.support.MethodReceiver;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Generics {

  @CodeTranslate
  public void callParameterizedMethodWithTypeArgument() throws Exception {
    MethodReceiver.<String>parameterizedMethod(s -> {
      GenericsTest.obj = s;
    });
  }

  @CodeTranslate
  public void callParameterizedMethodWithDefaultTypeArgument() throws Exception {
    MethodReceiver.parameterizedMethod(o -> {
      GenericsTest.obj = "" + o;
    });
  }
}
