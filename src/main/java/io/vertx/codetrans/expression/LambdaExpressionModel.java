package io.vertx.codetrans.expression;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.type.TypeInfo;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeModel;
import io.vertx.codetrans.CodeWriter;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class LambdaExpressionModel extends ExpressionModel {

  private final LambdaExpressionTree.BodyKind bodyKind;
  private final List<TypeInfo> parameterTypes;
  private final List<String> parameterNames;
  private final CodeModel body;

  public LambdaExpressionModel(CodeBuilder builder, LambdaExpressionTree.BodyKind bodyKind, List<TypeInfo> parameterTypes, List<String> parameterNames, CodeModel body) {
    super(builder);
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
    writer.renderLambda(bodyKind, parameterTypes, parameterNames, body);
  }
}
