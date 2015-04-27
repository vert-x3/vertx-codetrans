package io.vertx.codetrans;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public abstract class ConversionTestBase {

  public static Lang[] langs() { return new Lang[] { new GroovyLang(), new JavaScriptLang(), new RubyLang() }; }

  public void runJavaScript(String path) {
    run(new JavaScriptLang(), path);
  }

  public void runJavaScript(String path, String method) {
    run(new JavaScriptLang(), path, method);
  }

  public void runGroovy(String path) {
    run(new GroovyLang(), path);
  }

  public void runGroovy(String path, String method) {
    run(new GroovyLang(), path, method);
  }

  public void runRuby(String path) {
    run(new RubyLang(), path);
  }

  public void runRuby(String path, String method) {
    run(new RubyLang(), path, method);
  }


  public void runAll(String path, Runnable after) {
    for (Lang lang : langs()) {
      run(lang, path);
      after.run();
    }
  }

  public void runAll(String path, String method, Runnable after) {
    for (Lang lang : langs()) {
      run(lang, path, method);
      after.run();
    }
  }

  public void runAll(String path, String method, Map<String, Object> globals, Runnable after) {
    for (Lang lang : langs()) {
      run(lang, path, method, globals);
      after.run();
    }
  }

  public void run(Lang lang, String path) {
    run(lang, path, "start");
  }

  public Result convert(Lang lang, String path, String method) {
    return convert(lang, path).get(method);
  }

  public Map<String, Result> convert(Lang lang, String path) {
    try {
      return ConvertingProcessor.convert(ClassIdentifierExpressionTest.class.getClassLoader(), lang, path + ".java");
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  public Script script(Lang lang, String path, String method) {
    Map<String, Result> results = convert(lang, path);
    Thread current = Thread.currentThread();
    ClassLoader prev = current.getContextClassLoader();
    LoadingClassLoader loader = new LoadingClassLoader(current.getContextClassLoader(), results);
    current.setContextClassLoader(loader);
    try {
      return lang.loadScript(loader, path + "_" + method);
    } catch (Exception e) {
      throw new AssertionError(e);
    } finally {
      current.setContextClassLoader(prev);
    }
  }

  public void run(Lang lang, String path, String method) {
    run(lang, path, method, Collections.emptyMap());
  }

  public void run(Lang lang, String path, String method, Map<String, Object> globals) {
    Script script = script(lang, path, method);
    try {
      script.run(globals);
    } catch (Exception e) {
      System.out.println("Script evaluation failed");
      System.out.println(script.getSource());
      throw new AssertionError(e);
    }
  }

  private Object unwrapJsonElement(ScriptObjectMirror obj) {
    if (obj.isArray()) {
      return unwrapJsonArray(obj);
    } else {
      return unwrapJsonObject(obj);
    }
  }

  public JsonObject unwrapJsonObject(ScriptObjectMirror obj) {
    JsonObject unwrapped = new JsonObject();
    for (String key : obj.getOwnKeys(true)) {
      Object value = obj.get(key);
      if (value instanceof ScriptObjectMirror) {
        value = unwrapJsonElement((ScriptObjectMirror) value);
      }
      unwrapped.put(key, value);
    }
    return unwrapped;
  }

  public JsonArray unwrapJsonArray(ScriptObjectMirror obj) {
    JsonArray unwrapped = new JsonArray();
    long len = (long) obj.size();
    for (int i = 0;i < len;i++) {
      Object value = obj.get(i);
      if (value instanceof ScriptObjectMirror) {
        value = unwrapJsonElement((ScriptObjectMirror) value);
      }
      unwrapped.add(value);
    }
    return unwrapped;
  }

  public JsonObject unwrapJsonObject(Map<String, ?> obj) {
    JsonObject ret = new JsonObject();
    obj.forEach((k,v) -> ret.put(k, unwrapJson(v)));
    return ret;
  }

  public JsonArray unwrapJsonArray(List<?> obj) {
    JsonArray ret = new JsonArray();
    obj.forEach(e -> ret.add(unwrapJson(e)));
    return ret;
  }

  private Object unwrapJson(Object o) {
    if (o instanceof Map) {
      return unwrapJsonObject((Map<String, ?>) o);
    } else if (o instanceof List) {
      return unwrapJsonArray((List<?>) o);
    } else {
      return o;
    }
  }
}
