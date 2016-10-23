package io.vertx.codetrans;

import io.vertx.codetrans.lang.scala.ScalaLang;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
//TODO: I have no idea how to make these work for scala ....
public class AsyncResultTest extends ConversionTestBase {

  public static void callbackWithFailure(Handler<AsyncResult> callback) {
    callback.handle(Future.failedFuture("oh no"));
  }

  public static String path;

  @Before
  public void before() throws Exception {
    File tmp = File.createTempFile("vertx", "tmp");
    tmp.deleteOnExit();
    try (FileWriter writer = new FileWriter(tmp)) {
      writer.append("hello");
    }
    path = tmp.getAbsolutePath();
  }

  private static Boolean succeeded;
  private static Object result;
  private static CountDownLatch resultLatch;
  public static void setResult(Object result, Boolean succeeded) {
    AsyncResultTest.result = result;
    AsyncResultTest.succeeded = succeeded;
    resultLatch.countDown();
  }

  @Test
  public void testAsyncResultHandlerSucceeded() throws Exception {
    for (Lang lang : langs()) {
      if(ScalaLang.class.equals(lang.getClass())) continue;
      resultLatch = new CountDownLatch(1);
      result = null;
      run(lang, "asyncresult/AsyncResultHandler", "succeeded");
      Assert.assertTrue(resultLatch.await(10, TimeUnit.SECONDS));
      Assert.assertEquals("hello", result);
      Assert.assertEquals(Boolean.TRUE, succeeded);
    }
  }

  private static Boolean failed;
  private static Throwable cause;
  private static CountDownLatch causeLatch;
  public static void setCause(Throwable cause, Boolean failed) {
    AsyncResultTest.cause = cause;
    AsyncResultTest.failed = failed;
    causeLatch.countDown();
  }

  @Test
  public void testAsyncResultHandlerFailed() throws Exception {
    for (Lang lang : langs()) {
      if(ScalaLang.class.equals(lang.getClass())) continue;
      causeLatch = new CountDownLatch(1);
      cause = null;
      run(lang, "asyncresult/AsyncResultHandler", "failed");
      causeLatch.await(10, TimeUnit.SECONDS);
      Assert.assertNotNull(cause);
      Assert.assertEquals("oh no", cause.getMessage());
      Assert.assertEquals(Boolean.TRUE, failed);
    }
  }

  @Test
  public void analyzeSucceeded() throws Exception {
    for (Lang lang : langs()) {
      resultLatch = new CountDownLatch(1);
      result = null;
      run(lang, "asyncresult/AsyncResultHandler", "analyzeSucceeded");
      resultLatch.await(10, TimeUnit.SECONDS);
      Assert.assertTrue(resultLatch.await(10, TimeUnit.SECONDS));
      Assert.assertEquals("hello", result);
      Assert.assertEquals(Boolean.TRUE, succeeded);
    }
  }

  @Test
  public void analyzeFailed() throws Exception {
    for (Lang lang : langs()) {
      if(ScalaLang.class.equals(lang.getClass())) continue;
      causeLatch = new CountDownLatch(1);
      cause = null;
      run(lang, "asyncresult/AsyncResultHandler", "analyzeFailed");
      causeLatch.await(10, TimeUnit.SECONDS);
      Assert.assertNotNull(cause);
      Assert.assertEquals("oh no", cause.getMessage());
      Assert.assertEquals(Boolean.TRUE, failed);
    }
  }
}
