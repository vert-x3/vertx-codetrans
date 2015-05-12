package interrop;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.InterropTest;
import java.text.DateFormat;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ConstructorInterrop {

  @CodeTranslate
  public void noArgConstructor() throws Exception {
    AtomicInteger r = new AtomicInteger();
    InterropTest.numericValue = r.incrementAndGet();
  }
}
