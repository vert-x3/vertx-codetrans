package variable;

import io.vertx.core.AbstractVerticle;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.VariableTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Variable extends AbstractVerticle {

  @Override
  @CodeTranslate
  public void start() throws Exception {
    String o = VariableTest.constant;
    String b;
    b = o;
    VariableTest.o = b;
  }
}
