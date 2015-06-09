package io.vertx.codetrans;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.TypeInfo;
import io.vertx.codetrans.expression.ApiTypeModel;
import io.vertx.codetrans.expression.AsyncResultModel;
import io.vertx.codetrans.expression.BinaryExpressionModel;
import io.vertx.codetrans.expression.EnumExpressionModel;
import io.vertx.codetrans.expression.ExpressionModel;
import io.vertx.codetrans.expression.VariableScope;
import io.vertx.codetrans.expression.IdentifierModel;
import io.vertx.codetrans.expression.ThisModel;
import io.vertx.codetrans.statement.StatementModel;

import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public interface CodeBuilder {

  CodeWriter newWriter();

  String render(RunnableCompilationUnit unit);

  default ExpressionModel combine(ExpressionModel left, String op, ExpressionModel right) {
    return new BinaryExpressionModel(this, left, op, right);
  }

  default ExpressionModel asyncResult(String identifier, TypeInfo type) {
    return new AsyncResultModel(this, identifier, type);
  }

  ExpressionModel asyncResultHandler(LambdaExpressionTree.BodyKind bodyKind, TypeInfo.Parameterized resultType, String resultName, CodeModel body);

  default ApiTypeModel apiType(TypeInfo.Class.Api type) {
    return new ApiTypeModel(this, type);
  }

  default EnumExpressionModel enumType(TypeInfo.Class.Enum type) {
    return new EnumExpressionModel(this, type);
  }

  default ExpressionModel identifier(String name, VariableScope scope) {
    return new IdentifierModel(this, name, scope);
  }

  StatementModel variableDecl(VariableScope scope, TypeInfo type, String name, ExpressionModel initializer);

  StatementModel enhancedForLoop(String variableName, ExpressionModel expression, StatementModel body);

  StatementModel forLoop(StatementModel initializer, ExpressionModel condition, ExpressionModel update, StatementModel body);

  default ExpressionModel jsonArrayEncoder(ExpressionModel expression) {
    return render(writer -> {
      writer.renderJsonArrayToString(expression);
    });
  }

  default ExpressionModel jsonObjectEncoder(ExpressionModel expression) {
    return render(writer -> {
      writer.renderJsonObjectToString(expression);
    });
  }

  //

  default ExpressionModel thisModel() {
    return new ThisModel(this);
  }

  default ExpressionModel forConditionalExpression(ExpressionModel condition, ExpressionModel trueExpression, ExpressionModel falseExpression) {
    return render((renderer) -> {
      renderer.renderConditionalExpression(condition, trueExpression, falseExpression);
    });
  }

  default ExpressionModel forAssign(ExpressionModel variable, ExpressionModel expression) {
    return render((renderer) -> {
      renderer.renderAssign(variable, expression);
    });
  }

  default ExpressionModel render(Consumer<CodeWriter> c) {
    CodeBuilder builder = this;
    return new ExpressionModel(builder) {
      @Override
      public void render(CodeWriter writer) {
        c.accept(writer);
      }
    };
  }
}
