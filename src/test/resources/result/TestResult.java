package result;

import io.vertx.codetrans.annotations.CodeTranslate;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestResult {

  @CodeTranslate
  public void sourceResult() {
    System.out.println("abc");
  }

  @CodeTranslate
  public void unsupportedResult() {
    for (;;) {
    }
  }
}
