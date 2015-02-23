package expression;

import io.vertx.codetrans.MethodInvocationExpressionTest;
import io.vertx.codetrans.annotations.CodeTranslate;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MethodInvocation {

  @CodeTranslate
  public void instanceSelectInvocation() throws Exception {
    Runnable counter = MethodInvocationExpressionTest.counter;
    counter.run();
  }

  @CodeTranslate
  public void classSelectInvocation() throws Exception {
    MethodInvocationExpressionTest.count();
  }

  @CodeTranslate
  public void instanceIdentInvocation() throws Exception {
    someMethod();
  }

  public void someMethod() {
  }
}
