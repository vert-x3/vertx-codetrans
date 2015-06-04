package io.vertx.codetrans;

import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.comp.Attr;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;
import io.vertx.codegen.TypeInfo;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.Types;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CodeTranslator {

  private final Trees trees;
  private final DeclaredType SystemType;
  private final DeclaredType ThrowableType;
  private final Attr attr;
  private final TypeInfo.Factory factory;
  private final Types typeUtils;

  public CodeTranslator(ProcessingEnvironment processingEnv) {
    this.trees = Trees.instance(processingEnv);
    this.SystemType = (DeclaredType) processingEnv.getElementUtils().getTypeElement(System.class.getName()).asType();
    this.ThrowableType = (DeclaredType) processingEnv.getElementUtils().getTypeElement(Throwable.class.getName()).asType();
    Context context = ((JavacProcessingEnvironment)processingEnv).getContext();
    this.attr = Attr.instance(context);
    this.typeUtils = processingEnv.getTypeUtils();
    this.factory = new TypeInfo.Factory(processingEnv.getElementUtils(), processingEnv.getTypeUtils()) {
      @Override
      public TypeInfo.Wildcard create(WildcardType type) {
        // Tolerate wildcard bounds for unit tests, for instance we interrop with Map that uses bound wildcards
        return new TypeInfo.Wildcard();
      }
    };
  }

  public String translate(ExecutableElement methodElt, Lang lang) {
    TypeElement typeElt = (TypeElement) methodElt.getEnclosingElement();
    attributeClass(typeElt);
    TreePath path = trees.getPath(methodElt);
    ModelBuilder builder = new ModelBuilder(trees, path, SystemType, ThrowableType, factory, typeUtils, lang);
    VisitContext visitContext = new VisitContext();
    CodeModel model = builder.build(path, visitContext);
    CodeWriter writer = lang.newWriter();
    model.render(writer);
    return writer.getBuffer().toString();
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
