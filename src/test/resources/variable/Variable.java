package variable;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.VariableTest;
import io.vertx.core.AbstractVerticle;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Variable extends AbstractVerticle {

  @CodeTranslate
  public void declare() throws Exception {
    String o = VariableTest.constant;
    String b;
    b = o;
    VariableTest.o = b;
  }

  @CodeTranslate
  public void globalExpression() throws Exception {
    VariableTest.o = vertx;
  }

  private String member = "member_value";

  @CodeTranslate
  public void memberExpression() throws Exception {
    VariableTest.o = member;
  }

  @CodeTranslate
  public void memberExpressionAccessedByMethod() throws Exception {
    accessMember();
  }

  private void accessMember() {
    VariableTest.o = member;
  }
}
