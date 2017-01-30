package io.vertx.codetrans;

import io.vertx.codegen.Case;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public interface Lang {

  Script loadScript(ClassLoader loader, String path, String method) throws Exception;

  default File createSourceFile(File root, List<String> className, String methodName) {
    Stream.Builder<String> builder = Stream.builder();
    className.forEach(builder::add);
    if (methodName != null) {
      builder.add((methodName));
    }
    String t = builder
      .build()
      .map(s -> Case.SNAKE.format(Case.CAMEL.parse(s)).replace('.', File.separatorChar))
      .collect(Collectors.joining(File.separator));
    return new File(root, t + "." + getExtension());
  }

  String getExtension();

  CodeBuilder codeBuilder();

}
