package asyncresult;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.file.OpenOptions;
import io.vertx.examples.AsyncResultTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AsyncResultFailed extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    vertx.fileSystem().truncate("----", 0, res -> {
      AsyncResultTest.setCause(res.cause(), res.failed());
    });
  }
}
