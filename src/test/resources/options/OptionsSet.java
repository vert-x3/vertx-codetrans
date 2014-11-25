package options;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.examples.CodeTrans;
import io.vertx.examples.OptionsTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class OptionsSet extends AbstractVerticle {

  @Override
  @CodeTrans
  public void start() throws Exception {
    OptionsTest.options = new HttpServerOptions().setPort(8080).setHost("localhost");
  }
}
