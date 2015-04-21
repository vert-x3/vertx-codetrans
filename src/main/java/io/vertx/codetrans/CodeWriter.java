package io.vertx.codetrans;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CodeWriter implements Appendable {

  private final Lang lang;
  private int indent = 0;
  private boolean first = true;
  private StringBuilder buffer = new StringBuilder();

  public CodeWriter(Lang lang) {
    this.lang = lang;
  }

  public Lang getLang() {
    return lang;
  }

  public CodeWriter indent() {
    indent += 2;
    return this;
  }

  public CodeWriter unindent() {
    if (indent < 2) {
      throw new IllegalStateException();
    }
    indent -= 2;
    return this;
  }

  public StringBuilder getBuffer() {
    return buffer;
  }

  @Override
  public CodeWriter append(CharSequence csq) {
    return append(csq, 0, csq.length());
  }

  @Override
  public CodeWriter append(CharSequence csq, int start, int end) {
    while (start < end) {
      append(csq.charAt(start++));
    }
    return this;
  }

  @Override
  public CodeWriter append(char c) {
    if (c == '\n') {
      first = true;
    } else if (first) {
      first = false;
      for (int i = 0;i < indent;i++) {
        buffer.append(' ');
      }
    }
    buffer.append(c);
    return this;
  }

  public void renderChars(String value) {
    for (int i = 0;i < value.length();i++) {
      char c = value.charAt(i);
      switch (c) {
        case '\b':
          append("\\b");
          break;
        case '\f':
          append("\\f");
          break;
        case '\n':
          append("\\n");
          break;
        case '\t':
          append("\\t");
          break;
        case '\r':
          append("\\r");
          break;
        case '"':
          append("\\\"");
          break;
        case '\\':
          append("\\\\");
          break;
        default:
          if (c < 32 || c > 126) {
            String s = Integer.toHexString(c).toUpperCase();
            while (s.length() < 4) {
              s = "0" + s;
            }
            append("\\u").append(s);
          } else {
            append(c);
          }
      }
    }
  }
}
