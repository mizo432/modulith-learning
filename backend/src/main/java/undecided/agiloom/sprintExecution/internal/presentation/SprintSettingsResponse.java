package undecided.agiloom.sprintExecution.internal.presentation;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import undecided.agiloom.sprintExecution.spi.EndDayOfWeek;

/**
 * スプリント設定レスポンス。
 */
@Builder
@Getter
public class SprintSettingsResponse {

  /** プロダクトのID。 */
  private UUID productId;

  /** スプリント期間（営業日数）。 */
  private Integer businessDays;

  /** スプリント終了曜日。 */
  private EndDayOfWeek endDayOfWeek;
}
