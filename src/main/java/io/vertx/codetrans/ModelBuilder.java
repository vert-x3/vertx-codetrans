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

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
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

  public ModelBuilder(Trees trees, TreePath path, DeclaredType systemType, DeclaredType throwableType, TypeInfo.Factory factory, Types typeUtils, Lang lang) {
    this.path = path;
    this.trees = trees;
    this.systemType = systemType;
    this.throwableType = throwableType;
    this.factory = factory;
    this.typeUtils = typeUtils;
  }

  public CodeModel build(TreePath path, VisitContext context) {
    return scan(path, context);
  }

  public StatementModel scan(StatementTree tree, VisitContext context) {
    return (StatementModel) scan((Tree) tree, context);
  }

  public ExpressionModel scan(ExpressionTree tree, VisitContext context) {
    return (ExpressionModel) scan((Tree) tree, context);
  }

  @Override
  public CodeModel visitForLoop(ForLoopTree node, VisitContext context) {
    if (node.getInitializer().size() != 1) {
      throw new UnsupportedOperationException();
    }
    if (node.getUpdate().size() != 1) {
      throw new UnsupportedOperationException();
    }
    StatementModel initializer = scan(node.getInitializer().get(0), context);
    ExpressionModel update = scan(node.getUpdate().get(0).getExpression(), context);
    StatementModel body = scan(node.getStatement(), context);
    ExpressionModel condition = scan(node.getCondition(), context);
    return context.builder.forLoop(initializer, condition, update, body);
  }

  @Override
  public CodeModel visitEnhancedForLoop(EnhancedForLoopTree node, VisitContext context) {
    ExpressionModel expression = scan(node.getExpression(), context);
    StatementModel body = scan(node.getStatement(), context);
    return context.builder.enhancedForLoop(node.getVariable().getName().toString(), expression, body);
  }

  @Override
  public CodeModel visitAssignment(AssignmentTree node, VisitContext context) {
    ExpressionModel variable = scan(node.getVariable(), context);
    ExpressionModel expression = scan(node.getExpression(), context);
    return context.builder.forAssign(variable, expression);
  }

  @Override
  public StatementModel visitVariable(VariableTree node, VisitContext context) {
    JCTree.JCVariableDecl decl = (JCTree.JCVariableDecl) node;
    ExpressionModel initializer;
    if (node.getInitializer() != null) {
      initializer = scan(node.getInitializer(), context);
    } else {
      initializer = null;
    }
    TypeInfo type = factory.create(decl.type);
    return context.builder.variableDecl(
        type,
        decl.name.toString(),
        initializer
    );
  }

  @Override
  public StatementModel visitIf(IfTree node, VisitContext context) {
    List<ConditionalBlockModel> conditionals = new ArrayList<>();
    StatementModel otherwise = build(conditionals, node, context);
    return StatementModel.conditionals(conditionals, otherwise);
  }

  private StatementModel build(List<ConditionalBlockModel> conditionals, IfTree node, VisitContext context) {
    ExpressionModel condition = scan(node.getCondition(), context);
    StatementModel body = scan(node.getThenStatement(), context);
    conditionals.add(new ConditionalBlockModel(condition, body));
    StatementTree elseStatement = node.getElseStatement();
    if (elseStatement != null) {
      if (elseStatement instanceof IfTree) {
        IfTree next = (IfTree) elseStatement;
        return build(conditionals, next, context);
      } else {
        return scan(node.getElseStatement(), context);
      }
    }
    return null;
  }

  @Override
  public CodeModel visitConditionalExpression(ConditionalExpressionTree node, VisitContext context) {
    ExpressionModel condition = scan(node.getCondition(), context);
    ExpressionModel trueExpression = scan(node.getTrueExpression(), context);
    ExpressionModel falseExpression = scan(node.getFalseExpression(), context);
    return context.builder.forConditionalExpression(condition, trueExpression, falseExpression);
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
  public ExpressionModel visitBinary(BinaryTree node, VisitContext context) {
    ExpressionModel left = scan(node.getLeftOperand(), context);
    ExpressionModel right = scan(node.getRightOperand(), context);
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
    return context.builder.combine(left, op, right);
  }

  @Override
  public ExpressionModel visitLiteral(LiteralTree node, VisitContext context) {
    switch (node.getKind()) {
      case NULL_LITERAL:
        return context.builder.nullLiteral();
      case STRING_LITERAL:
        return context.builder.stringLiteral(node.getValue().toString());
      case BOOLEAN_LITERAL:
        return context.builder.booleanLiteral(node.getValue().toString());
      case INT_LITERAL:
        return context.builder.integerLiteral(node.getValue().toString());
      case LONG_LITERAL:
        return context.builder.longLiteral(node.getValue().toString());
      case CHAR_LITERAL:
        return context.builder.characterLiteral(node.getValue().toString().charAt(0));
      case FLOAT_LITERAL:
        return context.builder.floatLiteral(node.getValue().toString());
      case DOUBLE_LITERAL:
        return context.builder.doubleLiteral(node.getValue().toString());
      default:
        throw new UnsupportedOperationException("Literal " + node.getKind().name() + " not yet implemented");
    }
  }

  @Override
  public ExpressionModel visitIdentifier(IdentifierTree node, VisitContext context) {
    JCTree.JCIdent ident = (JCTree.JCIdent) node;
    if (node.getName().toString().equals("this")) {
      return new ThisModel(context.builder);
    }
    if (ident.sym instanceof TypeElement) {
      if (ident.type.equals(systemType)) {
        return context.builder.forFieldSelect("out",
            () ->
                context.builder.forMethodInvocation("println", args -> context.builder.console(args.get(0))));
      } else {
        if (typeUtils.isSubtype(ident.type, throwableType)) {
          return context.builder.forNew(args -> {
            if (args.size() == 0) {
              return new ThrowableModel(context.builder, ident.type.toString(), null);
            } else if (args.size() == 1) {
              return new ThrowableModel(context.builder, ident.type.toString(), args.get(0));
            }
            throw new UnsupportedOperationException("Only empty or String throwable constructor are accepted");
          });
        }
        TypeInfo.Class type = (TypeInfo.Class) factory.create(ident.type);
        if (type.getKind() == ClassKind.API) {
/*
          return new ExpressionModel() {
            @Override
            public ExpressionModel onMethodInvocation(TypeInfo receiverType, MethodRef method, TypeInfo returnType, List<ExpressionModel> argumentModels, List<TypeInfo> argumenTypes) {
              return context.lang.staticFactory(type, method, returnType, argumentModels, argumenTypes);
            }
          };
*/
          return context.builder.apiType((TypeInfo.Class.Api) type);
        } else if (type.getKind() == ClassKind.JSON_OBJECT) {
          return context.builder.forNew(args -> {
            switch (args.size()) {
              case 0:
                return new JsonObjectLiteralModel(context.builder);
              default:
                throw new UnsupportedOperationException();
            }
          });
        } else if (type.getKind() == ClassKind.JSON_ARRAY) {
          return context.builder.forNew(args -> {
            switch (args.size()) {
              case 0:
                return new JsonArrayLiteralModel(context.builder);
              default:
                throw new UnsupportedOperationException();
            }
          });
        } else if (type.getKind() == ClassKind.DATA_OBJECT) {
          return context.builder.forNew(args -> new DataObjectLiteralModel(context.builder, type));
        } else if (type.getKind() == ClassKind.ENUM) {
          return context.builder.enumType((TypeInfo.Class.Enum) type);
        } else {
          return context.builder.classExpression(type);
        }
      }
    } else {
      ExpressionModel alias = context.getAlias(ident.sym);
      if (alias != null) {
        return alias;
      } else {
        ElementKind kind = ident.sym.getKind();
        String name = node.getName().toString();
        TypeInfo type = factory.create(ident.type);
        switch (kind) {
          case LOCAL_VARIABLE:
          case PARAMETER:
            return context.builder.variable(type, true, name);
          case FIELD:
            return context.builder.variable(type, false, name);
          default:
            throw new UnsupportedOperationException("Unsupported kind " + kind);
        }
      }
    }
  }

  @Override
  public CodeModel visitNewClass(NewClassTree node, VisitContext context) {
    ExpressionModel identifier = scan(node.getIdentifier(), context);
    List<ExpressionModel> arguments = node.getArguments().stream().map(arg -> scan(arg, context)).collect(Collectors.toList());
    JCTree.JCNewClass newClass = (JCTree.JCNewClass) node;
    TypeInfo type = factory.create(newClass.type);
    return identifier.onNew(type, arguments);
  }

  @Override
  public CodeModel visitThrow(ThrowTree node, VisitContext context) {
    ThrowableModel throwableExpression = (ThrowableModel) scan(node.getExpression(), context);
    return StatementModel.render(writer -> {
      writer.renderThrow(throwableExpression.getType(), throwableExpression.getReason());
    });
  }

  @Override
  public CodeModel visitParenthesized(ParenthesizedTree node, VisitContext context) {
    ExpressionModel expression = scan(node.getExpression(), context);
    return context.builder.forParenthesized(expression);
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
      if (expression instanceof ThisModel) {
        p.getReferencedMethods().add(node.getName().toString());
      }
      ExpressionModel methodReferenceExpression = expression.onMethodReference(node.getName().toString());
      return methodReferenceExpression;
    } else {
      throw new UnsupportedOperationException("New reference not implemented yet");
    }
  }

  @Override
  public ExpressionModel visitMethodInvocation(MethodInvocationTree node, VisitContext context) {

    // Compute the argument types
    List<TypeInfo> argumentTypes = new ArrayList<>();
    for (JCTree.JCExpression argument : ((JCTree.JCMethodInvocation) node).getArguments()) {
      TypeInfo argumentType = null;
      if (argument.type.getKind() != TypeKind.NULL) {
        argumentType = factory.create(argument.type);
      }
      argumentTypes.add(argumentType);
    }

    //
    List<ExpressionModel> argumentModels = node.getArguments().stream().map(argument -> scan(argument, context)).collect(Collectors.toList());
    TypeInfo returnType = factory.create(((JCTree) node).type);

    //
    Symbol.MethodSymbol sym;
    ExpressionModel memberSelectExpression;
    String methodName;
    boolean addToRefedMethods;
    if (node.getMethodSelect() instanceof IdentifierTree) {
      JCTree.JCIdent def = (JCTree.JCIdent) node.getMethodSelect();
      methodName = def.getName().toString();
      memberSelectExpression = context.builder.thisModel(); // todo : see if we can resolve via scannning
      sym = (Symbol.MethodSymbol) def.sym;
      addToRefedMethods = true;
    } else {
      // Is there a case it would not be a member select expression ?
      JCTree.JCFieldAccess memberSelect = (JCTree.JCFieldAccess) node.getMethodSelect();
      memberSelectExpression = scan(memberSelect.getExpression(), context);
      methodName = memberSelect.getIdentifier().toString();
      sym = (Symbol.MethodSymbol) memberSelect.sym;
      addToRefedMethods = false;
    }

    // Compute the parameter types
    List<TypeInfo> parameterTypes = new ArrayList<>();
    for (Symbol.VarSymbol var : sym.getParameters()) {
      TypeInfo parameterType = factory.create(var.asType());
      parameterTypes.add(parameterType);
    }

    TypeInfo type = factory.create(sym.owner.type);
    MethodRef method = new MethodRef(methodName, parameterTypes);
    if (addToRefedMethods) {
      context.getReferencedMethods().add(methodName);
    }

    ExpressionModel expression = memberSelectExpression.onMethodInvocation(type, method, returnType, argumentModels, argumentTypes);
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
    return StatementModel.render(writer -> {
      writer.renderBlock(new BlockModel() {
        @Override
        public void render(CodeWriter writer) {
          for (int i = 0; i < models.size(); i++) {
            StatementModel model = models.get(i);
            writer.renderFragment(fragments.get(i));
            writer.renderStatement(model);
          }
          writer.renderFragment(fragments.getLast());
        }
      });
    });
  }

  @Override
  public ExpressionModel visitLambdaExpression(LambdaExpressionTree node, VisitContext context) {
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
            ExpressionModel result = context.builder.asyncResult(last.name.toString());
            CodeModel body = scan(node.getBody(), context.putAlias(last.sym, result));
            TypeInfo.Parameterized parameterized = (TypeInfo.Parameterized) type;
            return context.builder.asyncResultHandler(node.getBodyKind(), parameterized, last.name.toString(), body);
          }
        }
      }
    }
    CodeModel body = scan(node.getBody(), context);
    return new LambdaExpressionModel(context.builder, node.getBodyKind(), parameterTypes, parameterNames, body);
  }

  @Override
  public MethodModel visitMethod(MethodTree node, VisitContext p) {
    return new MethodModel(scan(node.getBody(), p), node.getParameters().stream().map(param -> param.getName().toString()).collect(Collectors.toList()));
  }
}
