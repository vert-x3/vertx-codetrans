package io.vertx.codetrans.lang.kotlin;

import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.Lang;
import io.vertx.codetrans.Script;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sergey Mashkov
 */
public class KotlinLang implements Lang {

  @Override
  public String id() {
    return "kotlin";
  }

  @Override
  public File createSourceFile(File root, List<String> className, String methodName) {
    File folder = new File(root, className.stream().collect(Collectors.joining(File.separator)));
    if (methodName != null) {
      folder = new File(folder, methodName);
    }
    return new File(folder.getParent(), folder.getName() + ".kt");
  }

  @Override
  public Script loadScript(ClassLoader loader, String path, String method) throws Exception {
    loader = new URLClassLoader(new URL[]{ new File(new File("target"), "kotlin-classes").toURI().toURL() }, loader);
//    String fqn = path.replace('/', '.') + "." + Character.toUpperCase(method.charAt(0)) + method.substring(1) + "Kt";
//    Class<?> c = loader.loadClass(fqn);
//    Method m = c.getDeclaredMethod(method);
    String fqn = path.replace('/', '.') + "." + method;
    Class<?> c = loader.loadClass(fqn);
    Field accessor = c.getDeclaredField("INSTANCE");
    Method m = c.getDeclaredMethod(method);
    return new Script() {
      @Override
      public String getSource() {
        throw new UnsupportedOperationException();
      }
      @Override
      public void run(Map<String, Object> globals) throws Exception {
        Object instance = accessor.get(null);
        m.invoke(instance);
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
