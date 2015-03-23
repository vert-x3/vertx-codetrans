package io.vertx.codetrans;

import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ConditionalExpressionTree;
import com.sun.source.tree.EnhancedForLoopTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.IfTree;
import com.sun.source.tree.LambdaExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.ParenthesizedTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.ThrowTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.UnaryTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;
import io.vertx.codegen.ClassKind;
import io.vertx.codegen.TypeInfo;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The model builder is a tree scanner for building a code model from the Java program AST.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ModelBuilder extends TreePathScanner<CodeModel, VisitContext> {

  private final Trees trees;
  private final TreePath path;
  private final DeclaredType systemType;
  private final DeclaredType throwableType;
  private final Types typeUtils;
  private final TypeInfo.Factory factory;
  private final Lang lang;

  public ModelBuilder(Trees trees, TreePath path, DeclaredType systemType, DeclaredType throwableType, TypeInfo.Factory factory, Types typeUtils, Lang lang) {
    this.path = path;
    this.trees = trees;
    this.systemType = systemType;
    this.throwableType = throwableType;
    this.factory = factory;
    this.typeUtils = typeUtils;
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
      if (ident.type.equals(systemType)) {
        return ExpressionModel.forFieldSelect("out", () ->
            ExpressionModel.forMethodInvocation("println", args -> lang.console(args.get(0)))
        );
      } else {
        if (typeUtils.isSubtype(ident.type, throwableType)) {
          return ExpressionModel.forNew(args -> {
            if (args.size() == 0) {
              return new ThrowableModel(ident.type.toString(), null);
            } else if (args.size() == 1) {
              return new ThrowableModel(ident.type.toString(), args.get(0));
            }
            throw new UnsupportedOperationException("Only empty or String throwable constructor are accepted");
          });
        }
        TypeInfo.Class type = (TypeInfo.Class) factory.create(ident.type);
        if (type.getKind() == ClassKind.API) {
          return new ExpressionModel() {
            @Override
            public ExpressionModel onMethodInvocation(String methodName, List<TypeInfo> parameterTypes, List<ExpressionModel> argumentModels, List<TypeInfo> argumenTypes) {
              return lang.staticFactory(type, methodName, parameterTypes, argumentModels, argumenTypes);
            }
          };
        } else if (type.getKind() == ClassKind.JSON_OBJECT) {
          return ExpressionModel.forNew(args -> {
            switch (args.size()) {
              case 0:
                return new JsonObjectLiteralModel();
              default:
                throw new UnsupportedOperationException();
            }
          });
        } else if (type.getKind() == ClassKind.JSON_ARRAY) {
          return ExpressionModel.forNew(args -> {
            switch (args.size()) {
              case 0:
                return new JsonArrayLiteralModel();
              default:
                throw new UnsupportedOperationException();
            }
          });
        } else if (type.getKind() == ClassKind.DATA_OBJECT) {
          return ExpressionModel.forNew(args -> new DataObjectLiteralModel(type));
        } else if (type.getKind() == ClassKind.ENUM) {
          return new EnumExpressionModel((TypeInfo.Class.Enum) type);
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
        return ExpressionModel.render(identifier).as(type);
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
  public CodeModel visitThrow(ThrowTree node, VisitContext visitContext) {
    ThrowableModel throwableExpression = (ThrowableModel) scan(node.getExpression(), visitContext);
    return StatementModel.render(writer -> {
      writer.getLang().renderThrow(throwableExpression.getType(), throwableExpression.getReason(), writer);
    });
  }

  @Override
  public CodeModel visitParenthesized(ParenthesizedTree node, VisitContext visitContext) {
    ExpressionModel expression = scan(node.getExpression(), visitContext);
    return ExpressionModel.forParenthesized(expression);
  }

  @Override
  public ExpressionModel visitMemberSelect(MemberSelectTree node, VisitContext p) {
    ExpressionModel expression = scan(node.getExpression(), p);
    TypeInfo fieldType = factory.create(((JCTree) node).type);
    ExpressionModel fieldExpression = expression.onField(node.getIdentifier().toString());
    return fieldExpression.as(fieldType);
  }


  @Override
  public CodeModel visitMemberReference(MemberReferenceTree node, VisitContext p) {
    if (node.getMode() == MemberReferenceTree.ReferenceMode.INVOKE) {
      ExpressionModel expression = scan(node.getQualifierExpression(), p);
      ExpressionModel methodReferenceExpression = expression.onMethodReference(node.getName().toString());
      return methodReferenceExpression;
    } else {
      throw new UnsupportedOperationException("New reference not implemented yet");
    }
  }

  @Override
  public ExpressionModel visitMethodInvocation(MethodInvocationTree node, VisitContext p) {
    if (node.getMethodSelect() instanceof IdentifierTree) {
      throw new UnsupportedOperationException("Invoking a method of the same class is not supported");
    }
    // Is there a case it would not be a member select expression ?
    JCTree.JCFieldAccess memberSelect = (JCTree.JCFieldAccess) node.getMethodSelect();
    ExpressionModel memberSelectExpression = scan(memberSelect.getExpression(), p);
    String methodName = memberSelect.getIdentifier().toString();
    List<ExpressionModel> argumentModels = node.getArguments().stream().map(argument -> scan(argument, p)).collect(Collectors.toList());
    TypeInfo returnType = factory.create(((JCTree) node).type);
    JCTree.JCMethodInvocation abc = (JCTree.JCMethodInvocation) node;

    // Compute the parameter types
    Symbol.MethodSymbol sym = (Symbol.MethodSymbol) memberSelect.sym;
    List<TypeInfo> parameterTypes = new ArrayList<>();
    for (Symbol.VarSymbol var : sym.getParameters()) {
      TypeInfo parameterType = factory.create(var.asType());
      parameterTypes.add(parameterType);
    }

    // Compute the argument types
    List<TypeInfo> argumentTypes = new ArrayList<>();
    for (JCTree.JCExpression argument : abc.getArguments()) {
      TypeInfo argumentType = null;
      if (argument.type.getKind() != TypeKind.NULL) {
        argumentType = factory.create(argument.type);
      }
      argumentTypes.add(argumentType);
    }

    ExpressionModel expression = memberSelectExpression.onMethodInvocation(methodName,
        parameterTypes, argumentModels, argumentTypes);
    return expression.as(returnType);
  }

  @Override
  public StatementModel visitBlock(BlockTree node, VisitContext p) {

    List<? extends StatementTree> statements = node.getStatements();
    List<StatementModel> models = statements.stream().map((statement) -> scan(statement, p)).collect(Collectors.toList());
    LinkedList<String> fragments = new LinkedList<>();

    // Read the source code
    CompilationUnitTree unit = path.getCompilationUnit();
    StringBuilder buffer = new StringBuilder();
    try(Reader reader = unit.getSourceFile().openReader(true)) {
      char[] tmp = new char[256];
      while (true) {
        int len = reader.read(tmp);
        if (len == -1) {
          break;
        }
        buffer.append(tmp, 0, len);
      }
      String source = buffer.toString();

      int blockBegin = (int) trees.getSourcePositions().getStartPosition(unit, node);
      int blockEnd = (int) trees.getSourcePositions().getEndPosition(unit, node) - 1;
      // There is a bug with lambda blocks that are not set on the '{' but on the parameter list
      // so we need to correct and move to the '{'
      while (blockBegin < blockEnd && source.charAt(blockBegin) != '{') {
        blockBegin++;
      }
      while (blockBegin < blockEnd && source.charAt(blockBegin - 1) != '\n') {
        blockBegin++;
      }
      while (blockEnd >= blockBegin && source.charAt(blockEnd - 1) != '\n') {
        blockEnd--;
      }
      int prev = blockBegin;
      for (StatementTree statement : statements) {
        int statementBegin = (int) trees.getSourcePositions().getStartPosition(unit, statement);
        while (statementBegin > prev && source.charAt(statementBegin - 1) != '\n') {
          statementBegin--;
        }
        fragments.add(source.substring(prev, statementBegin));
        int statementEnd = (int) trees.getSourcePositions().getEndPosition(unit, statement);
        while (statementEnd < blockEnd && source.charAt(statementEnd - 1) != '\n') {
          statementEnd++;
        }
        prev = statementEnd;

      }
      fragments.add(source.substring(prev, blockEnd));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return StatementModel.render(renderer -> {
      renderer.getLang().renderBlock(new BlockModel() {
        @Override
        public void render(CodeWriter writer) {
          for (int i = 0;i < models.size();i++) {
            StatementModel model = models.get(i);
            writer.getLang().renderFragment(fragments.get(i), writer);
            writer.getLang().renderStatement(model, writer);
          }
          writer.getLang().renderFragment(fragments.getLast(), writer);
        }
      }, renderer);
    });
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
    return new LambdaExpressionModel(node.getBodyKind(), parameterTypes, parameterNames, body);
  }

  @Override
  public CodeModel visitMethod(MethodTree node, VisitContext p) {
    return scan(node.getBody(), p);
  }
}
