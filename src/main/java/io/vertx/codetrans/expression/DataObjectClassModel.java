package io.vertx.codetrans.expression;

import io.vertx.codegen.type.ClassTypeInfo;
import io.vertx.codetrans.CodeBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class DataObjectClassModel extends ClassModel {

  final ClassTypeInfo type;

  public DataObjectClassModel(CodeBuilder builder, ClassTypeInfo type) {
    super(builder);
    this.type = type;
  }

  @Override
  public ExpressionModel onNew(List<ExpressionModel> arguments) {
    switch (arguments.size()) {
      case 0:
        return new DataObjectLiteralModel(builder, this.type);
      case 1:
        ExpressionModel em = arguments.get(0);
        if (em instanceof JsonObjectLiteralModel) {
          JsonObjectLiteralModel jsonModel = (JsonObjectLiteralModel) em;
          Map<String, Member> copy = new HashMap<>();
          jsonModel.getMembers().forEach(member -> {
            copy.put(member.name, member);
          });
          return new DataObjectLiteralModel(builder, this.type, copy);
        } else if (em instanceof JsonObjectModel) {
          JsonObjectModel jsonModel = (JsonObjectModel) em;
          return new DataObjectModel(builder, builder.render(writer -> writer.renderToDataObject(jsonModel, this.type)));
        }
        break;
    }
    throw new UnsupportedOperationException("Cannot build a data object using " + arguments + " constructor");
  }
}
