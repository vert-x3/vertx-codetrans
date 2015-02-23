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
  public void lineComments() throws Exception {
    // foo
    String t = null;
    // bar
    String s = null;
    // juu
  }

  @CodeTranslate
  public void multiLineComments() throws Exception {
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
  public void lineCommentsInLamba() throws Exception {
    FragmentTest.someMethod(arg -> {
      // foo
      String t = null;
      // bar
      String s = null;
      // juu
    });
  }
}
