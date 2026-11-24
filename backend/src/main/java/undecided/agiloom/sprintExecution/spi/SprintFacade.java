package undecided.agiloom.sprintExecution.spi;

import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 他モジュールからスプリントを参照するための公開ファサード。
 *
 * <p>割り当て可能なスプリントの窓（未来4スプリント）の計算もここで行う。
 */
public interface SprintFacade {

  /** IDでスプリントを取得する。 */
  Optional<Sprint> findById(@NonNull SnowflakeId sprintId);

  /**
   * 割り当て可能なスプリント（未来4スプリント）をスプリント番号の昇順で返す。
   *
   * <p>未来4スプリントは進行中スプリントを含めて4つと数える（#650）。進行中スプリントが
   * ない場合は最も早い計画中スプリントから4つとする。スプリント0・完了・キャンセル済みは
   * 窓に含めない。
   */
  List<Sprint> findAssignableSprints(@NonNull SnowflakeId teamId);

  /**
   * 指定したスプリント番号より大きい最小番号のスプリントを返す。スプリント0は対象にしない。
   * キャンセル時のスライド先の決定に使う。
   */
  Optional<Sprint> findNextSprint(@NonNull SnowflakeId teamId, int afterSprintNumber);
}
