package io.vertx.codetrans;

import io.vertx.codetrans.annotations.CodeTranslate;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ConvertingProcessor extends AbstractProcessor {

  private static final javax.tools.JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
  private static final Locale locale = Locale.getDefault();
  private static final Charset charset = Charset.forName("UTF-8");

  public static Map<Lang, Result> convert(ClassLoader loader, List<Lang> langs, String source, String fqn, String method) throws Exception {
    URL url = loader.getResource(source);
    if (url == null) {
      throw new Exception("Cannot resolve source " + source + "");
    }
    String file = new File(url.toURI()).getAbsolutePath();
    return convertFromFiles(loader, langs, file, fqn, method);
  }

  public static Map<Lang, Result> convertFromFiles(ClassLoader loader, List<Lang> lang, String file, String fqn, String method) throws Exception {
    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
    StandardJavaFileManager manager = javac.getStandardFileManager(diagnostics, locale, charset);
    Iterable<? extends JavaFileObject> fileObjects = manager.getJavaFileObjects(file);
    StringWriter out = new StringWriter();
    JavaCompiler.CompilationTask task = javac.getTask(
      out,
      manager,
      diagnostics,
      Collections.<String>emptyList(),
      Collections.<String>emptyList(),
      fileObjects);
    task.setLocale(locale);
    ConvertingProcessor processor = new ConvertingProcessor(lang, fqn, method);
    task.setProcessors(Collections.<Processor>singletonList(processor));
    if (task.call()) {
      return processor.getResults();
    } else {
      StringWriter message = new StringWriter();
      PrintWriter writer = new PrintWriter(message);
      writer.append("Compilation of ").append(file).println(" failed:");
      for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics())  {
        writer.append(diagnostic.getMessage(locale));
      }
      writer.println("console:");
      writer.append(out.getBuffer());
      throw new Exception(message.toString());
    }
  }

  private Map<Lang, Result> results = new LinkedHashMap<>();
  private List<Lang> langs;
  private final String fqn;
  private final String method;
  private CodeTranslator translator;

  public ConvertingProcessor(List<Lang> langs, String fqn, String method) {
    this.langs = langs;
    this.fqn = fqn;
    this.method = method;
  }

  public Map<Lang, Result> getResults() {
    return results;
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
      if (typeElt.getQualifiedName().toString().equals(fqn) && methodElt.getSimpleName().toString().equals(method)) {
        for (Lang lang : langs) {
          Result result;
          try {
            String translation = translator.translate(methodElt, false, lang, RenderMode.SNIPPET);
            result = new Result.Source(translation);
          } catch (Exception e) {
            result = new Result.Failure(e);
          }
          results.put(lang, result);
        }
      }
    }
    return false;
  }
}
