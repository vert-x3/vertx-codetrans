package io.vertx.codetrans;

import io.vertx.codegen.TypeInfo;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class DataObjectModel extends ExpressionModel {

  public static ExpressionModel classModel(TypeInfo.Class type) {
    return forNew(args -> new DataObjectModel(type));
  }

  public static ExpressionModel instanceModel(ExpressionModel expression, TypeInfo.Class type) {
    return new ExpressionModel() {
      @Override
      public ExpressionModel onMethodInvocation(String methodName, List<ExpressionModel> arguments) {
        if (isSet(methodName)) {
          return ExpressionModel.render( writer -> {
            writer.getLang().renderDataObjectAssign(expression,
                ExpressionModel.render(unwrapSet(methodName)),
                arguments.get(0), writer);
          });
        }
        if (isGet(methodName)) {
          return ExpressionModel.render( writer -> {
            writer.getLang().renderDataObjectMemberSelect(expression,
                ExpressionModel.render(unwrapSet(methodName)), writer);
          });
        }
        throw new UnsupportedOperationException("TODO");
      }
      @Override
      public void render(CodeWriter writer) {
        expression.render(writer);
      }
    };
  }

  private final TypeInfo.Class type;
  private final Map<String, Member> members;

  private DataObjectModel(TypeInfo.Class type) {
    this(type, Collections.emptyMap());
  }

  private DataObjectModel(TypeInfo.Class type, Map<String, Member> members) {
    this.type = type;
    this.members = members;
  }

  public Iterable<Member> getMembers() {
    return members.values();
  }

  @Override
  public ExpressionModel onMethodInvocation(String methodName, List<ExpressionModel> arguments) {
    String name;
    Function<String, Member> memberFactory;
    if (isSet(methodName)) {
      name = unwrapSet(methodName);
      memberFactory = $ -> new Member.Single(render(name));
    } else if (isAdd(methodName)) {
      name = unwrapAdd(methodName);
      memberFactory = $ -> new Member.Array(render(name));
    } else {
      throw unsupported();
    }
    if (arguments.size() == 1) {
      Map<String, Member> copy = new LinkedHashMap<>(members);
      ExpressionModel value = arguments.get(0);
      Member member = copy.computeIfAbsent(name, memberFactory);
      member.append(value);
      copy.put(name, member);
      return new DataObjectModel(type, copy);
    } else {
      throw unsupported();
    }
  }

  @Override
  public ExpressionModel as(TypeInfo type) {
    if (!type.equals(this.type)) {
      throw new UnsupportedOperationException();
    }
    return this;
  }

  public void render(CodeWriter writer) {
    writer.getLang().renderDataObject(this, writer);
  }

  private static boolean isGet(String identifier) {
    return (identifier.startsWith("get") && identifier.length() > 3) ||
        (identifier.startsWith("is") && identifier.length() > 2);
  }

  private static String unwrapGet(String identifier) {
    if (identifier.startsWith("get")) {
      return Character.toLowerCase(identifier.charAt(3)) + identifier.substring(4);
    } else {
      return Character.toLowerCase(identifier.charAt(2)) + identifier.substring(3);
    }
  }

  private static boolean isSet(String identifier) {
    return identifier.startsWith("set") && identifier.length() > 3;
  }

  private static String unwrapSet(String identifier) {
    return Character.toLowerCase(identifier.charAt(3)) + identifier.substring(4);
  }

  private static boolean isAdd(String identifier) {
    return identifier.startsWith("add") && identifier.length() > 3;
  }

  private static String unwrapAdd(String identifier) {
    return Character.toLowerCase(identifier.charAt(3)) + identifier.substring(4) + "s"; // 's' for plural
  }
}
