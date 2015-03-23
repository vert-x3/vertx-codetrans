package io.vertx.codetrans;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class LoadingClassLoader extends ClassLoader {

  private final Map<String, Result> results;
  private final Map<String, File> files;

  public LoadingClassLoader(ClassLoader parent, Map<String, Result> results) {
    super(parent);
    this.results = results;
    this.files = new HashMap<>();
  }

  @Override
  protected URL findResource(String name) {
    File file = files.get(name);
    try {
      if (file == null) {
        Result result = results.get(name);
        if (result instanceof Result.Source) {
          File tmp = Files.createTempFile("vertx", "source").toFile();
          tmp.deleteOnExit();
          try (FileWriter writer = new FileWriter(tmp)) {
            writer.append(((Result.Source) result).getValue());
            files.put(name, file = tmp);
          }
        } else if (result instanceof Result.Failure) {
          RuntimeException err = new RuntimeException("Could not load " + name);
          err.initCause(((Result.Failure) result).getCause());
          throw err;
        }
      }
      if (file != null) {
        return file.toURI().toURL();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return super.findResource(name);
  }
}
