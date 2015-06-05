package io.vertx.codetrans;

import io.vertx.codegen.TypeInfo;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class EnumExpressionModel extends ExpressionModel {

  private final TypeInfo.Class.Enum type;

  public EnumExpressionModel(Lang lang, TypeInfo.Class.Enum type) {
    super(lang);
    this.type = type;
  }

  public TypeInfo getType() {
    return type;
  }

  @Override
  public ExpressionModel onField(String identifier) {
    return lang.render(writer -> {
      writer.renderEnumConstant(type, identifier);
    });
  }
}
