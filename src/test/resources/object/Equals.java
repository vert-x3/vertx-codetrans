package object;

import io.vertx.core.AbstractVerticle;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.ObjectTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Equals extends AbstractVerticle {

  @Override
  @CodeTranslate
  public void start() throws Exception {
    Object o = ObjectTest.o;
    ObjectTest.isEquals = o.equals(o);
  }
}
