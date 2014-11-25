package string;

import io.vertx.core.AbstractVerticle;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.StringTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class StringEscape extends AbstractVerticle {

  @Override
  @CodeTranslate
  public void start() throws Exception {
    StringTest.o = "\n\"\\'";
  }
}
