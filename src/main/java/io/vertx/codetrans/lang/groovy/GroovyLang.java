package io.vertx.codetrans.lang.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;
import io.vertx.codetrans.Lang;
import io.vertx.codetrans.CodeBuilder;

import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class GroovyLang implements Lang {

  @Override
  public CodeBuilder codeBuilder() {
    return new GroovyCodeBuilder();
  }

  @Override
  public io.vertx.codetrans.Script loadScript(ClassLoader loader, String source) throws Exception {
    GroovyClassLoader compiler = new GroovyClassLoader(loader);
    Class clazz = compiler.parseClass(source);
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

  @Override
  public String getExtension() {
    return "groovy";
  }
}
