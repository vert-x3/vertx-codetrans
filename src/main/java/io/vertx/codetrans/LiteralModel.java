package io.vertx.codetrans;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class LiteralModel {

  public static class Null extends ExpressionModel {

    public Null() {
    }

    @Override
    public void render(CodeWriter writer) {
      writer.getLang().renderNullLiteral(writer);
    }
  }

  public static class String extends ExpressionModel {

    final java.lang.String value;

    public String(java.lang.String value) {
      this.value = value;
    }

    @Override
    public void render(CodeWriter writer) {
      writer.getLang().renderStringLiteral(value, writer);
    }
  }

  public static class Boolean extends ExpressionModel {

    final java.lang.String value;

    public Boolean(java.lang.String value) {
      this.value = value;
    }

    @Override
    public void render(CodeWriter writer) {
      writer.getLang().renderBooleanLiteral(value, writer);
    }
  }

  public static class Integer extends ExpressionModel {

    final java.lang.String value;

    public Integer(java.lang.String value) {
      this.value = value;
    }

    @Override
    public void render(CodeWriter writer) {
      writer.getLang().renderIntegerLiteral(value, writer);
    }
  }

  public static class Long extends ExpressionModel {

    final java.lang.String value;

    public Long(java.lang.String value) {
      this.value = value;
    }

    @Override
    public void render(CodeWriter writer) {
      writer.getLang().renderLongLiteral(value, writer);
    }
  }

  public static class Character extends ExpressionModel {

    final char value;

    public Character(char value) {
      this.value = value;
    }

    @Override
    public void render(CodeWriter writer) {
      writer.getLang().renderCharLiteral(value, writer);
    }
  }

  public static class Float extends ExpressionModel {

    final java.lang.String value;

    public Float(java.lang.String value) {
      this.value = value;
    }

    @Override
    public void render(CodeWriter writer) {
      writer.getLang().renderFloatLiteral(value, writer);
    }
  }

  public static class Double extends ExpressionModel {

    final java.lang.String value;

    public Double(java.lang.String value) {
      this.value = value;
    }

    @Override
    public void render(CodeWriter writer) {
      writer.getLang().renderDoubleLiteral(value, writer);
    }
  }
}
