package io.vertx.codetrans.expression;

import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeWriter;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class LiteralModel {

  public static class Null extends ExpressionModel {

    public Null(CodeBuilder builder) {
      super(builder);
    }

    @Override
    public void render(CodeWriter writer) {
      writer.renderNullLiteral();
    }
  }

  public static class String extends ExpressionModel {

    final java.lang.String value;

    public String(CodeBuilder builder, java.lang.String value) {
      super(builder);
      this.value = value;
    }

    @Override
    public boolean isStringDecl() {
      return true;
    }

    @Override
    void collectParts(List<Object> parts) {
      parts.add(value);
    }

    public java.lang.String getValue() {
      return value;
    }

    @Override
    public void render(CodeWriter writer) {
      writer.renderStringLiteral(value);
    }
  }

  public static class Boolean extends ExpressionModel {

    final java.lang.String value;

    public Boolean(CodeBuilder builder, java.lang.String value) {
      super(builder);
      this.value = value;
    }

    @Override
    public void render(CodeWriter writer) {
      writer.renderBooleanLiteral(value);
    }
  }

  public static class Integer extends ExpressionModel {

    final java.lang.String value;

    public Integer(CodeBuilder builder, java.lang.String value) {
      super(builder);
      this.value = value;
    }

    @Override
    public void render(CodeWriter writer) {
      writer.renderIntegerLiteral(value);
    }
  }

  public static class Long extends ExpressionModel {

    final java.lang.String value;

    public Long(CodeBuilder builder, java.lang.String value) {
      super(builder);
      this.value = value;
    }

    @Override
    public void render(CodeWriter writer) {
      writer.renderLongLiteral(value);
    }
  }

  public static class Character extends ExpressionModel {

    final char value;

    public Character(CodeBuilder builder, char value) {
      super(builder);
      this.value = value;
    }

    @Override
    public void render(CodeWriter writer) {
      writer.renderCharLiteral(value);
    }
  }

  public static class Float extends ExpressionModel {

    final java.lang.String value;

    public Float(CodeBuilder builder, java.lang.String value) {
      super(builder);
      this.value = value;
    }

    @Override
    public void render(CodeWriter writer) {
      writer.renderFloatLiteral(value);
    }
  }

  public static class Double extends ExpressionModel {

    final java.lang.String value;

    public Double(CodeBuilder builder, java.lang.String value) {
      super(builder);
      this.value = value;
    }

    @Override
    public void render(CodeWriter writer) {
      writer.renderDoubleLiteral(value);
    }
  }
}
