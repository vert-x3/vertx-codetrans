package interrop;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.InterropTest;
import java.text.DateFormat;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class FieldInterrop {

  @CodeTranslate
  public void readConstant() throws Exception {
    InterropTest.numericValue = DateFormat.SHORT;
  }
}
