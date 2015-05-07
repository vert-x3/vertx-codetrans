package object;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.ObjectTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Equals {

  @CodeTranslate
  public void objectEquals() throws Exception {
    Object o = ObjectTest.o;
    ObjectTest.isEquals = o.equals(o);
  }

  @CodeTranslate
  public void stringEquals() throws Exception {
    ObjectTest.isEquals = "abc".equals("abc");
  }

  @CodeTranslate
  public void stringNotEquals() throws Exception {
    ObjectTest.isEquals = "abc".equals("def");
  }
}
