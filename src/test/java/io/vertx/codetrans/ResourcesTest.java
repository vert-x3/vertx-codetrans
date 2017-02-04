package io.vertx.codetrans;

import org.junit.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ResourcesTest extends ConversionTestBase {

  @Test
  public void testCopyResource() throws Exception {
    String[] paths = {
      "src/test/generated/js/resources/resources/file.txt",
      "src/test/generated/groovy/resources/resources/file.txt",
      "src/test/generated/ruby/resources/resources/file.txt",
      "src/test/generated/kotlin/resources/Resources/file.txt",
      "src/test/generated/scala/resources/Resources/file.txt",
    };
    for (String path : paths) {
      File file = new File(path.replace('/', File.separatorChar));
      assertTrue(file.exists());
      assertEquals("the-file", new String(Files.readAllBytes(file.toPath())).trim());
    }
  }
}
