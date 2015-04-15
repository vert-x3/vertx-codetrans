package variable;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.VariableTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Variable {

  @CodeTranslate
  public void declare() throws Exception {
    String o = VariableTest.constant;
    String b;
    b = o;
    VariableTest.o = b;
  }
}
