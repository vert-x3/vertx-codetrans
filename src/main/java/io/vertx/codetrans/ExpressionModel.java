package io.vertx.codetrans;

import io.vertx.codegen.TypeInfo;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ExpressionModel extends CodeModel {

  final Lang lang;

  public ExpressionModel(Lang lang) {
    this.lang = lang;
  }

  public ExpressionModel as(TypeInfo type) {
    switch (type.getKind()) {
      case JSON_OBJECT:
        return new JsonObjectModel(lang, this);
      case JSON_ARRAY:
        return new JsonArrayModel(lang, this);
      case DATA_OBJECT:
        return new DataObjectModel(lang, this);
      case MAP:
        return new MapModel(lang, this);
      default:
        return this;
    }
  }

  public ExpressionModel onMethodInvocation(TypeInfo receiverType, MethodRef method, TypeInfo returnType,
                                            List<ExpressionModel> argumentModels, List<TypeInfo> argumenTypes) {
    if (method.getName().equals("equals") && method.getParameterTypes().size() == 1) {
      return lang.render(writer -> {
        writer.renderEquals(ExpressionModel.this, argumentModels.get(0));
      });
    } else {
      return new MethodInvocationModel(lang, ExpressionModel.this, receiverType, method,
          returnType, argumentModels, argumenTypes);
    }
  }

  public ExpressionModel onField(String identifier) {
    return lang.render((renderer) -> {
      renderer.renderMemberSelect(ExpressionModel.this, identifier);
    });
  }

  public ExpressionModel onMethodReference(String methodName) {
    return lang.render((renderer) -> {
      renderer.renderMethodReference(ExpressionModel.this, methodName);
    });
  }

  public ExpressionModel onNew(TypeInfo type, List<ExpressionModel> arguments) {
    return lang.render((renderer) -> {
      renderer.renderNew(ExpressionModel.this, type, arguments);
    });
  }

  public ExpressionModel onPostFixIncrement() {
    return lang.render((renderer) -> {
      renderer.renderPostfixIncrement(ExpressionModel.this);
    });
  }

  public ExpressionModel onPrefixIncrement() {
    return lang.render((renderer) -> {
      renderer.renderPrefixIncrement(ExpressionModel.this, renderer);
    });
  }

  public ExpressionModel onPostFixDecrement() {
    return lang.render((renderer) -> {
      renderer.renderPostfixDecrement(ExpressionModel.this);
    });
  }

  public ExpressionModel onPrefixDecrement() {
    return lang.render((renderer) -> {
      renderer.renderPrefixDecrement(ExpressionModel.this);
    });
  }

  public ExpressionModel onLogicalComplement() {
    return lang.render((renderer) -> {
      renderer.renderLogicalComplement(ExpressionModel.this);
    });
  }

  public ExpressionModel unaryMinus() {
    return lang.render((renderer) -> {
      renderer.renderUnaryMinus(ExpressionModel.this);
    });
  }

  public ExpressionModel unaryPlus() {
    return lang.render((renderer) -> {
      renderer.renderUnaryPlus(ExpressionModel.this);
    });
  }

}
