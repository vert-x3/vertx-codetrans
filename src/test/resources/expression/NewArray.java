package expression;

import io.vertx.codetrans.ArrayTest;
import io.vertx.codetrans.annotations.CodeTranslate;

public class NewArray {

  @CodeTranslate
  public void primitiveArrayDeclare() throws Exception {
    int[] a = new int[]{1, 2};
    ArrayTest.ia = a;
  }

  @CodeTranslate
  public void arrayDeclare() throws Exception {
    String[] a = new String[]{"foo", "bar"};
    ArrayTest.sa = a;
  }

  @CodeTranslate
  public void newPrimitiveArrayExpression() throws Exception {
    ArrayTest.ia = new int[]{1, 2};
  }

  @CodeTranslate
  public void newArrayExpression() throws Exception {
    ArrayTest.sa = new String[]{"foo", "bar"};
  }
}
