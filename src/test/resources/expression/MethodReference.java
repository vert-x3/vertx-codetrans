package expression;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.core.buffer.Buffer;
import io.vertx.codetrans.MethodExpressionTest;
import io.vertx.support.HandlerInvoker;

import java.util.function.Consumer;


/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MethodReference {

  @CodeTranslate
  public void methodReference() throws Exception {
    Buffer buffer = Buffer.buffer("hello");
    HandlerInvoker.invokeStringHandler(buffer::appendString);
    MethodExpressionTest.helloworld = buffer.toString("UTF-8");
  }

  @CodeTranslate
  public void thisMethodReference() {
    HandlerInvoker.invokeStringHandler(this::appendString);
  }

  @CodeTranslate
  public void thisMethodReferenceNotLast() {
    HandlerInvoker.invokeStringHandlerFirstParam(this::appendString, "the_last_value");
  }

  private void appendString(String s) {
    Buffer buffer = Buffer.buffer("hello");
    buffer.appendString(s);
    MethodExpressionTest.helloworld = buffer.toString("UTF-8");
  }
}
