package json;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.JsonTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsonObjectPutNumber extends AbstractVerticle {

  @Override
  @CodeTranslate
  public void start() throws Exception {
    JsonTest.o = new JsonObject().put("port", 8080);
  }
}
