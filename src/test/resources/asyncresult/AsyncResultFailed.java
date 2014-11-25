package asyncresult;

import io.vertx.core.AbstractVerticle;
import io.vertx.examples.AsyncResultTest;
import io.vertx.examples.CodeTrans;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AsyncResultFailed extends AbstractVerticle {

  @Override
  @CodeTrans
  public void start() throws Exception {
    AsyncResultTest.callbackWithFailure(res -> {
      AsyncResultTest.setCause(res.cause(), res.failed());
    });
  }
}
