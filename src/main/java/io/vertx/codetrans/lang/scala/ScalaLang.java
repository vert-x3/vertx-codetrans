package io.vertx.codetrans.lang.scala;

import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.Lang;
import io.vertx.codetrans.Script;
import io.vertx.lang.scala.onthefly.OnTheFlyCompiler;

import java.io.File;
import java.util.Map;

/**
 * Scala language
 *
 * @author <a href="mailto:jochen.mader@codecentric.de">Jochen Mader</a
 */
public class ScalaLang implements Lang {

  scala.Option<File> nopath = scala.Option.<File>empty();

  @Override
  public CodeBuilder codeBuilder() {
    return new ScalaCodeBuilder();
  }

  @Override
  public Script loadScript(ClassLoader loader, String source) throws Exception {

    return new Script() {
      @Override
      public String getSource() {
        return source;
      }

      @Override
      public void run(Map<String, Object> globals) throws Exception {
        new OnTheFlyCompiler(nopath).eval(source);
      }
    };
  }

  @Override
  public String getExtension() {
    return "scala";
  }

}
