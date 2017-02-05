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
import com.sun.source.tree.InstanceOfTree;
import com.sun.source.tree.LambdaExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.ParenthesizedTree;
import com.sun.source.tree.ReturnTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.ThrowTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TryTree;
import com.sun.source.tree.UnaryTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;
import io.vertx.codegen.type.*;
import io.vertx.codetrans.expression.ArraysModel;
import io.vertx.codetrans.expression.ClassModel;
import io.vertx.codetrans.expression.DataObjectClassModel;
import io.vertx.codetrans.expression.ExpressionModel;
import io.vertx.codetrans.expression.IdentifierModel;
import io.vertx.codetrans.expression.ListClassModel;
import io.vertx.codetrans.expression.MethodInvocationModel;
import io.vertx.codetrans.expression.NullLiteralModel;
import io.vertx.codetrans.expression.VariableScope;
import io.vertx.codetrans.expression.JavaClassModel;
import io.vertx.codetrans.expression.JsonArrayClassModel;
import io.vertx.codetrans.expression.JsonObjectClassModel;
import io.vertx.codetrans.expression.LambdaExpressionModel;
import io.vertx.codetrans.expression.StringLiteralModel;
import io.vertx.codetrans.expression.MapClassModel;
import io.vertx.codetrans.expression.ParenthesizedModel;
import io.vertx.codetrans.expression.SystemModel;
import io.vertx.codetrans.expression.ThisModel;
import io.vertx.codetrans.expression.ThrowableClassModel;
import io.vertx.codetrans.expression.ThrowableModel;
import io.vertx.codetrans.statement.ConditionalBlockModel;
import io.vertx.codetrans.statement.ReturnModel;
import io.vertx.codetrans.statement.StatementModel;
import io.vertx.codetrans.statement.TryCatchModel;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
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
  private final TypeMirrorFactory factory;
  private final TypeElement typeElt;

  public ModelBuilder(Trees trees, TypeElement typeElt, DeclaredType systemType, DeclaredType throwableType, TypeMirrorFactory factory, Types typeUtils, Lang lang) {
    this.path = trees.getPath(typeElt);
    this.trees = trees;
    this.systemType = systemType;
    this.throwableType = throwableType;
    this.factory = factory;
    this.typeUtils = typeUtils;
    this.typeElt = typeElt;
  }

  public MethodModel build(ExecutableElement methodElt, VisitContext context) {
    TreePath path = trees.getPath(methodElt);
    return (MethodModel) scan(path, context);
  }

  public StatementModel build(VariableElement variableElt, VisitContext context) {
    TreePath path = trees.getPath(variableElt);
    return (StatementModel) scan(path, context);
  }

  private StatementModel scan(StatementTree tree, VisitContext context) {
    return (StatementModel) scan((Tree) tree, context);
  }

  private ExpressionModel scan(ExpressionTree tree, VisitContext context) {
    return (ExpressionModel) scan((Tree) tree, context);
  }

  @Override
  public CodeModel visitReturn(ReturnTree node, VisitContext context) {
    ExpressionModel expression = scan(node.getExpression(), context);
    return new ReturnModel(expression);
  }

  @Override
  public ExpressionModel visitParameterizedType(ParameterizedTypeTree tree, VisitContext context) {
    return (ExpressionModel) scan((Tree) tree.getType(), context);
  }

  @Override
  public CodeModel visitForLoop(ForLoopTree node, VisitContext context) {
    if (node.getInitializer().size() != 1) {
      throw new UnsupportedOperationException();
    }
    if (node.getUpdate().size() != 1) {
      throw new UnsupportedOperationException();
    }
    StatementModel body = scan(node.getStatement(), context);
    if (node.getInitializer().size() == 1 &&
        node.getInitializer().get(0).getKind() == Tree.Kind.VARIABLE &&
        node.getCondition().getKind() == Tree.Kind.LESS_THAN &&
        node.getUpdate().size() == 1 &&
        node.getUpdate().get(0).getKind() == Tree.Kind.EXPRESSION_STATEMENT &&
        node.getUpdate().get(0).getExpression().getKind() == Tree.Kind.POSTFIX_INCREMENT) {
      VariableTree init = (VariableTree) node.getInitializer().get(0);
      BinaryTree lessThan = (BinaryTree) node.getCondition();
      UnaryTree increment = (UnaryTree) node.getUpdate().get(0).getExpression();
      if (lessThan.getLeftOperand().getKind() == Tree.Kind.IDENTIFIER &&
          increment.getExpression().getKind() == Tree.Kind.IDENTIFIER) {
        String id1 = init.getName().toString();
        String id2 = ((IdentifierTree) lessThan.getLeftOperand()).getName().toString();
        String id3 = ((IdentifierTree) increment.getExpression()).getName().toString();
        if (id1.equals(id2) && id2.equals(id3)) {
          ExpressionModel from = scan(init.getInitializer(), context);
          ExpressionModel to = scan(lessThan.getRightOperand(), context);
          return context.builder.sequenceForLoop(id1, from, to, body);
        }
      }
    }
    StatementModel initializer = scan(node.getInitializer().get(0), context);
    ExpressionModel update = scan(node.getUpdate().get(0).getExpression(), context);
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
    ElementKind kind = decl.sym.getKind();
    VariableScope scope = resolvescope(context, kind, decl.getName().toString());
    return context.builder.variableDecl(scope, type, decl.name.toString(), initializer);
  }

  @Override
  public CodeModel visitTry(TryTree node, VisitContext context) {
    if (node.getCatches().size() != 1) {
      throw new UnsupportedOperationException("Expecting a single catch block");
    }
    StatementModel tryBlock = scan(node.getBlock(), context);
    StatementModel catchBlock = scan(node.getCatches().get(0).getBlock(), context);
    return new TryCatchModel(tryBlock, catchBlock);
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
  public CodeModel visitInstanceOf(InstanceOfTree node, VisitContext p) {
    ExpressionModel classModel = scan(node.getExpression(), p);
    TypeElement type = (TypeElement) ((JCTree.JCIdent) node.getType()).sym;
    return classModel.onInstanceOf(type);
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
        return new NullLiteralModel(context.builder);
      case STRING_LITERAL:
        return new StringLiteralModel(context.builder, node.getValue().toString());
      case BOOLEAN_LITERAL:
        return context.builder.render(writer -> {
          writer.renderBooleanLiteral(node.getValue().toString());
        });
      case INT_LITERAL:
        return context.builder.render(writer -> {
          writer.renderIntegerLiteral(node.getValue().toString());
        });
      case LONG_LITERAL:
        return context.builder.render(writer -> {
          writer.renderLongLiteral(node.getValue().toString());
        });
      case CHAR_LITERAL:
        return context.builder.render(writer -> {
          writer.renderCharLiteral(node.getValue().toString().charAt(0));
        });
      case FLOAT_LITERAL:
        return context.builder.render(writer -> {
          writer.renderFloatLiteral(node.getValue().toString());
        });
      case DOUBLE_LITERAL:
        return context.builder.render(writer -> {
          writer.renderDoubleLiteral(node.getValue().toString());
        });
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
      ClassTypeInfo type = (ClassTypeInfo) factory.create(ident.type);
      if (ident.type.equals(systemType)) {
        return new SystemModel(context.builder);
      } else if (type.getName().equals("java.util.Arrays")) {
        return new ArraysModel(context.builder);
      } else {
        if (typeUtils.isSubtype(ident.type, throwableType)) {
          return new ThrowableClassModel(context.builder, type);
        }
        if (type.getKind() == ClassKind.API) {
          return context.builder.apiType((ApiTypeInfo) type);
        } else if (type.getKind() == ClassKind.JSON_OBJECT) {
          return context.builder.jsonObjectClassModel();
        } else if (type.getKind() == ClassKind.JSON_ARRAY) {
          return context.builder.jsonArrayClassModel();
        } else if (type.getKind() == ClassKind.DATA_OBJECT) {
          return context.builder.dataObjectClass(type);
        } else if (type.getKind() == ClassKind.ENUM) {
          return context.builder.enumType((EnumTypeInfo) type);
        } else {
          switch (type.getName()) {
            case "java.util.HashMap":
              return new MapClassModel(context.builder);
            case "java.util.ArrayList":
              return new ListClassModel(context.builder);
            default:
              return new JavaClassModel(context.builder, type);
          }
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
        VariableScope scope;
        scope = resolvescope(context, kind, name);
        return context.builder.identifier(name, scope).as(type);
      }
    }
  }

  private VariableScope resolvescope(VisitContext context, ElementKind kind, final String name) {
    VariableScope scope;
    switch (kind) {
      case LOCAL_VARIABLE:
        scope = VariableScope.VARIABLE;
        break;
      case PARAMETER:
        scope = VariableScope.PARAMETER;
        break;
      case FIELD:
        AtomicReference<VariableScope> resolvedScope = new AtomicReference<>(VariableScope.GLOBAL);
        new TreePathScanner<Void, Void>() {
          @Override
          public Void visitVariable(VariableTree node, Void aVoid) {
            if (node.getName().toString().equals(name)) {
              resolvedScope.set(VariableScope.FIELD);
            }
            return null;
          };
        }.scan(path, null);
        if (resolvedScope.get() == VariableScope.FIELD) {
          context.getReferencedFields().add(name);
        }
        scope = resolvedScope.get();
        break;
      default:
        throw new UnsupportedOperationException("Unsupported kind " + kind);
    }
    return scope;
  }

  @Override
  public CodeModel visitNewClass(NewClassTree node, VisitContext context) {
    ClassModel identifier = (ClassModel) scan(node.getIdentifier(), context);
    List<ExpressionModel> arguments = node.getArguments().stream().map(arg -> scan(arg, context)).collect(Collectors.toList());
    JCTree.JCNewClass newClass = (JCTree.JCNewClass) node;
    return identifier.onNew(arguments);
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
    return new ParenthesizedModel(context.builder, expression);
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
      JCTree.JCMemberReference refTree = (JCTree.JCMemberReference) node;
      ExecutableElement method = (ExecutableElement) refTree.sym;
      MethodSignature signature = createMethodSignature(method, false);
      ExpressionModel expression = scan(node.getQualifierExpression(), p);
      if (expression instanceof ThisModel) {
        p.getReferencedMethods().add(node.getName().toString());
      }
      ExpressionModel methodReferenceExpression = expression.onMethodReference(signature);
      return methodReferenceExpression;
    } else {
      throw new UnsupportedOperationException("New reference not implemented yet");
    }
  }

  @Override
  public ExpressionModel visitMethodInvocation(MethodInvocationTree node, VisitContext context) {

    ExecutableElement exec = (ExecutableElement) trees.getElement(trees.getPath(path.getCompilationUnit(), node));
    boolean varargs = exec.isVarArgs();

    // Compute the argument types
    List<TypeInfo> argTypes = new ArrayList<>();
    List<TypeMirror> argsTypeMirror = new ArrayList<>();
    for (JCTree.JCExpression argExpr : ((JCTree.JCMethodInvocation) node).getArguments()) {
      TypeInfo argType = null;
      argsTypeMirror.add(argExpr.type);
      if (argExpr.type.getKind() != TypeKind.NULL) {
        argType = factory.create(argExpr.type);
      }
      argTypes.add(argType);
    }

    //
    List<TypeArg> typeArgs = resolveTypeArgs(node, argsTypeMirror);

    //
    List<ExpressionModel> argumentModels = node.getArguments().stream().map(argument -> scan(argument, context)).collect(Collectors.toList());
    TypeInfo returnType = factory.create(((JCTree) node).type);

    // We don't go for scanning here as it would complicate things and need to introduce
    // extra models
    Symbol.MethodSymbol sym;
    ExpressionModel memberSelectExpression;
    String name;
    boolean addToRefedMethods;
    if (node.getMethodSelect() instanceof IdentifierTree) {
      JCTree.JCIdent def = (JCTree.JCIdent) node.getMethodSelect();
      name = def.getName().toString();
      memberSelectExpression = context.builder.thisModel();
      sym = (Symbol.MethodSymbol) def.sym;
      addToRefedMethods = true;
    } else {
      // Is there a case it would not be a member select expression ?
      JCTree.JCFieldAccess memberSelect = (JCTree.JCFieldAccess) node.getMethodSelect();
      memberSelectExpression = scan(memberSelect.getExpression(), context);
      name = memberSelect.getIdentifier().toString();
      sym = (Symbol.MethodSymbol) memberSelect.sym;
      addToRefedMethods = false;
    }

    //
    TypeInfo type = factory.create(sym.owner.type);
    MethodSignature signature = createMethodSignature(sym, varargs);
    if (addToRefedMethods) {
      context.getReferencedMethods().add(name);
    }

    ExpressionModel expression = memberSelectExpression.onMethodInvocation(type, signature, returnType, typeArgs, argumentModels, argTypes);
    return expression.as(returnType);
  }

  private List<TypeArg> resolveTypeArgs(MethodInvocationTree node, List<TypeMirror> argTypes) {
    ExecutableElement exec = (ExecutableElement) trees.getElement(trees.getPath(path.getCompilationUnit(), node));
    ExecutableType methodType = (ExecutableType) exec.asType();
    List<TypeArg> typeArgs = new ArrayList<>();
    List<JCTree.JCExpression> typeArgsExpr = ((JCTree.JCMethodInvocation) node).getTypeArguments();
    List<? extends TypeParameterElement> typeParamElts = exec.getTypeParameters();

    // Resolve the type argument
    for (int i = 0;i < typeParamElts.size();i++) {
      if (i < typeArgsExpr.size()) {

        // This is the case where the code has an explicit list of type arguments:
        // foo<String>(...) -> we resolve String
        JCTree.JCExpression typeArgExpr = typeArgsExpr.get(i);
        TypeInfo typeArg = factory.create(typeArgExpr.type);
        typeArgs.add(new TypeArg(typeArg, false));
      } else {

        // This is the case where we need to make some inference to deduce the type
        // for this matter we examine all valid parameters and try to use the method invocation
        // types to resolve the type, for instance:
        // foo("abc") -> we resolve String
        // but it can get more complex, see resolveTypeVariable
        TypeParameterElement typeParameterElt = typeParamElts.get(i);
        TypeVariable typeVar = (TypeVariable) typeParameterElt.asType();
        TypeMirror resolved = null;

        // In case of a varargs we don't examine the last parameter
        int size = methodType.getParameterTypes().size();
        if (exec.isVarArgs()) {
          size--;
        }

        // Use each parameter and see if we can resolve (or not)
        for (int j = 0;j < size;j++) {
          TypeMirror argType = argTypes.get(j);
          TypeMirror another = methodType.getParameterTypes().get(j);
          resolved = resolveTypeVariable(typeVar, another, argType);
          if (resolved != null) {
            // No need to continue
            break;
          }
        }
        if (resolved != null) {
          typeArgs.add(new TypeArg(factory.create(resolved), true));
        } else {
          typeArgs.add(null);
        }
      }
    }
    return typeArgs;
  }

  /**
   * Resolve the type variable, basically some kind of inference:
   *
   * for the method {@code <T> void m(T t)}
   *   - resolveType(<T>,T,m("s")) -> String
   *   - resolveType(<T>,T,m(null)) -> null
   * for the method {@code <T> void m(List<T> t)}
   *   - resolveType(<T>,List<T>,m(Arrays.asList("s"))) -> String
   *   - resolveType(<T>,List<T>,m(BooleanList)) where BooleanList extends List<Boolean> -> Boolean
   *   - resolveType(<T>,List<T>,m(null)) -> null
   */
  private TypeMirror resolveTypeVariable(TypeVariable typeVar, TypeMirror parameterType, TypeMirror argumentType) {
    if (typeVar.equals(parameterType) && argumentType.getKind() != TypeKind.NULL) {
      return argumentType;
    } else {
      TypeParameterElement ttt = resolveTypeParameterElement(typeVar, parameterType);
      if (ttt != null && argumentType.getKind() == TypeKind.DECLARED) {
        TypeMirror resolved = io.vertx.codegen.Helper.resolveTypeParameter(typeUtils, (DeclaredType) argumentType, ttt);
        if (resolved != null) {
          return resolved;
        }
      }
    }
    return null;
  }

  /**
   * Resolve the type variable as a type parameter in the given type.
   *
   * for the method {@code <T> void m(List<T>)} -> the <T> of List<T>
   * for the method {@code <T> void m(List<Set<T>>)} -> the <T> of Set<T>
   */
  private TypeParameterElement resolveTypeParameterElement(TypeVariable typeVar, TypeMirror type) {
    if (type.getKind() == TypeKind.DECLARED) {
      DeclaredType dt = (DeclaredType) type;
      for (int i = 0;i < dt.getTypeArguments().size();i++) {
        if (typeVar.equals(dt.getTypeArguments().get(i))) {
          TypeElement te = (TypeElement) dt.asElement();
          return te.getTypeParameters().get(i);
        }
      }
      for (int i = 0;i < dt.getTypeArguments().size();i++) {
        TypeParameterElement te = resolveTypeParameterElement(typeVar, dt.getTypeArguments().get(i));
        if (te != null) {
          return te;
        }
      }
    }
    return null;
  }

  private MethodSignature createMethodSignature(ExecutableElement sym, boolean varargs) {
    ExecutableType methodType = (ExecutableType) typeUtils.asMemberOf((DeclaredType) sym.getEnclosingElement().asType(), sym);

    // Compute the parameter types
    List<TypeInfo> parameterTypes = new ArrayList<>();
    for (Iterator<? extends TypeMirror> it = methodType.getParameterTypes().iterator();it.hasNext();) {
      TypeMirror type = it.next();
      if (!it.hasNext() && varargs) {
        ArrayType arrayType = (ArrayType) type;
        type = arrayType.getComponentType();
      }
      TypeInfo parameterType = factory.create(type);
      parameterTypes.add(parameterType);
    }

    TypeInfo returnType = factory.create(methodType.getReturnType());
    return new MethodSignature(sym.getSimpleName().toString(), parameterTypes, varargs, returnType);
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
      if (last.vartype instanceof ParameterizedTypeTree) {
        ParameterizedTypeTree typeApply = (ParameterizedTypeTree) last.getType();
        if (typeApply.getType() instanceof MemberSelectTree) {
          TypeInfo type = factory.create(last.getType().type);
          if (type.getKind() == ClassKind.ASYNC_RESULT) {
            String identifier = last.name.toString();
            ExpressionModel result = context.builder.asyncResult(identifier, ((ParameterizedTypeInfo)(type)).getArgs().get(0));
            CodeModel body = scan(node.getBody(), context.putAlias(last.sym, result));
            ParameterizedTypeInfo parameterized = (ParameterizedTypeInfo) type;
            BlockTree block = (BlockTree) node.getBody();
            CodeModel succeededBody = null;
            CodeModel failedBody = null;
            if (block.getStatements().size() == 1) {
              StatementTree statement = block.getStatements().get(0);
              if (statement.getKind() == Tree.Kind.IF) {
                IfTree ifTree = (IfTree) statement;
                if (ifTree.getCondition().getKind() == Tree.Kind.PARENTHESIZED) {
                  ExpressionTree inner = ((ParenthesizedTree) ifTree.getCondition()).getExpression();
                  if (inner.getKind() == Tree.Kind.METHOD_INVOCATION) {
                    MethodInvocationModel invocationModel = (MethodInvocationModel) visitMethodInvocation((MethodInvocationTree) inner, context);
                    if (invocationModel.receiverType.getKind() == ClassKind.ASYNC_RESULT &&
                        invocationModel.expression instanceof IdentifierModel &&
                        ((IdentifierModel) invocationModel.expression).name.equals(identifier)) {
                      MethodSignature method = invocationModel.method;
                      if (method.name.equals("succeeded") && method.parameterTypes.isEmpty()) {
                        succeededBody = scan(ifTree.getThenStatement(), context);
                        if (ifTree.getElseStatement() != null) {
                          failedBody = scan(ifTree.getElseStatement(), context);
                        }
                      } else if (method.name.equals("failed") && method.parameterTypes.isEmpty()) {
                        failedBody = scan(ifTree.getThenStatement(), context);
                        if (ifTree.getElseStatement() != null) {
                          succeededBody = scan(ifTree.getThenStatement(), context);
                        }
                      }
                    }
                  }
                }
              }
            }
            return context.builder.asyncResultHandler(node.getBodyKind(), parameterized, identifier, body, succeededBody, failedBody);
          }
        }
      }
    }
    CodeModel body = scan(node.getBody(), context);
    return new LambdaExpressionModel(context.builder, node.getBodyKind(), parameterTypes, parameterNames, body);
  }

  @Override
  public MethodModel visitMethod(MethodTree node, VisitContext p) {
    List<TypeInfo> parameterTypes = new ArrayList<>();
    for (VariableTree var : node.getParameters()) {
      JCTree.JCVariableDecl decl = (JCTree.JCVariableDecl) var;
      TypeInfo parameterType = factory.create(decl.sym.asType());
      parameterTypes.add(parameterType);
    }
    TypeInfo returnType = VoidTypeInfo.INSTANCE;
    if (node.getReturnType() instanceof JCTree) {
      returnType = factory.create(((JCTree)node.getReturnType()).type);
    }
    return new MethodModel("" + typeElt.getQualifiedName(), scan(node.getBody(), p), new MethodSignature(node.getName().toString(), parameterTypes, false, returnType), node.getParameters().stream().map(param -> param.getName().toString()).collect(Collectors.toList()));
  }
}
