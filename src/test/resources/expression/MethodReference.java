package expression;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.core.buffer.Buffer;
import io.vertx.codetrans.MethodExpressionTest;
import io.vertx.support.CallbackProvider;

import java.util.function.Consumer;


/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MethodReference {

  @CodeTranslate
  public void lambdaisation() throws Exception {
    Buffer buffer = Buffer.buffer("hello");
    CallbackProvider.callbackWithString(buffer::appendString);
    MethodExpressionTest.helloworld = buffer.toString("UTF-8");
  }
}
