package io.vertx.codetrans;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public interface Lang {

  Script loadScript(ClassLoader loader, String source) throws Exception;

  String getExtension();

  CodeBuilder codeBuilder();

}
