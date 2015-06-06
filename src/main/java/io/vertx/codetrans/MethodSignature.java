package io.vertx.codetrans;

import io.vertx.codegen.TypeInfo;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MethodSignature {

  final String name;
  final List<TypeInfo> parameterTypes;

  public MethodSignature(String name, List<TypeInfo> parameterTypes) {
    this.name = name;
    this.parameterTypes = parameterTypes;
  }

  public String getName() {
    return name;
  }

  public List<TypeInfo> getParameterTypes() {
    return parameterTypes;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof MethodSignature) {
      MethodSignature that = (MethodSignature) obj;
      return name.equals(that.name) && parameterTypes.equals(that.parameterTypes);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return name.hashCode() + 31 * parameterTypes.hashCode();
  }

  @Override
  public String toString() {
    return "MethodRef[name=" + name + ",parameters=" + parameterTypes + "]";
  }
}
