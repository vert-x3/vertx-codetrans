package io.vertx.codetrans;

import io.vertx.codegen.TypeInfo;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsonArrayModel extends ExpressionModel {

  private static final ExpressionModel CLASS_MODEL = forNew(args -> {
    switch (args.size()) {
      case 0:
        return new JsonArrayModel(Collections.emptyList());
      default:
        throw new UnsupportedOperationException();
    }
  });

  public static ExpressionModel classModel() {
    return CLASS_MODEL;
  }

  private List<ExpressionModel> values;

  public JsonArrayModel(List<ExpressionModel> values) {
    this.values = values;
  }

  public List<ExpressionModel> getValues() {
    return values;
  }

  @Override
  public ExpressionModel onMethodInvocation(TypeInfo returnType, String methodName, List<ExpressionModel> arguments) {
    switch (methodName) {
      case "add":
        return new JsonArrayModel(Helper.append(values, arguments.get(0)));
      default:
        throw new UnsupportedOperationException("Method " + methodName + " not yet implemented");
    }
  }

  @Override
  public void render(CodeWriter writer) {
    writer.getLang().renderJsonArray(this, writer);
  }
}
