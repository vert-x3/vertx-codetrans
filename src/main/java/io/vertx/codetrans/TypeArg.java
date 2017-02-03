package io.vertx.codetrans;

import io.vertx.codegen.type.TypeInfo;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TypeArg {

  public final TypeInfo value;
  public final boolean resolved;

  public TypeArg(TypeInfo value, boolean resolved) {
    this.value = value;
    this.resolved = resolved;
  }
}
