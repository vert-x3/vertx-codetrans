package throwable;

import io.vertx.codetrans.CustomException;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.ThrowableTest;

import java.net.BindException;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Throwable {

  @CodeTranslate
  public void throwRuntimeExceptionNoArg() throws Exception {
    throw new RuntimeException();
  }

  @CodeTranslate
  public void throwRuntimeExceptionStringArg() throws Exception {
    throw new RuntimeException("foobar");
  }

  @CodeTranslate
  public void instanceOf() throws Exception {
    java.lang.Throwable t = ThrowableTest.t;
    ThrowableTest.bool = t instanceof BindException;
  }

  @CodeTranslate
  public void field() throws Exception {
    CustomException custom = ThrowableTest.custom;
    ThrowableTest.test = custom.getCode();
  }
}
