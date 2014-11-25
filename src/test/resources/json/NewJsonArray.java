package json;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.examples.AsyncResultTest;
import io.vertx.examples.JsonTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class NewJsonArray extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    JsonTest.o = new JsonArray();
  }
}
