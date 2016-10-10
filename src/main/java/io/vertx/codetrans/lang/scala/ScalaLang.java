package io.vertx.codetrans.lang.scala;

import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.Lang;
import io.vertx.codetrans.Script;

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
  public Script loadScript(ClassLoader loader, String source) throws Exception {
    throw new RuntimeException("can't compile on the fly");
  }

  @Override
  public String getExtension() {
    return "scala";
  }

}
