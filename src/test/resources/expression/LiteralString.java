package expression;

import io.vertx.codetrans.LiteralExpressionTest;
import io.vertx.codetrans.annotations.CodeTranslate;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class LiteralString {

  @CodeTranslate
  public void value() throws Exception {
    LiteralExpressionTest.result = "foobar";
  }

  @CodeTranslate
  public void concat() throws Exception {
    int a = 3;
    LiteralExpressionTest.result = "_" + a + "_";
  }

  @CodeTranslate
  public void escape() throws Exception {
    LiteralExpressionTest.result = "\n\"\\'";
  }
}
