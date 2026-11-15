package undecided.agiloom.sprintExecution.internal.presentation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import undecided.agiloom.sprintExecution.spi.EndDayOfWeek;

/**
 * スプリント設定更新リクエスト。
 */
@Builder
@Getter
public class UpdateSprintSettingsRequest {

  /** プロダクトのID。 */
  @NotNull
  private UUID productId;

  /** スプリント期間（営業日数）。1〜30日の範囲。 */
  @NotNull
  @Min(value = 1, message = "businessDays must be at least 1")
  @Max(value = 30, message = "businessDays must be 30 or less")
  private Integer businessDays;

  /** スプリント終了曜日。 */
  @NotNull
  private EndDayOfWeek endDayOfWeek;
}
