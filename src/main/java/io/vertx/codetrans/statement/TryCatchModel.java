package io.vertx.codetrans.statement;

import io.vertx.codetrans.CodeWriter;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TryCatchModel extends StatementModel {

  final StatementModel tryBlock;
  final StatementModel catchBlock;

  public TryCatchModel(StatementModel tryBlock, StatementModel catchBlock) {
    this.tryBlock = tryBlock;
    this.catchBlock = catchBlock;
  }

  public StatementModel getTryBlock() {
    return tryBlock;
  }

  public StatementModel getCatchBlock() {
    return catchBlock;
  }

  @Override
  public void render(CodeWriter writer) {
    writer.renderTryCatch(tryBlock, catchBlock);
  }
}
