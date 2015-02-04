package io.vertx.codetrans;

import io.vertx.codegen.TypeInfo;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsonObjectModel extends ExpressionModel {

  private static ExpressionModel CLASS_MODEL = forNew(args -> {
    switch (args.size()) {
      case 0:
        return new JsonObjectModel(Collections.emptyList());
      default:
        throw new UnsupportedOperationException();
    }
  });

  public static ExpressionModel classModel() {
    return CLASS_MODEL;
  }

  public static ExpressionModel instanceModel(ExpressionModel expression) {
    return new ExpressionModel() {
      @Override
      public ExpressionModel onMethodInvocation(TypeInfo returnType, String methodName, List<ExpressionModel> arguments) {
        switch (methodName) {
          case "put":
            return ExpressionModel.render(writer -> {
              writer.getLang().renderJsonObjectAssign(expression, arguments.get(0), arguments.get(1), writer);
            });
          case "getString":
          case "getJsonObject":
          case "getInteger":
          case "getLong":
          case "getFloat":
          case "getDouble":
          case "getBoolean":
          case "getJsonArray":
          case "getValue":
            if (arguments.size() == 1) {
              return ExpressionModel.render( writer -> {
                writer.getLang().renderJsonObjectMemberSelect(expression, arguments.get(0), writer);
              });
            } else {
              throw unsupported("Invalid arguments " + arguments);
            }
          default:
            throw unsupported("Method " + methodName);
        }
      }
      @Override
      public void render(CodeWriter writer) {
        expression.render(writer);
      }
    };
  }

  private final List<Member> entries;

  private JsonObjectModel(List<Member> entries) {
    this.entries = entries;
  }

  public Iterable<Member> getMembers() {
    return entries;
  }

  @Override
  public ExpressionModel onMethodInvocation(TypeInfo returnType, String methodName, List<ExpressionModel> arguments) {
    switch (methodName) {
      case "put":
        return new JsonObjectModel(Helper.append(entries, new Member.Single(arguments.get(0)).append(arguments.get(1))));
      default:
        throw new UnsupportedOperationException("Method " + methodName + " not yet implemented");
    }
  }

  @Override
  public void render(CodeWriter writer) {
    writer.getLang().renderJsonObject(this, writer);
  }
}
