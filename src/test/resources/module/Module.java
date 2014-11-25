package module;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.examples.CodeTrans;
import io.vertx.examples.ModuleTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Module extends AbstractVerticle {

  @Override
  @CodeTrans
  public void start() throws Exception {
    ModuleTest.buffer = Buffer.buffer("the_buffer");
    ModuleTest.toString = Buffer.buffer("the_buffer").toString("UTF-8");
  }
}
