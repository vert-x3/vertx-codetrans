package io.vertx.codetrans;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsonArrayModel extends ExpressionModel {

  public static final ExpressionModel CLASS_MODEL = forNew(args -> {
    switch (args.size()) {
      case 0:
        return new JsonArrayModel(Collections.emptyList());
      default:
        throw new UnsupportedOperationException();
    }
  });

  private List<ExpressionModel> values;

  public JsonArrayModel(List<ExpressionModel> values) {
    this.values = values;
  }

  public List<ExpressionModel> getValues() {
    return values;
  }

  @Override
  public ExpressionModel onMemberSelect(String identifier) {
    return new ExpressionModel() {
      @Override
      public ExpressionModel onMethodInvocation(List<ExpressionModel> arguments) {
        switch (identifier) {
          case "add":
            return new JsonArrayModel(Helper.append(values, arguments.get(0)));
          default:
            throw new UnsupportedOperationException("Method " + identifier + " not yet implemented");
        }
      }
    };
  }

  @Override
  public void render(CodeWriter writer) {
    writer.getLang().renderJsonArray(this, writer);
  }
}
