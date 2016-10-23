package expression;

import io.vertx.codetrans.LiteralExpressionTest;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.support.TheEnum;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class LiteralEnum {

  @CodeTranslate
  public void enumConstant() throws Exception {
    LiteralExpressionTest.enumresult = TheEnum.THE_CONSTANT;
  }

  @CodeTranslate
  public void enumConstantInString() throws Exception {
    LiteralExpressionTest.string = "->" + TheEnum.THE_CONSTANT + "<-";
  }
}
