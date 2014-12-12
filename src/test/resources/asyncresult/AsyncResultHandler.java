package asyncresult;

import io.vertx.codetrans.AsyncResultTest;
import io.vertx.codetrans.annotations.CodeTranslate;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AsyncResultHandler {

  @CodeTranslate
  public void failed() throws Exception {
    AsyncResultTest.callbackWithFailure(res -> {
      AsyncResultTest.setCause(res.cause(), res.failed());
    });
  }

  @CodeTranslate
  public void succeeded() throws Exception {
    AsyncResultTest.callbackWithSuccess(res -> {
      AsyncResultTest.setResult(res.result(), res.succeeded());
    });
  }
}
