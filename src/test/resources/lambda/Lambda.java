package lambda;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.LambdaTest;
import io.vertx.support.HandlerInvoker;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Lambda {

  @CodeTranslate
  public void oneArg() throws Exception {
    LambdaTest.invoke((obj) -> {
      LambdaTest.o = obj;
    });
  }

  @CodeTranslate
  public void noArg() throws Exception {
    LambdaTest.callback(() -> {
      LambdaTest.count();
    });
  }

  @CodeTranslate
  public void invokeStringHandler() throws Exception {
    HandlerInvoker.invokeStringHandler(s -> {
          LambdaTest.o = s;
        }
    );
  }

  @CodeTranslate
  public void invokeStringHandlerFirstParam() throws Exception {
    HandlerInvoker.invokeStringHandlerFirstParam(s -> {
      LambdaTest.o = s;
    }
        , "the_other_value");
  }

  @CodeTranslate
  public void invokeStringHandlerLastParamBlock() throws Exception {
    // Block
    HandlerInvoker.invokeStringHandlerLastParam("the_other_value", s -> {
      LambdaTest.o = s;
    });
  }

  @CodeTranslate
  public void invokeStringHandlerLastParamExpression() throws Exception {
    // Expression
    HandlerInvoker.invokeStringHandlerLastParam("the_other_value", s -> LambdaTest.o = s);
  }
}
