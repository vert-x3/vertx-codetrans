package io.vertx.codetrans.lang.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.Script;
import io.vertx.codetrans.Lang;
import io.vertx.codetrans.CodeBuilder;

import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class GroovyLang implements Lang {

  @Override
  public CodeBuilder codeBuilder() {
    return new GroovyCodeBuilder();
  }

  @Override
  public io.vertx.codetrans.Script loadScript(ClassLoader loader, String path) throws Exception {
    InputStream in = loader.getResourceAsStream(path + ".groovy");
    if (in != null) {
      String source = new Scanner(in,"UTF-8").useDelimiter("\\A").next();
      GroovyClassLoader compiler = new GroovyClassLoader(loader);
      Class clazz = compiler.parseClass(new GroovyCodeSource(source, path.replace('/', '.'), "/"));
      return new io.vertx.codetrans.Script() {
        @Override
        public String getSource() {
          return source;
        }

        @Override
        public void run(Map<String, Object> globals) throws Exception {
          Script script = (Script) clazz.newInstance();
          script.setBinding(new Binding(globals));
          script.run();
        }
      };
    }
    throw new Exception("Could not compile " + path);
  }

  @Override
  public String getExtension() {
    return "groovy";
  }
}
