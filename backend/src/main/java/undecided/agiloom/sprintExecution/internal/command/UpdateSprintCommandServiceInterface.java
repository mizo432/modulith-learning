package undecided.agiloom.sprintExecution.internal.command;

import org.jspecify.annotations.NonNull;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * Sprint更新コマンドサービスのインターフェースです。
 */
public interface UpdateSprintCommandServiceInterface {

  /**
   * Sprintを更新します。
   *
   * @param command 更新コマンド
   */
  void execute(@NonNull UpdateSprintCommand command);

  /**
   * Sprintのステータスを変更します。
   *
   * @param id SprintのID
   * @param status 変更後のステータス
   */
  void changeStatus(@NonNull SnowflakeId id, @NonNull SprintStatus status);
}
