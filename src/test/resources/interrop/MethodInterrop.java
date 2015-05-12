package interrop;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.InterropTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MethodInterrop {

  @CodeTranslate
  public void objectEquals() throws Exception {
    Object o = InterropTest.o;
    InterropTest.isEquals = o.equals(o);
  }

  @CodeTranslate
  public void stringEquals() throws Exception {
    InterropTest.isEquals = "abc".equals("abc");
  }

  @CodeTranslate
  public void stringNotEquals() throws Exception {
    InterropTest.isEquals = "abc".equals("def");
  }

  @CodeTranslate
  public void stringStartsWithTrue() throws Exception {
    String s = "foobar";
    InterropTest.isEquals = s.startsWith("foo");
  }

  @CodeTranslate
  public void stringStartsWithFalse() throws Exception {
    String s = "foobar";
    InterropTest.isEquals = s.startsWith("bar");
  }
}
