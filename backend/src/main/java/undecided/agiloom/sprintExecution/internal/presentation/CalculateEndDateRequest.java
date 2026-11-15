package undecided.agiloom.sprintExecution.internal.presentation;

import java.time.LocalDate;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

/**
 * スプリント終了日計算リクエスト。
 */
@Builder
@Getter
public class CalculateEndDateRequest {

  /** プロダクトのID。 */
  @NotNull
  private UUID productId;

  /** スプリントの開始日。 */
  @NotNull
  private LocalDate startDate;
}
