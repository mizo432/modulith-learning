package undecided.agiloom.sprintExecution.internal.command;

import org.jspecify.annotations.NonNull;
import undecided.agiloom.sprintExecution.spi.SprintDTO;
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
   * @return 更新されたSprintのDTO
   */
  SprintDTO execute(@NonNull UpdateSprintCommand command);

  /**
   * Sprintのステータスを変更します。
   *
   * @param id SprintのID
   * @param status 変更後のステータス
   * @return 更新されたSprintのDTO
   */
  SprintDTO changeStatus(@NonNull SnowflakeId id, @NonNull SprintStatus status);
}
