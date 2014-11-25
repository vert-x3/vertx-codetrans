package options;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JKSOptions;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.OptionsTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class NestedOptions extends AbstractVerticle {

  @Override
  @CodeTranslate
  public void start() throws Exception {
    OptionsTest.options = new HttpServerOptions().setKeyStoreOptions(new JKSOptions().setPath("/mystore.jks").setPassword("secret"));
  }
}
