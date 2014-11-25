package parenthesized;

import io.vertx.core.AbstractVerticle;
import io.vertx.examples.annotations.CodeTranslate;
import io.vertx.examples.ParenthesizedTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Parenthesized extends AbstractVerticle {

  @Override
  @CodeTranslate
  public void start() throws Exception {
    ParenthesizedTest.a = (3 + 2) * 3;
    ParenthesizedTest.b = 3 + (2 * 3);
  }
}
