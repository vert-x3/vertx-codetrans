package io.vertx.codetrans;

import io.vertx.codetrans.annotations.CodeTranslate;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
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
  private Lang lang;
  private CodeTranslator translator;

  public ConvertingProcessor(Lang lang) {
    this.lang = lang;
  }

  public Map<String, String> getResult() {
    return result;
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Collections.singleton(CodeTranslate.class.getName());
  }

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    this.translator = new CodeTranslator(processingEnv);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    for (Element annotatedElt : roundEnv.getElementsAnnotatedWith(CodeTranslate.class)) {
      ExecutableElement methodElt = (ExecutableElement) annotatedElt;
      TypeElement typeElt = (TypeElement) methodElt.getEnclosingElement();
      String translation = translator.translate(methodElt, lang);
      result.put(typeElt.toString().replace('.', '/') + '.' + lang.getExtension(), translation);
    }
    return false;
  }
}
