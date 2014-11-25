package parenthesized;

import io.vertx.core.AbstractVerticle;
import io.vertx.examples.CodeTrans;
import io.vertx.examples.ObjectTest;
import io.vertx.examples.ParenthesizedTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Parenthesized extends AbstractVerticle {

  @Override
  @CodeTrans
  public void start() throws Exception {
    ParenthesizedTest.a = (3 + 2) * 3;
    ParenthesizedTest.b = 3 + (2 * 3);
  }
}
