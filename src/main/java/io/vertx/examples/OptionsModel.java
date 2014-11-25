package io.vertx.examples;

import io.vertx.codegen.TypeInfo;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class OptionsModel extends ExpressionModel {

  public static ExpressionModel create(TypeInfo.Class type) {
    return ExpressionModel.forNew(args -> new OptionsModel(type));
  }

  private final TypeInfo.Class type;
  private final Map<String, Member> members;

  private OptionsModel(TypeInfo.Class type) {
    this(type, Collections.emptyMap());
  }

  private OptionsModel(TypeInfo.Class type, Map<String, Member> members) {
    this.type = type;
    this.members = members;
  }

  public TypeInfo.Class getType() {
    return type;
  }

  public Iterable<Member> getMembers() {
    return members.values();
  }

  @Override
  public ExpressionModel onMemberSelect(String identifier) {
    String name;
    Function<String, Member> memberFactory;
    if (identifier.length() > 3 && identifier.startsWith("set")) {
      name = Character.toLowerCase(identifier.charAt(3)) + identifier.substring(4);
      memberFactory = $ -> new Member.Single(ExpressionModel.render(name));
    } else if (identifier.length() > 3 && identifier.startsWith("add")) {
      name = Character.toLowerCase(identifier.charAt(3)) + identifier.substring(4) + "s"; // 's' for plural
      memberFactory = $ -> new Member.Array(ExpressionModel.render(name));
    } else {
      throw new UnsupportedOperationException("Not implemented");
    }
    return new ExpressionModel() {
      @Override
      public ExpressionModel onMethodInvocation(List<ExpressionModel> arguments) {
        if (arguments.size() == 1) {
          Map<String, Member> copy = new LinkedHashMap<>(members);
          ExpressionModel value = arguments.get(0);
          Member member = copy.computeIfAbsent(name, memberFactory);
          member.append(value);
          copy.put(name, member);
          return new OptionsModel(type, copy);
        } else {
          throw new UnsupportedOperationException("not yet implemented");
        }
      }
    };
  }

  public void render(CodeWriter writer) {
    writer.getLang().renderOptions(this, writer);
  }
}
