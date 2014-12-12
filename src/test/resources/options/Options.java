package options;

import io.vertx.core.http.HttpServerOptions;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.OptionsTest;
import io.vertx.core.net.JKSOptions;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Options {

  @CodeTranslate
  public void empty() throws Exception {
    OptionsTest.options = new HttpServerOptions();
  }

  @CodeTranslate
  public void nested() throws Exception {
    OptionsTest.options = new HttpServerOptions().setKeyStoreOptions(new JKSOptions().setPath("/mystore.jks").setPassword("secret"));
  }

  @CodeTranslate
  public void add() throws Exception {
    OptionsTest.options = new HttpServerOptions().addEnabledCipherSuite("foo").addEnabledCipherSuite("bar");
  }

  @CodeTranslate
  public void set() throws Exception {
    OptionsTest.options = new HttpServerOptions().setPort(8080).setHost("localhost");
  }
}
