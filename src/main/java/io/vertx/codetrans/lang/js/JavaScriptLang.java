package io.vertx.codetrans.lang.js;

import io.vertx.codegen.Case;
import io.vertx.codegen.type.ClassTypeInfo;
import io.vertx.codetrans.Lang;
import io.vertx.codetrans.Script;
import io.vertx.codetrans.CodeBuilder;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JavaScriptLang implements Lang {

  LinkedHashSet<ClassTypeInfo> modules = new LinkedHashSet<>();

  @Override
  public String id() {
    return "js";
  }

  @Override
  public CodeBuilder codeBuilder() {
    return new JavaScriptCodeBuilder();
  }

  @Override
  public Script loadScript(ClassLoader loader, String path, String method) throws Exception {
    String name = "src/test/generated/js/".replace('/', File.separatorChar) + Stream.of(path.split("/"))
      .map(f -> Case.SNAKE.format(Case.CAMEL.parse(f)))
      .collect(Collectors.joining(File.separator)) + File.separator + Case.SNAKE.format(Case.CAMEL.parse(method)) + ".js";
    File f = new File(name);
    String src = new String(Files.readAllBytes(f.toPath()));
    ScriptEngineManager mgr = new ScriptEngineManager();
    ScriptEngine engine = mgr.getEngineByName("nashorn");
    engine.put("__engine", engine);
    InputStream require = getClass().getClassLoader().getResourceAsStream("vertx-js/util/require.js");
    if (require == null) {
      throw new Exception("Not require.js");
    }
    engine.put(ScriptEngine.FILENAME, "require.js");
    engine.eval(new InputStreamReader(require));
    engine.eval("var console = require('vertx-js/util/console')");
    return new Script() {
      @Override
      public String getSource() {
        return src;
      }

      @Override
      public void run(Map<String, Object> globals) throws Exception {
        engine.setBindings(new SimpleBindings(globals), ScriptContext.GLOBAL_SCOPE);
        engine.eval(src);
      }
    };
  }

  @Override
  public String getExtension() {
    return "js";
  }

}
