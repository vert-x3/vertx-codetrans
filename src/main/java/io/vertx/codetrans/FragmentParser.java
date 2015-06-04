package io.vertx.codetrans;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public abstract class FragmentParser {

  public void parse(String s) {
    int width = 0;
    int status = 0;
    int margin = 0;
    for (int i = 0;i < s.length();i++) {
      char c = s.charAt(i);
      if (c == '\n') {
        width = 0;
      } else {
        width++;
      }
      if (status == 0) {
        switch (c) {
          case '/':
            status = 1;
            break;
          case '\n':
            onNewline();
            break;
          default:
            break;
        }
      } else if (status == 1) {
        switch (c) {
          case '/':
            status = 2;
            onBeginComment(false);
            break;
          case '*':
            margin = width - 2;
            status = 3;
            onBeginComment(true);
            break;
        }
      } else if (status == 2) {
        switch (c) {
          case '\n': {
            onEndComment(false);
            onNewline();
            status = 0;
            break;
          }
          default:
            onComment(c);
            break;
        }
      } else if (status == 3) {
        switch (c) {
          case '*':
            status = 4;
            break;
          case '\n':
            onComment('\n');
            break;
          case ' ':
            if (width <= margin) {
              for (int j = 0;j < width;j++) {
                if (s.charAt(i - j) != ' ') {
                  onComment(' ');
                  break;
                }
              }
            } else {
              onComment(' ');
            }
            break;
          default:
            onComment(c);
            break;
        }
      } else {
        switch (c) {
          case '/':
            status = 0;
            onEndComment(true);
            break;
          default:
            status = 3;
            onComment('*');
            onComment(c);
            break;
        }
      }
    }
    switch (status) {
      case 2:
        onEndComment(false);
        break;
    }
  }

  protected abstract void onNewline();

  protected abstract void onComment(char c);

  protected abstract void onBeginComment(boolean multiline);

  protected abstract void onEndComment(boolean multiline);

}
