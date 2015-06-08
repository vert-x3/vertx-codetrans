package io.vertx.codetrans.expression;

import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeWriter;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MapClassModel extends ClassModel {

  public MapClassModel(CodeBuilder builder) {
    super(builder);
  }

  @Override
  public ExpressionModel onNew(List<ExpressionModel> arguments) {
    switch (arguments.size()) {
      case 0:
        return new MapModel(builder, this);
      default:
        throw new UnsupportedOperationException();
    }
  }

  @Override
  public void render(CodeWriter writer) {
    writer.renderNewMap();
  }
}
