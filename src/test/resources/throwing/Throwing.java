package throwing;

import io.vertx.codetrans.annotations.CodeTranslate;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Throwing {

  @CodeTranslate
  public void throwRuntimeExceptionNoArg() throws Exception {
    throw new RuntimeException();
  }

  @CodeTranslate
  public void throwRuntimeExceptionStringArg() throws Exception {
    throw new RuntimeException("foobar");
  }
}
