package undecided.agiloom.sprintExecution.internal.command;

import org.jspecify.annotations.NonNull;
import undecided.agiloom.sprintExecution.spi.SprintDTO;

/**
 * Sprint作成コマンドサービスのインターフェースです。
 */
public interface CreateSprintCommandServiceInterface {

  /**
   * Sprintを作成します。
   *
   * @param command 作成コマンド
   * @return 生成されたSprintのDTO
   */
  SprintDTO execute(@NonNull CreateSprintCommand command);
}
