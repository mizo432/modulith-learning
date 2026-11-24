package undecided.agiloom.requirementBacklog.spi;

import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 他モジュールからスプリント単位のストーリーポイント集計を参照するためのクエリ。
 *
 * <p>スプリント容量（ベロシティ・割り当て可能ポイント）の計算に使う。
 */
public interface SprintPointsQuery {

  /**
   * 指定したスプリントに割り当て済みのバックログアイテムのストーリーポイント合計を返す。
   *
   * <p>ステータスは不問。ストーリーポイント未設定のアイテムは0として扱う。
   */
  long sumAssignedStoryPoints(@NonNull SnowflakeId sprintId);

  /**
   * 指定したスプリントに割り当て済みで、完了（DONE）したバックログアイテムの
   * ストーリーポイント合計を返す。
   *
   * <p>ストーリーポイント未設定のアイテムは0として扱う。
   */
  long sumCompletedStoryPoints(@NonNull SnowflakeId sprintId);
}
