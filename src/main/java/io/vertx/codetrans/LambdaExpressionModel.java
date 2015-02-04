package io.vertx.codetrans;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.TypeInfo;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class LambdaExpressionModel extends ExpressionModel {

  private final LambdaExpressionTree.BodyKind bodyKind;
  private final List<TypeInfo> parameterTypes;
  private final List<String> parameterNames;
  private final CodeModel body;

  public LambdaExpressionModel(LambdaExpressionTree.BodyKind bodyKind, List<TypeInfo> parameterTypes, List<String> parameterNames, CodeModel body) {
    this.bodyKind = bodyKind;
    this.parameterTypes = parameterTypes;
    this.parameterNames = parameterNames;
    this.body = body;
  }

  public LambdaExpressionTree.BodyKind getBodyKind() {
    return bodyKind;
  }

  public List<TypeInfo> getParameterTypes() {
    return parameterTypes;
  }

  public List<String> getParameterNames() {
    return parameterNames;
  }

  public CodeModel getBody() {
    return body;
  }

  @Override
  public void render(CodeWriter writer) {
    writer.getLang().renderLambda(bodyKind, parameterTypes, parameterNames, body, writer);
  }
}
