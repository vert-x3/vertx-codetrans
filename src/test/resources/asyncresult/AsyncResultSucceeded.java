package asyncresult;

import io.vertx.core.AbstractVerticle;
import io.vertx.examples.AsyncResultTest;
import io.vertx.examples.CodeTrans;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AsyncResultSucceeded extends AbstractVerticle {

  @Override
  @CodeTrans
  public void start() throws Exception {
    AsyncResultTest.callbackWithSuccess(res -> {
      AsyncResultTest.setResult(res.result(), res.succeeded());
    });
  }
}
