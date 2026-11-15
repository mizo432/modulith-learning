package undecided.agiloom.sprintExecution.internal.presentation;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

/**
 * スプリント終了日計算レスポンス。
 */
@Builder
@Getter
public class CalculateEndDateResponse {

  /** 計算された終了日。 */
  private LocalDate endDate;

  /** 開始日。 */
  private LocalDate startDate;

  /** 営業日数。 */
  private Integer businessDays;

  /** 終了曜日の表示名（例: "金曜日"）。 */
  private String endDayOfWeekLabel;

  /** 終了日が過去の日付かどうか。 */
  private Boolean isPastDate;
}
