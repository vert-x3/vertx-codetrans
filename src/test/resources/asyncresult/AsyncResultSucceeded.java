package asyncresult;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.file.OpenOptions;
import io.vertx.examples.AsyncResultTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AsyncResultSucceeded extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    vertx.fileSystem().open(AsyncResultTest.path, new OpenOptions(), res -> {
      AsyncResultTest.setResult(res.result(), res.succeeded());
    });
  }
}
