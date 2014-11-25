package json;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.examples.AsyncResultTest;
import io.vertx.examples.CodeTrans;
import io.vertx.examples.JsonTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsonArrayAddNumber extends AbstractVerticle {

  @Override
  @CodeTrans
  public void start() throws Exception {
    JsonTest.o = new JsonArray().add(8080);
  }
}
