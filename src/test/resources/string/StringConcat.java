package string;

import io.vertx.core.AbstractVerticle;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.StringTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class StringConcat extends AbstractVerticle {

  @Override
  @CodeTranslate
  public void start() throws Exception {
    int a = 3;
    StringTest.o = "_" + a + "_";
  }
}
