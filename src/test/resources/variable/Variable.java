package variable;

import io.vertx.core.AbstractVerticle;
import io.vertx.examples.VariableTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Variable extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    String o = VariableTest.constant;
    String b;
    b = o;
    VariableTest.o = b;
  }
}
