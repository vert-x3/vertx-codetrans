package io.vertx.examples;

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

  private final Map<String, String> results;
  private final Map<String, File> files;

  public LoadingClassLoader(ClassLoader parent, Map<String, String> results) {
    super(parent);
    this.results = results;
    this.files = new HashMap<>();
  }

  @Override
  protected URL findResource(String name) {
    File file = files.get(name);
    try {
      if (file == null) {
        String result = results.get(name);
        if (result != null) {
          File tmp = Files.createTempFile("vertx", "source").toFile();
          tmp.deleteOnExit();
          try (FileWriter writer = new FileWriter(tmp)) {
            writer.append(result);
            files.put(name, file = tmp);
          }
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
