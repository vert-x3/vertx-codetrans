package expression;

import io.vertx.codetrans.MethodExpressionTest;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.support.SubHandler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MethodInvocation {

  @CodeTranslate
  public void instanceSelectInvocation() throws Exception {
    Runnable counter = MethodExpressionTest.counter;
    counter.run();
  }

  @CodeTranslate
  public void classSelectInvocation() throws Exception {
    MethodExpressionTest.count();
  }

  @CodeTranslate
  public void thisInvocation() throws Exception {
    someMethod();
  }

  public void someMethod() {
    MethodExpressionTest.count();
  }

  @CodeTranslate
  public void thisInvocationWithParam() throws Exception {
    someMethodWithParam("the_arg");
  }

  public void someMethodWithParam(String s) {
    MethodExpressionTest.state = s;
  }

  @CodeTranslate
  public void instanceHandlerSubtypeArgument() throws Exception {
    SubHandler handler = SubHandler.create();
    handler.instanceHandler(handler);
  }

  @CodeTranslate
  public void classHandlerSubtypeArgument() throws Exception {
    SubHandler handler = SubHandler.create();
    SubHandler.classHandler(handler);
  }

  @CodeTranslate
  public void invokeNullArgument() throws Exception {
    MethodExpressionTest.checkNull(null);
  }

  @CodeTranslate
  public void invokeMethodWithByteReturn() {
    someMethodWithByteReturn();
  }

  public byte someMethodWithByteReturn() {
    MethodExpressionTest.count();
    return 1;
  }

  @CodeTranslate
  public void invokeMethodWithBoxedByteReturn() {
    someMethodWithBoxedByteReturn();
  }

  public Byte someMethodWithBoxedByteReturn() {
    MethodExpressionTest.count();
    return 1;
  }

  @CodeTranslate
  public void invokeMethodWithShortReturn() {
    someMethodWithShortReturn();
  }

  public short someMethodWithShortReturn() {
    MethodExpressionTest.count();
    return 1;
  }

  @CodeTranslate
  public void invokeMethodWithBoxedShortReturn() {
    someMethodWithBoxedShortReturn();
  }

  public Short someMethodWithBoxedShortReturn() {
    MethodExpressionTest.count();
    return 1;
  }

  @CodeTranslate
  public void invokeMethodWithIntReturn() {
    someMethodWithIntReturn();
  }

  public int someMethodWithIntReturn() {
    MethodExpressionTest.count();
    return 1;
  }

  @CodeTranslate
  public void invokeMethodWithIntegerReturn() {
    someMethodWithIntegerReturn();
  }

  public Integer someMethodWithIntegerReturn() {
    MethodExpressionTest.count();
    return 1;
  }

  @CodeTranslate
  public void invokeMethodWithLongReturn() {
    someMethodWithLongReturn();
  }

  public long someMethodWithLongReturn() {
    MethodExpressionTest.count();
    return 1L;
  }

  @CodeTranslate
  public void invokeMethodWithBoxedLongReturn() {
    someMethodWithBoxedLongReturn();
  }

  public Long someMethodWithBoxedLongReturn() {
    MethodExpressionTest.count();
    return 1L;
  }

  @CodeTranslate
  public void invokeMethodWithFloatReturn() {
    someMethodWithFloatReturn();
  }

  public float someMethodWithFloatReturn() {
    MethodExpressionTest.count();
    return 1.0F;
  }

  @CodeTranslate
  public void invokeMethodWithBoxedFloatReturn() {
    someMethodWithBoxedFloatReturn();
  }

  public Float someMethodWithBoxedFloatReturn() {
    MethodExpressionTest.count();
    return 1.0F;
  }

  @CodeTranslate
  public void invokeMethodWithDoubleReturn() {
    someMethodWithDoubleReturn();
  }

  public double someMethodWithDoubleReturn() {
    MethodExpressionTest.count();
    return 1.0D;
  }

  @CodeTranslate
  public void invokeMethodWithBoxedDoubleReturn() {
    someMethodWithBoxedDoubleReturn();
  }

  public Double someMethodWithBoxedDoubleReturn() {
    MethodExpressionTest.count();
    return 1.0D;
  }

  @CodeTranslate
  public void invokeMethodWithCharReturn() {
    someMethodWithCharReturn();
  }

  public char someMethodWithCharReturn() {
    MethodExpressionTest.count();
    return 'c';
  }

  @CodeTranslate
  public void invokeMethodWithCharacterReturn() {
    someMethodWithCharacterReturn();
  }

  public Character someMethodWithCharacterReturn() {
    MethodExpressionTest.count();
    return 'c';
  }

  @CodeTranslate
  public void invokeMethodWithBooleanReturn() {
    someMethodWithBooleanReturn();
  }

  public boolean someMethodWithBooleanReturn() {
    MethodExpressionTest.count();
    return false;
  }

  @CodeTranslate
  public void invokeMethodWithBoxedBooleanReturn() {
    someMethodWithBoxedBooleanReturn();
  }

  public Boolean someMethodWithBoxedBooleanReturn() {
    MethodExpressionTest.count();
    return false;
  }
}
