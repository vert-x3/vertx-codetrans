package io.vertx.codetrans.lang.js;

import io.vertx.codegen.type.ClassTypeInfo;
import io.vertx.codetrans.Lang;
import io.vertx.codetrans.Script;
import io.vertx.codetrans.CodeBuilder;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JavaScriptLang implements Lang {

  LinkedHashSet<ClassTypeInfo> modules = new LinkedHashSet<>();

  @Override
  public CodeBuilder codeBuilder() {
    return new JavaScriptCodeBuilder();
  }

  @Override
  public Script loadScript(ClassLoader loader, String source) throws Exception {
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
        return source;
      }

      @Override
      public void run(Map<String, Object> globals) throws Exception {
        engine.setBindings(new SimpleBindings(globals), ScriptContext.GLOBAL_SCOPE);
        engine.eval(source);
      }
    };
  }

  @Override
  public String getExtension() {
    return "js";
  }

}
