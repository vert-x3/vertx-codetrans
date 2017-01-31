package io.vertx.codetrans.lang.scala;

import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.Lang;
import io.vertx.codetrans.Script;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Scala language
 *
 * @author <a href="mailto:jochen.mader@codecentric.de">Jochen Mader</a
 */
public class ScalaLang implements Lang {

  @Override
  public CodeBuilder codeBuilder() {
    return new ScalaCodeBuilder();
  }

  @Override
  public File createSourceFile(File root, List<String> className, String methodName) {
    File folder = new File(root, className.stream().collect(Collectors.joining(File.separator)));
    if (methodName != null) {
      folder = new File(folder, methodName);
    }
    return new File(folder.getParent(), folder.getName() + ".scala");
  }

  @Override
  public Script loadScript(ClassLoader loader, String path, String method) throws Exception {
    loader = new URLClassLoader(new URL[]{ new File(new File("target"), "scala-classes").toURI().toURL() }, loader);
    String fqn = path.replace('/', '.') + '.' + method;
    Class<?> clazz = loader.loadClass(fqn);
    Object instance = clazz.newInstance();
    Method m = clazz.getDeclaredMethod("apply");
    return new Script() {
      @Override
      public String getSource() {
        throw new UnsupportedOperationException();
      }
      @Override
      public void run(Map<String, Object> globals) throws Exception {
        try {
          m.invoke(instance);
        } catch (InvocationTargetException e) {
          rethrowAny(e.getCause());
        }
      }
    };
  }

  @Override
  public String getExtension() {
    return "scala";
  }

  @SuppressWarnings("unchecked")
  private static <T extends Throwable> void throwThis(Throwable exception) throws T {
    throw (T) exception;
  }

  private static void rethrowAny(Throwable exception) {
    ScalaLang.throwThis(exception);
  }
}
