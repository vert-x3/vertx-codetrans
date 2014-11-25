package io.vertx.examples;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsonObjectModel extends ExpressionModel {

  public static ExpressionModel CLASS_MODEL = ExpressionModel.forNew(args -> {
    switch (args.size()) {
      case 0:
        return new JsonObjectModel(Collections.emptyList());
      default:
        throw new UnsupportedOperationException();
    }
  });

  private final List<Member> entries;

  private JsonObjectModel(List<Member> entries) {
    this.entries = entries;
  }

  public Iterable<Member> getMembers() {
    return entries;
  }

  @Override
  public ExpressionModel onMemberSelect(String identifier) {
    return new ExpressionModel() {
      @Override
      public ExpressionModel onMethodInvocation(List<ExpressionModel> arguments) {
        switch (identifier) {
          case "put":
            return new JsonObjectModel(Helper.append(entries, new Member.Single(arguments.get(0)).append(arguments.get(1))));
          default:
            throw new UnsupportedOperationException("Method " + identifier + " not yet implemented");
        }
      }
    };
  }

  @Override
  public void render(CodeWriter writer) {
    writer.getLang().renderJsonObject(this, writer);
  }
}
