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

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CodeTranslator {

  private Trees trees;
  private DeclaredType SystemType;
  private Attr attr;
  private TypeInfo.Factory factory;

  public CodeTranslator(ProcessingEnvironment processingEnv) {
    this.trees = Trees.instance(processingEnv);
    this.SystemType = (DeclaredType) processingEnv.getElementUtils().getTypeElement(System.class.getName()).asType();
    Context context = ((JavacProcessingEnvironment)processingEnv).getContext();
    this.attr = Attr.instance(context);
    this.factory = new TypeInfo.Factory(processingEnv.getElementUtils(), processingEnv.getTypeUtils());
  }

  public String translate(ExecutableElement methodElt, Lang lang) {
    TypeElement typeElt = (TypeElement) methodElt.getEnclosingElement();
    attributeClass(typeElt);
    TreePath path = trees.getPath(methodElt);
    ModelBuilder builder = new ModelBuilder(SystemType, factory, lang);
    CodeModel model = builder.build(path);
    CodeWriter writer = new CodeWriter(lang);
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
