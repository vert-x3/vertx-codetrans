package io.vertx.codetrans.expression;

import io.vertx.codegen.type.ClassTypeInfo;
import io.vertx.codegen.type.TypeInfo;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.MethodSignature;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class DataObjectLiteralModel extends ExpressionModel {

  private final ClassTypeInfo type;
  private final Map<String, Member> members;

  public DataObjectLiteralModel(CodeBuilder builder, ClassTypeInfo type) {
    this(builder, type, Collections.emptyMap());
  }

  private DataObjectLiteralModel(CodeBuilder builder, ClassTypeInfo type, Map<String, Member> members) {
    super(builder);
    this.type = type;
    this.members = members;
  }

  public ClassTypeInfo getType() {
    return type;
  }

  public Iterable<Member> getMembers() {
    return members.values();
  }

  @Override
  public ExpressionModel onMethodInvocation(TypeInfo receiverType, MethodSignature method, TypeInfo returnType, List<TypeInfo> typeArguments, List<ExpressionModel> argumentModels, List<TypeInfo> argumentTypes) {
    String methodName = method.getName();
    if (isSet(methodName)) {
      if (argumentModels.size() == 1) {
        String name = unwrapSet(methodName);
        Map<String, Member> copy = new LinkedHashMap<>(members);
        ExpressionModel value = argumentModels.get(0).toDataObjectValue();
        Member.Single member = (Member.Single) copy.computeIfAbsent(name, Member.Single::new);
        member.append(value);
        copy.put(name, member);
        return new DataObjectLiteralModel(builder, type, copy);
      } else {
        throw unsupported("Method " + method + " must be invoked with a single argument argument");
      }
    } else if (isAdd(methodName)) {
      if (argumentModels.size() == 1) {
        String name = unwrapSet(methodName) + "s";
        Map<String, Member> copy = new LinkedHashMap<>(members);
        ExpressionModel value = argumentModels.get(0).toDataObjectValue();
        Member.Sequence member = (Member.Sequence) copy.computeIfAbsent(name, Member.Sequence::new);
        member.append(value);
        copy.put(name, member);
        return new DataObjectLiteralModel(builder, type, copy);
      } else if (argumentModels.size() == 2 && argumentTypes.get(0).getName().equals("java.lang.String")) {
        String name = unwrapSet(methodName) + "s";
        Map<String, Member> copy = new LinkedHashMap<>(members);
        ExpressionModel key = argumentModels.get(0);
        if (!(key instanceof StringLiteralModel)) {
          throw new UnsupportedOperationException("Must use a string literal in a key/value adder");
        }
        ExpressionModel value = argumentModels.get(1).toDataObjectValue();
        Member.Entries member = (Member.Entries) copy.computeIfAbsent(name, Member.Entries::new);
        member.append(((StringLiteralModel)key).getValue(), value);
        copy.put(name, member);
        return new DataObjectLiteralModel(builder, type, copy);
      } else {
        throw unsupported("Method " + method + " must be invoked with a single argument or with a key/value argument");
      }
    } else {
      throw unsupported("Method " + method);
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
    writer.renderDataObject(this);
  }

  static boolean isGet(String identifier) {
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

  static boolean isSet(String identifier) {
    return identifier.startsWith("set") && identifier.length() > 3;
  }

  static String unwrapSet(String identifier) {
    return Character.toLowerCase(identifier.charAt(3)) + identifier.substring(4);
  }

  static boolean isAdd(String identifier) {
    return identifier.startsWith("add") && identifier.length() > 3;
  }

  static String unwrapAdd(String identifier) {
    return Character.toLowerCase(identifier.charAt(3)) + identifier.substring(4) + "s"; // 's' for plural
  }
}
