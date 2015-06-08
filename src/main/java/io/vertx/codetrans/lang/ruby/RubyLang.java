package io.vertx.codetrans.lang.ruby;

import io.vertx.codetrans.Lang;
import io.vertx.codetrans.Script;
import io.vertx.codetrans.CodeBuilder;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.ScriptingContainer;

import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RubyLang implements Lang {

  @Override
  public CodeBuilder codeBuilder() {
    return new RubyCodeBuilder();
  }

  @Override
  public Script loadScript(ClassLoader loader, String path) throws Exception {
    String filename = path + ".rb";
    InputStream in = loader.getResourceAsStream(filename);
    if (in == null) {
      throw new Exception("Could not find " + filename);
    }
    String source = new Scanner(in, "UTF-8").useDelimiter("\\A").next();
    ScriptingContainer container = new ScriptingContainer(LocalContextScope.SINGLETHREAD);
    return new Script() {
      @Override
      public String getSource() {
        return source;
      }

      @Override
      public void run(Map<String, Object> globals) {
        for (Map.Entry<String, Object> global : globals.entrySet()) {
          container.put("$" + global.getKey(), global.getValue());
        }
        container.runScriptlet(source);
      }
    };
  }

  @Override
  public String getExtension() {
    return "rb";
  }

}
