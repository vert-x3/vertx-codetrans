package io.vertx.codetrans.lang.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;
import io.vertx.codegen.Case;
import io.vertx.codetrans.Lang;
import io.vertx.codetrans.CodeBuilder;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class GroovyLang implements Lang {

  @Override
  public String id() {
    return "groovy";
  }

  @Override
  public CodeBuilder codeBuilder() {
    return new GroovyCodeBuilder();
  }

  @Override
  public io.vertx.codetrans.Script loadScript(ClassLoader loader, String path, String method) throws Exception {
    String name = "src/test/generated/groovy/".replace('/', File.separatorChar) + Stream.of(path.split("/"))
      .map(f -> Case.SNAKE.format(Case.CAMEL.parse(f)))
      .collect(Collectors.joining(File.separator));
    loader = new URLClassLoader(new URL[]{ new File(name).toURI().toURL() }, loader);
    File f = new File(name + File.separator + Case.SNAKE.format(Case.CAMEL.parse(method)) + ".groovy");
    String src = new String(Files.readAllBytes(f.toPath()));
    GroovyClassLoader compiler = new GroovyClassLoader(loader);
    Class clazz = compiler.parseClass(src);
    return new io.vertx.codetrans.Script() {
      @Override
      public String getSource() {
        return src;
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
