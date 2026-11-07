package undecided.agiloom.sprintExecution.internal.command;

import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * Sprint作成コマンドサービスのインターフェースです。
 */
public interface CreateSprintCommandServiceInterface {

  /**
   * Sprintを作成します。
   *
   * @param command 作成コマンド
   * @return 生成されたSprintのID
   */
  SnowflakeId execute(@NonNull CreateSprintCommand command);
}
