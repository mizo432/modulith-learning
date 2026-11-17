package undecided.agiloom.sprintExecution.internal.presentation;

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
  private String productId;

  /** スプリント期間（営業日数）。 */
  private Integer businessDays;

  /** スプリント終了曜日。 */
  private EndDayOfWeek endDayOfWeek;
}
