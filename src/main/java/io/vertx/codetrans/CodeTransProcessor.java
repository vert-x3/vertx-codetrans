package io.vertx.codetrans;

import io.vertx.core.Verticle;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * A processor plugin generate scripts from {@link io.vertx.core.Verticle} class. It scans all the compiled
 * classes and tries to generate corresponding scripts for each class.<p/>
 *
 * The script is named after the verticle fqn using the last atom of the package name and the lower
 * cased class name, for example : {@code examples.http.Server} maps to {@code http/server.js},
 * {@code http/server.groovy}, etc...<p/>
 *
 * The processor is only active when the option {@code codetrans.output} is set to a valid directory where the scripts
 * will be written. A log <i>codetrans.log</i> will also be written with the processor activity.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CodeTransProcessor extends AbstractProcessor {

  private File outputDir;
  private CodeTranslator translator;
  private List<Lang> langs;

  @Override
  public Set<String> getSupportedOptions() {
    return Collections.singleton("codetrans.output");
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Collections.singleton("*");
  }

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    String outputOption = processingEnv.getOptions().get("codetrans.output");
    if (outputOption != null) {
      outputDir = new File(outputOption);
    }
    translator = new CodeTranslator(processingEnv);
    langs = Arrays.asList(new JavaScriptLang(), new GroovyLang());
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (outputDir != null && (outputDir.exists() || outputDir.mkdirs())) {
      try (PrintWriter log = new PrintWriter(new FileWriter(new File(outputDir, "codetrans.log"), true), true)) {
        TypeMirror verticleType = processingEnv.getElementUtils().getTypeElement(Verticle.class.getName()).asType();
        for (Element rootElt : roundEnv.getRootElements()) {
          Set<Modifier> modifiers = rootElt.getModifiers();
          if (rootElt.getKind() == ElementKind.CLASS &&
              !modifiers.contains(Modifier.ABSTRACT) &&
              modifiers.contains(Modifier.PUBLIC) &&
              processingEnv.getTypeUtils().isSubtype(rootElt.asType(), verticleType)) {
            TypeElement typeElt = (TypeElement) rootElt;
            for (Element enclosedElt : typeElt.getEnclosedElements()) {
              if (enclosedElt.getKind() == ElementKind.METHOD) {
                ExecutableElement methodElt = (ExecutableElement) enclosedElt;
                if (methodElt.getSimpleName().toString().equals("start") && methodElt.getParameters().isEmpty()) {
                  String folderName = processingEnv.getElementUtils().getPackageOf(typeElt).getSimpleName().toString();
                  String fileName = typeElt.getSimpleName().toString().toLowerCase();
                  for (Lang lang : langs) {
                    try {
                      String translation = translator.translate(methodElt, lang);
                      File folder = new File(outputDir, folderName);
                      if (folder.exists() || folder.mkdirs()) {
                        File f = new File(folder, fileName + "." + lang.getExtension());
                        Files.write(f.toPath(), translation.getBytes(), StandardOpenOption.CREATE);
                        log.println("Generated " + f.getAbsolutePath());
                      }
                    } catch (Exception e) {
                      log.print("Skipping generation of " + typeElt.getQualifiedName());
                      e.printStackTrace(log);
                    }
                  }
                }
              }
            }
          }
        }
      } catch (IOException e) {
        e.printStackTrace();;
      }
      return true;
    } else {
      return false;
    }
  }
}
