package generics;

import io.vertx.codetrans.GenericsTest;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.support.MethodReceiver;
import io.vertx.support.Parameterized;
import io.vertx.support.TypeParameterBound;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Generics {

  @CodeTranslate
  public void callParameterizedMethodWithGivenTypeArgument() throws Exception {
    GenericsTest.obj = MethodReceiver.<String>parameterizedMethodMatchingTypeVariableParameter(null);
  }

  @CodeTranslate
  public void callParameterizedMethodWithoutTypeArgument() throws Exception {
    GenericsTest.obj = MethodReceiver.parameterizedMethodMatchingTypeVariableParameter(null);
  }

  @CodeTranslate
  public void callParameterizedMethodWithTypeArgumentInferedByType() throws Exception {
    GenericsTest.obj = MethodReceiver.parameterizedMethodMatchingTypeVariableParameter("the_string");
  }

  @CodeTranslate
  public void callParameterizedMethodWithTypeArgumentInferedByTypeArgument() throws Exception {
    Parameterized<String> strings = Parameterized.create("the_value");
    GenericsTest.obj = MethodReceiver.parameterizedMethodMatchingGenericParameter(strings);
  }

  @CodeTranslate
  public void callParameterizedMethodTypeArgumentInferedByInheritedTypeArgument() throws Exception {
    TypeParameterBound strings = TypeParameterBound.create("the_value");
    GenericsTest.obj = MethodReceiver.parameterizedMethodMatchingGenericParameter(strings);
  }
}
