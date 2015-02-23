package io.vertx.codetrans;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ResultTest extends ConversionTestBase {

  @Test
  public void testSource() throws Exception {
    Result result = convert(new GroovyLang(), "result/TestResult", "result/TestResult_sourceResult.groovy");
    assertTrue(result instanceof Result.Source);
  }

  @Test
  public void testUnsupported() throws Exception {
    Result result = convert(new GroovyLang(), "result/TestResult", "result/TestResult_unsupportedResult.groovy");
    Result.Failure failure = (Result.Failure) result;
    assertTrue(failure.getCause() instanceof UnsupportedOperationException);
  }
}
