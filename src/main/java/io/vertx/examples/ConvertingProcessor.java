package io.vertx.examples;

import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.comp.Attr;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;
import io.vertx.codegen.TypeInfo;
import io.vertx.core.AbstractVerticle;
import org.junit.Assert;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ConvertingProcessor extends AbstractProcessor {

  private static final javax.tools.JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
  private static final Locale locale = Locale.getDefault();
  private static final Charset charset = Charset.forName("UTF-8");

  public static Map<String, String> convert(ClassLoader loader, Lang lang, String... sources) throws Exception {
    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
    StandardJavaFileManager manager = javac.getStandardFileManager(diagnostics, locale, charset);
    List<File> files = new ArrayList<>();
    for (String source : sources) {
      URL url = loader.getResource(source);
      if (url == null) {
        throw new Exception("Cannot resolve source " + source + "");
      }
      Assert.assertNotNull(url);
      files.add(new File(url.toURI()));
    }
    Iterable<? extends JavaFileObject> fileObjects = manager.getJavaFileObjects(files.toArray(new File[files.size()]));
    StringWriter out = new StringWriter();
    JavaCompiler.CompilationTask task = javac.getTask(
        out,
        manager,
        diagnostics,
        Collections.<String>emptyList(),
        Collections.<String>emptyList(),
        fileObjects);
    task.setLocale(locale);
    ConvertingProcessor processor = new ConvertingProcessor(lang);
    task.setProcessors(Collections.<Processor>singletonList(processor));
    if (task.call()) {
      return processor.getResult();
    } else {
      StringWriter message = new StringWriter();
      PrintWriter writer = new PrintWriter(message);
      writer.append("Compilation of ").append(Arrays.toString(sources)).println(" failed:");
      for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics())  {
        writer.append(diagnostic.getMessage(locale));
      }
      writer.println("console:");
      writer.append(out.getBuffer());
      throw new Exception(message.toString());
    }
  }

  private Map<String, String> result = new HashMap<>();
  private Trees trees;
  private DeclaredType AbstractVerticleType;
  private DeclaredType SystemType;
  private Attr attr;
  private Lang lang;
  private TypeInfo.Factory factory;

  public ConvertingProcessor(Lang lang) {
    this.lang = lang;
  }

  public Map<String, String> getResult() {
    return result;
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Collections.singleton(CodeTrans.class.getName());
  }

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    this.trees = Trees.instance(processingEnv);
    this.AbstractVerticleType = (DeclaredType) processingEnv.getElementUtils().getTypeElement(AbstractVerticle.class.getName()).asType();
    this.SystemType = (DeclaredType) processingEnv.getElementUtils().getTypeElement(System.class.getName()).asType();
    Context context = ((JavacProcessingEnvironment)processingEnv).getContext();
    this.attr = Attr.instance(context);
    this.factory = new TypeInfo.Factory(processingEnv.getElementUtils(), processingEnv.getTypeUtils());
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    for (Element annotatedElt : roundEnv.getElementsAnnotatedWith(CodeTrans.class)) {
      ExecutableElement methodElt = (ExecutableElement) annotatedElt;
      TypeElement typeElt = (TypeElement) methodElt.getEnclosingElement();
      attributeClass(typeElt);
      TreePath path = trees.getPath(annotatedElt);
      ModelBuilder builder = new ModelBuilder(SystemType, factory, lang);
      CodeModel model = builder.build(path);
      CodeWriter writer = new CodeWriter(lang);
      model.render(writer);
      result.put(typeElt.toString().replace('.', '/') + '.' + lang.getExtension(), writer.getBuffer().toString());
    }
    return false;
  }

  public void attributeClass(Element classElement) {
    assert classElement.getKind() == ElementKind.CLASS;
    JCTree.JCClassDecl ct = (JCTree.JCClassDecl) trees.getTree(classElement);
    if (ct.sym != null) {
      if ((ct.sym.flags_field & Flags.UNATTRIBUTED) != 0) {
        attr.attribClass(ct.pos(), ct.sym);
      }
    }
  }
}
