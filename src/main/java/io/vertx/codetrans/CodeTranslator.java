package io.vertx.codetrans;

import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.comp.Attr;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;
import io.vertx.codegen.type.TypeInfo;
import io.vertx.codegen.type.TypeUse;
import io.vertx.codegen.type.TypeMirrorFactory;
import io.vertx.codetrans.statement.StatementModel;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.Types;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CodeTranslator {

  private final Trees trees;
  private final DeclaredType SystemType;
  private final DeclaredType ThrowableType;
  private final Attr attr;
  private final TypeMirrorFactory factory;
  private final Types typeUtils;

  public CodeTranslator(ProcessingEnvironment processingEnv) {
    this.trees = Trees.instance(processingEnv);
    this.SystemType = (DeclaredType) processingEnv.getElementUtils().getTypeElement(System.class.getName()).asType();
    this.ThrowableType = (DeclaredType) processingEnv.getElementUtils().getTypeElement(Throwable.class.getName()).asType();
    Context context = ((JavacProcessingEnvironment)processingEnv).getContext();
    this.attr = Attr.instance(context);
    this.typeUtils = processingEnv.getTypeUtils();
    this.factory = new TypeMirrorFactory(processingEnv.getElementUtils(), processingEnv.getTypeUtils(), null) {
      @Override
      public TypeInfo create(TypeUse use, TypeMirror type) {
        if (type.getKind() == TypeKind.WILDCARD) {
          WildcardType wildcardType = (WildcardType) type;
          if (wildcardType.getExtendsBound() != null) {
            return super.create(wildcardType.getExtendsBound());
          } else if (wildcardType.getSuperBound() != null) {
            return super.create(use, wildcardType.getSuperBound());
          }
        }
        return super.create(use, type);
      }
    };
  }

  public String translate(ExecutableElement methodElt, Lang lang) {
    return translate(methodElt, false, lang, RenderMode.SNIPPET);
  }

  public String translate(ExecutableElement methodElt, boolean isVerticle, Lang lang) {
    return translate(methodElt, isVerticle, lang, RenderMode.SNIPPET);
  }

  public String translate(ExecutableElement methodElt, boolean isVerticle, Lang lang, RenderMode renderMode) {
    TypeElement typeElt = (TypeElement) methodElt.getEnclosingElement();
    attributeClass(typeElt);
    ModelBuilder builder = new ModelBuilder(trees, typeElt, SystemType, ThrowableType, factory, typeUtils, lang);
    VisitContext visitContext = new VisitContext(lang.codeBuilder());
    MethodModel main = builder.build(methodElt, visitContext);
    Map<String, MethodModel> methods = new HashMap<>();
    Map<String, StatementModel> fields = new HashMap<>();
    Map<String, Boolean> pending = visitContext.getReferencedMethods().stream().collect(Collectors.toMap(k -> k, k -> true));
    visitContext.getReferencedFields().forEach(field -> {
      pending.put(field, false);
    });
    while (pending.size() > 0) {
      Iterator<Map.Entry<String, Boolean>> it = pending.entrySet().iterator();
      Map.Entry<String, Boolean> entry = it.next();
      String name = entry.getKey();
      it.remove();
      VisitContext other = null;
      if (entry.getValue()) {
        for (Element enclosed : typeElt.getEnclosedElements()) {
          if (enclosed instanceof ExecutableElement && enclosed.getSimpleName().toString().equals(name)) {
            other = new VisitContext(visitContext.builder);
            MethodModel method = builder.build((ExecutableElement) enclosed, other);
            methods.put(name, method);
          }
        }
      } else {
        for (Element enclosed : typeElt.getEnclosedElements()) {
          if (enclosed instanceof VariableElement && enclosed.getSimpleName().toString().equals(name)) {
            other = new VisitContext(visitContext.builder);
            StatementModel statement = builder.build((VariableElement) enclosed, other);
            fields.put(name, statement);
          }
        }
      }
      if (other == null) {
        throw new UnsupportedOperationException("Field / method " + name + " could not be resolved ");
      }
      for (String method : other.getReferencedMethods()) {
        if (fields.containsKey(method)) {
          throw new UnsupportedOperationException("Duplicate field / method " + method);
        }
        if (!methods.containsKey(method)) {
          pending.put(method, true);
        }
      }
      for (String field : other.getReferencedFields()) {
        if (methods.containsKey(field)) {
          throw new UnsupportedOperationException("Duplicate field / method " + field);
        }
        if (!fields.containsKey(field)) {
          pending.put(field, false);
        }
      }
    }

    RunnableCompilationUnit unit = new RunnableCompilationUnit(isVerticle, main, methods, fields);
    return visitContext.builder.render(unit, renderMode);
  }

  private void attributeClass(Element classElement) {
    assert classElement.getKind() == ElementKind.CLASS;
    JCTree.JCClassDecl ct = (JCTree.JCClassDecl) trees.getTree(classElement);
    if (ct.sym != null) {
      if ((ct.sym.flags_field & Flags.UNATTRIBUTED) != 0) {
        attr.attribClass(ct.pos(), ct.sym);
      }
    }
  }
}
