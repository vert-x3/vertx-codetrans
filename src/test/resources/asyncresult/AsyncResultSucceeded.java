package asyncresult;

import io.vertx.core.AbstractVerticle;
import io.vertx.codetrans.AsyncResultTest;
import io.vertx.codetrans.annotations.CodeTranslate;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AsyncResultSucceeded extends AbstractVerticle {

  @Override
  @CodeTranslate
  public void start() throws Exception {
    AsyncResultTest.callbackWithSuccess(res -> {
      AsyncResultTest.setResult(res.result(), res.succeeded());
    });
  }
}
