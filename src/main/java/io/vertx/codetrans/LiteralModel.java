package io.vertx.codetrans;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class LiteralModel {

  public static class Null extends ExpressionModel {

    public Null(Lang lang) {
      super(lang);
    }

    @Override
    public void render(CodeWriter writer) {
      writer.renderNullLiteral();
    }
  }

  public static class String extends ExpressionModel {

    final java.lang.String value;

    public String(Lang lang, java.lang.String value) {
      super(lang);
      this.value = value;
    }

    @Override
    public void render(CodeWriter writer) {
      writer.renderStringLiteral(value);
    }
  }

  public static class Boolean extends ExpressionModel {

    final java.lang.String value;

    public Boolean(Lang lang, java.lang.String value) {
      super(lang);
      this.value = value;
    }

    @Override
    public void render(CodeWriter writer) {
      writer.renderBooleanLiteral(value);
    }
  }

  public static class Integer extends ExpressionModel {

    final java.lang.String value;

    public Integer(Lang lang, java.lang.String value) {
      super(lang);
      this.value = value;
    }

    @Override
    public void render(CodeWriter writer) {
      writer.renderIntegerLiteral(value);
    }
  }

  public static class Long extends ExpressionModel {

    final java.lang.String value;

    public Long(Lang lang, java.lang.String value) {
      super(lang);
      this.value = value;
    }

    @Override
    public void render(CodeWriter writer) {
      writer.renderLongLiteral(value);
    }
  }

  public static class Character extends ExpressionModel {

    final char value;

    public Character(Lang lang, char value) {
      super(lang);
      this.value = value;
    }

    @Override
    public void render(CodeWriter writer) {
      writer.renderCharLiteral(value);
    }
  }

  public static class Float extends ExpressionModel {

    final java.lang.String value;

    public Float(Lang lang, java.lang.String value) {
      super(lang);
      this.value = value;
    }

    @Override
    public void render(CodeWriter writer) {
      writer.renderFloatLiteral(value);
    }
  }

  public static class Double extends ExpressionModel {

    final java.lang.String value;

    public Double(Lang lang, java.lang.String value) {
      super(lang);
      this.value = value;
    }

    @Override
    public void render(CodeWriter writer) {
      writer.renderDoubleLiteral(value);
    }
  }
}
