package io.vertx.codetrans.lang.kotlin;

import groovy.json.internal.Charsets;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.Lang;
import io.vertx.codetrans.Script;
import io.vertx.lang.kotlin.KotlinCompilerHelper;
import org.jetbrains.kotlin.descriptors.ClassKind;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.Map;

/**
 * @author Sergey Mashkov
 */
public class KotlinLang implements Lang {
  @Override
  public Script loadScript(ClassLoader loader, String source) throws Exception {
    return new Script() {
      @Override
      public String getSource() {
        return source;
      }

      @Override
      public void run(Map<String, Object> globals) throws Exception {
        File tmp = File.createTempFile("kc_", ".kts");
        try {
          Files.write(tmp.toPath(), source.getBytes(Charsets.UTF_8));

          KotlinCompilerHelper.INSTANCE.compileKotlinScript(loader, true, tmp.toURI().toURL(), (generationState, classDescriptor) ->
            classDescriptor.getKind() == ClassKind.CLASS
          ).forEach(aClass -> {
            try {
              aClass.getConstructor(String[].class).newInstance((Object) new String[0]);
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ignore) {
            }
          });
        } finally {
          if (!tmp.delete() && tmp.exists()) {
            // log warn?
          }
        }
      }
    };
  }

  @Override
  public String getExtension() {
    return "kt";
  }

  @Override
  public CodeBuilder codeBuilder() {
    return new KotlinCodeBuilder();
  }
}
