package io.vertx.codetrans;

/**
 * Control how code is rendered.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public enum RenderMode {

  /**
   * A snippet of code, for documentation purpose, the code does not have to execute.
   */
  SNIPPET,

  /**
   * Render to run as a Vert.x Verticle.
   */
  VERTICLE,

  /**
   * Render to be tested by codetrans.
   */
  TEST

}
