package fragment;

import io.vertx.codetrans.FragmentTest;
import io.vertx.codetrans.annotations.CodeTranslate;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Fragment {

  @CodeTranslate
  public void empty() throws Exception {
    String a = null;
  }

  @CodeTranslate
  public void lineComment() throws Exception {
    // foo
    String t = null;
    // bar
    String s = null;
    // juu
  }

  @CodeTranslate
  public void multiLineComment() throws Exception {
    /*a
      b
     c
    d
   e
  f
     */
    String t = null;
    /*
     * a
     */
  }

  @CodeTranslate
  public void lineCommentInLamba() throws Exception {
    FragmentTest.someMethod(arg -> {
      // foo
      String t = null;
      // bar
      String s = null;
      // juu
    });
  }
}
