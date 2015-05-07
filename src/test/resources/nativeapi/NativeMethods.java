package nativeapi;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.NativeApiTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class NativeMethods {

  @CodeTranslate
  public void objectEquals() throws Exception {
    Object o = NativeApiTest.o;
    NativeApiTest.isEquals = o.equals(o);
  }

  @CodeTranslate
  public void stringEquals() throws Exception {
    NativeApiTest.isEquals = "abc".equals("abc");
  }

  @CodeTranslate
  public void stringNotEquals() throws Exception {
    NativeApiTest.isEquals = "abc".equals("def");
  }

  @CodeTranslate
  public void stringStartsWithTrue() throws Exception {
    String s = "foobar";
    NativeApiTest.isEquals = s.startsWith("foo");
  }

  @CodeTranslate
  public void stringStartsWithFalse() throws Exception {
    String s = "foobar";
    NativeApiTest.isEquals = s.startsWith("bar");
  }
}
