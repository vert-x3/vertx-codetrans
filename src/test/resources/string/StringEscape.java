package string;

import io.vertx.core.AbstractVerticle;
import io.vertx.examples.CodeTrans;
import io.vertx.examples.StringTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class StringEscape extends AbstractVerticle {

  @Override
  @CodeTrans
  public void start() throws Exception {
    StringTest.o = "\n\"\\'";
  }
}
