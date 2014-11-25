package io.vertx.codetrans;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ModuleTest extends ConversionTestBase {

  public static Object buffer;
  public static Object toString;

  @Test
  public void testJavaScriptModule() throws Throwable {
    runJavaScript("module/Module");
    Assert.assertNotNull(buffer);
    Assert.assertEquals("the_buffer", toString);
  }

  @Test
  public void testGroovytModule() throws Throwable {
    runGroovy("module/Module");
//    Assert.assertTrue("Was expecting buffer " + buffer.getClass().getName() + "  instance of " + io.vertx.groovy.core.buffer.Buffer.class.getName(), buffer instanceof io.vertx.groovy.core.buffer.Buffer);
    Assert.assertEquals("the_buffer", toString);
  }
}
