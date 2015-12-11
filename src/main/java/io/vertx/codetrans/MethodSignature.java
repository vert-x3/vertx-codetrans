package io.vertx.codetrans;

import io.vertx.codegen.type.TypeInfo;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MethodSignature {

  final String name;
  final List<TypeInfo> parameterTypes;
  final boolean varargs;

  public MethodSignature(String name, List<TypeInfo> parameterTypes, boolean varargs) {
    this.name = name;
    this.parameterTypes = parameterTypes;
    this.varargs = varargs;
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
      return name.equals(that.name) && parameterTypes.equals(that.parameterTypes) && varargs == that.varargs;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return name.hashCode() + 31 * (parameterTypes.hashCode() + (31 * (varargs ? 1 : 0)));
  }

  @Override
  public String toString() {
    return "MethodSignature[name=" + name + ",parameters=" + parameterTypes + ",varargs=" + varargs + "]";
  }
}
