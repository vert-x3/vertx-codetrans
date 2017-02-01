package io.vertx.codetrans.lang.ruby;

import io.vertx.codegen.Case;
import io.vertx.codetrans.Lang;
import io.vertx.codetrans.Script;
import io.vertx.codetrans.CodeBuilder;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.ScriptingContainer;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RubyLang implements Lang {

  @Override
  public String id() {
    return "ruby";
  }

  @Override
  public CodeBuilder codeBuilder() {
    return new RubyCodeBuilder();
  }

  @Override
  public Script loadScript(ClassLoader loader, String path, String method) throws Exception {
    String name = "src/test/generated/ruby/".replace('/', File.separatorChar) + Stream.of(path.split("/"))
      .map(f -> Case.SNAKE.format(Case.CAMEL.parse(f)))
      .collect(Collectors.joining(File.separator)) + File.separator + Case.SNAKE.format(Case.CAMEL.parse(method)) + ".rb";
    File f = new File(name);
    String src = new String(Files.readAllBytes(f.toPath()));
    ScriptingContainer container = new ScriptingContainer(LocalContextScope.SINGLETHREAD);
    return new Script() {
      @Override
      public String getSource() {
        return src;
      }

      @Override
      public void run(Map<String, Object> globals) {
        for (Map.Entry<String, Object> global : globals.entrySet()) {
          container.put("$" + global.getKey(), global.getValue());
        }
        container.runScriptlet(src);
      }
    };
  }

  @Override
  public String getExtension() {
    return "rb";
  }

}
