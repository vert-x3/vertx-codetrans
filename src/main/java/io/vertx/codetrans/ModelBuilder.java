package io.vertx.codetrans;

import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ConditionalExpressionTree;
import com.sun.source.tree.EnhancedForLoopTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.IfTree;
import com.sun.source.tree.LambdaExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.ParenthesizedTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.UnaryTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;
import io.vertx.codegen.ClassKind;
import io.vertx.codegen.TypeInfo;
import io.vertx.codetrans.annotations.MapModel;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The model builder is a tree scanner for building a code model from the Java program AST.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ModelBuilder extends TreePathScanner<CodeModel, VisitContext> {

  private final DeclaredType SystemType;
  private final TypeInfo.Factory factory;
  private final Lang lang;

  public ModelBuilder(DeclaredType systemType, TypeInfo.Factory factory, Lang lang) {
    SystemType = systemType;
    this.factory = factory;
    this.lang = lang;
  }

  public CodeModel build(TreePath path) {
    return scan(path, new VisitContext());
  }

  public StatementModel scan(StatementTree tree, VisitContext visitContext) {
    return (StatementModel) scan((Tree) tree, visitContext);
  }

  public ExpressionModel scan(ExpressionTree tree, VisitContext visitContext) {
    return (ExpressionModel) scan((Tree) tree, visitContext);
  }

  @Override
  public CodeModel visitForLoop(ForLoopTree node, VisitContext p) {
    if (node.getInitializer().size() != 1) {
      throw new UnsupportedOperationException();
    }
    if (node.getUpdate().size() != 1) {
      throw new UnsupportedOperationException();
    }
    StatementModel initializer = scan(node.getInitializer().get(0), p);
    ExpressionModel update = scan(node.getUpdate().get(0).getExpression(), p);
    StatementModel body = scan(node.getStatement(), p);
    ExpressionModel condition = scan(node.getCondition(), p);
    return lang.forLoop(initializer, condition, update, body);
  }

  @Override
  public CodeModel visitEnhancedForLoop(EnhancedForLoopTree node, VisitContext p) {
    ExpressionModel expression = scan(node.getExpression(), p);
    StatementModel body = scan(node.getStatement(), p);
    return lang.enhancedForLoop(node.getVariable().getName().toString(), expression, body);
  }

  @Override
  public CodeModel visitAssignment(AssignmentTree node, VisitContext context) {
    ExpressionModel variable = scan(node.getVariable(), context);
    ExpressionModel expression = scan(node.getExpression(), context);
    return ExpressionModel.forAssign(variable, expression);
  }

  @Override
  public StatementModel visitVariable(VariableTree node, VisitContext p) {
    JCTree.JCVariableDecl decl = (JCTree.JCVariableDecl) node;
    ExpressionModel initializer;
    if (node.getInitializer() != null) {
      initializer = scan(node.getInitializer(), p);
    } else {
      initializer = null;
    }
    TypeInfo type = factory.create(decl.type);
    return lang.variable(
        type,
        decl.name.toString(),
        initializer
    );
  }

  @Override
  public StatementModel visitIf(IfTree node, VisitContext visitContext) {
    ExpressionModel condition = scan(node.getCondition(), visitContext);
    StatementModel thenBody = scan(node.getThenStatement(), visitContext);
    StatementModel elseBody = node.getElseStatement() != null ? scan(node.getElseStatement(), visitContext) : null;
    return StatementModel.ifThenElse(condition, thenBody, elseBody);
  }

  @Override
  public CodeModel visitConditionalExpression(ConditionalExpressionTree node, VisitContext visitContext) {
    ExpressionModel condition = scan(node.getCondition(), visitContext);
    ExpressionModel trueExpression = scan(node.getTrueExpression(), visitContext);
    ExpressionModel falseExpression = scan(node.getFalseExpression(), visitContext);
    return ExpressionModel.forConditionalExpression(condition, trueExpression, falseExpression);
  }

  @Override
  public ExpressionModel visitUnary(UnaryTree node, VisitContext p) {
    ExpressionModel expression = scan(node.getExpression(), p);
    switch (node.getKind()) {
      case POSTFIX_INCREMENT:
        // Note we don't handle the case (3++) that is not legal in JavaScript
        return expression.onPostFixIncrement();
      case POSTFIX_DECREMENT:
        // Note we don't handle the case (3--) that is not legal in JavaScript
        return expression.onPostFixDecrement();
      case PREFIX_INCREMENT:
        // Note we don't handle the case (++3) that is not legal in JavaScript
        return expression.onPrefixIncrement();
      case PREFIX_DECREMENT:
        // Note we don't handle the case (--3) that is not legal in JavaScript
        return expression.onPrefixDecrement();
      case LOGICAL_COMPLEMENT:
        return expression.onLogicalComplement();
      case UNARY_MINUS:
        return expression.unaryMinus();
      case UNARY_PLUS:
        return expression.unaryPlus();
      default:
        throw new UnsupportedOperationException("Unary operator " + node.getKind().name() + " not yet implemented");
    }
  }

  @Override
  public CodeModel visitExpressionStatement(ExpressionStatementTree node, VisitContext context) {
    ExpressionModel expression = scan(node.getExpression(), context);
    return StatementModel.render(expression::render);
  }

  @Override
  public ExpressionModel visitBinary(BinaryTree node, VisitContext p) {
    ExpressionModel left = scan(node.getLeftOperand(), p);
    ExpressionModel right = scan(node.getRightOperand(), p);
    String op;
    switch (node.getKind()) {
      case CONDITIONAL_AND:
        op = "&&";
        break;
      case CONDITIONAL_OR:
        op = "||";
        break;
      case EQUAL_TO:
        op = "==";
        break;
      case NOT_EQUAL_TO:
        op = "!=";
        break;
      case PLUS:
        op = "+";
        break;
      case LESS_THAN:
        op = "<";
        break;
      case LESS_THAN_EQUAL:
        op = "<=";
        break;
      case GREATER_THAN:
        op = ">";
        break;
      case GREATER_THAN_EQUAL:
        op = ">=";
        break;
      case MULTIPLY:
        op = "*";
        break;
      case DIVIDE:
        op = "/";
        break;
      case AND:
        op = "&";
        break;
      case OR:
        op = "|";
        break;
      case XOR:
        op = "^";
        break;
      case MINUS:
        op = "-";
        break;
      case REMAINDER:
        op = "%";
        break;
      default:
        throw new UnsupportedOperationException("Binary operator " + node.getKind().name() + " not yet implemented");
    }
    return lang.combine(left, op, right);
  }

  @Override
  public ExpressionModel visitLiteral(LiteralTree node, VisitContext p) {
    switch (node.getKind()) {
      case NULL_LITERAL:
        return lang.nullLiteral();
      case STRING_LITERAL:
        return lang.stringLiteral(node.getValue().toString());
      case BOOLEAN_LITERAL:
        return ExpressionModel.render(renderer -> renderer.getLang().renderBooleanLiteral(node.getValue().toString(), renderer));
      case INT_LITERAL:
        return ExpressionModel.render(renderer -> renderer.getLang().renderIntegerLiteral(node.getValue().toString(), renderer));
      case LONG_LITERAL:
        return ExpressionModel.render(renderer -> renderer.getLang().renderLongLiteral(node.getValue().toString(), renderer));
      case CHAR_LITERAL:
        return ExpressionModel.render(renderer -> renderer.getLang().renderCharLiteral(node.getValue().toString().charAt(0), renderer));
      case FLOAT_LITERAL:
        return ExpressionModel.render(renderer -> renderer.getLang().renderFloatLiteral(node.getValue().toString(), renderer));
      case DOUBLE_LITERAL:
        return ExpressionModel.render(renderer -> renderer.getLang().renderDoubleLiteral(node.getValue().toString(), renderer));
      default:
        throw new UnsupportedOperationException("Literal " + node.getKind().name() + " not yet implemented");
    }
  }

  @Override
  public ExpressionModel visitIdentifier(IdentifierTree node, VisitContext context) {
    JCTree.JCIdent ident = (JCTree.JCIdent) node;
    if (ident.sym instanceof TypeElement) {
      if (ident.type.equals(SystemType)) {
        return ExpressionModel.forMemberSelect("out", () ->
            ExpressionModel.forMemberSelect("println", () ->
                ExpressionModel.forMethodInvocation(args -> lang.console(args.get(0)))));
      } else {
        TypeInfo.Class type = (TypeInfo.Class) factory.create(ident.type);
        if (type.getKind() == ClassKind.API) {
          return ExpressionModel.forMemberSelect((identifier) -> lang.staticFactory(type, identifier));
        } else if (type.getKind() == ClassKind.JSON_OBJECT) {
          return JsonObjectModel.classModel();
        } else if (type.getKind() == ClassKind.JSON_ARRAY) {
          return JsonArrayModel.classModel();
        } else if (type.getKind() == ClassKind.DATA_OBJECT) {
          return DataObjectModel.classModel(type);
        } else {
          return lang.classExpression(type);
        }
      }
    } else {
      ExpressionModel alias = context.getAlias(ident.sym);
      if (alias != null) {
        return alias;
      } else {
        String identifier = node.getName().toString();
        TypeInfo type = factory.create(ident.type);
        switch (type.getKind()) {
          case JSON_OBJECT:
            return JsonObjectModel.instanceModel(ExpressionModel.render(identifier));
          case DATA_OBJECT:
            return DataObjectModel.instanceModel(ExpressionModel.render(identifier), (TypeInfo.Class) type);
          case MAP:
            return new MapModel(ExpressionModel.render(identifier));
          default:
            return ExpressionModel.render(identifier);
        }
      }
    }
  }

  @Override
  public CodeModel visitNewClass(NewClassTree node, VisitContext visitContext) {
    ExpressionModel identifier = scan(node.getIdentifier(), visitContext);
    List<ExpressionModel> arguments = node.getArguments().stream().map(arg -> scan(arg, visitContext)).collect(Collectors.toList());
    return identifier.onNew(arguments);
  }

  @Override
  public CodeModel visitParenthesized(ParenthesizedTree node, VisitContext visitContext) {
    ExpressionModel expression = scan(node.getExpression(), visitContext);
    return ExpressionModel.forParenthesized(expression);
  }

  @Override
  public ExpressionModel visitMemberSelect(MemberSelectTree node, VisitContext p) {
    ExpressionModel expression = scan(node.getExpression(), p);
    return expression.onMemberSelect(node.getIdentifier().toString());
  }

  @Override
  public ExpressionModel visitMethodInvocation(MethodInvocationTree node, VisitContext p) {
    ExpressionModel methodSelect = scan(node.getMethodSelect(), p);
    List<ExpressionModel> arguments = node.getArguments().stream().map(argument -> scan(argument, p)).collect(Collectors.toList());
    return methodSelect.onMethodInvocation(arguments);
  }

  @Override
  public StatementModel visitBlock(BlockTree node, VisitContext p) {
    List<StatementModel> statements = node.getStatements().stream().map((statement) -> scan(statement, p)).collect(Collectors.toList());
    return StatementModel.block(statements);
  }

  @Override
  public ExpressionModel visitLambdaExpression(LambdaExpressionTree node, VisitContext p) {
    List<String> parameterNames = node.getParameters().stream().map(parameter -> parameter.getName().toString()).collect(Collectors.toList());
    List<TypeInfo> parameterTypes = node.getParameters().stream().
        map(parameter -> factory.create(((JCTree.JCVariableDecl) parameter).type)).
        collect(Collectors.toList());
    int size = parameterNames.size();
    if (size > 0) {
      JCTree.JCVariableDecl last = (JCTree.JCVariableDecl) node.getParameters().get(size - 1);
      if (last.vartype instanceof JCTree.JCTypeApply) {
        JCTree.JCTypeApply typeApply = (JCTree.JCTypeApply) last.vartype;
        if (typeApply.clazz instanceof JCTree.JCFieldAccess) {
          JCTree.JCFieldAccess clazz = (JCTree.JCFieldAccess) typeApply.clazz;
          Symbol.ClassSymbol sym = (Symbol.ClassSymbol) clazz.sym;
          TypeInfo type = factory.create(sym.type);
          if (type.getKind() == ClassKind.ASYNC_RESULT) {
            ExpressionModel result = lang.asyncResult(last.name.toString());
            CodeModel body = scan(node.getBody(), p.putAlias(last.sym, result));
            return lang.asyncResultHandler(node.getBodyKind(), last.name.toString(), body);
          }
        }
      }
    }
    CodeModel body = scan(node.getBody(), p);
    return lang.lambda(node.getBodyKind(), parameterTypes, parameterNames, body);
  }

  @Override
  public CodeModel visitMethod(MethodTree node, VisitContext p) {
    return scan(node.getBody(), p);
  }
}
