package undecided.agiloom.sprintExecution.internal.domain;

import static com.github.mizo432.shared.common.precondition.ObjectPrecondition.checkNotNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import undecided.agiloom.sprintExecution.spi.SprintSettings;
import undecided.generic.calendarReg.spi.Holiday;
import undecided.generic.calendarReg.spi.HolidayQuery;

/**
 * スプリント終了日計算用のドメインサービス。
 *
 * <p>営業日数、終了曜日、祝日情報を基にスプリントの終了日を計算します。
 */
@Component
public class SprintSettingsCalculator {

  private final HolidayQuery holidayQuery;

  public SprintSettingsCalculator(HolidayQuery holidayQuery) {
    this.holidayQuery = holidayQuery;
  }

  /**
   * 開始日からスプリントの終了日を計算します。
   *
   * <p>営業日数分だけ営業日（土日・祝日除外）を進め、
   * 終了日が指定された終了曜日になるように調整します。
   *
   * @param settings スプリント設定
   * @param startDate スプリントの開始日
   * @return 計算された終了日
   */
  @NonNull
  public LocalDate calculateEndDate(@NonNull SprintSettings settings, @NonNull LocalDate startDate) {
    checkNotNull(settings, () -> new IllegalArgumentException("settings must not be null"));
    checkNotNull(startDate, () -> new IllegalArgumentException("startDate must not be null"));

    // 開始日から前方に十分な範囲の祝日を取得
    LocalDate searchEndDate = startDate.plusDays(60); // 最大30営業日 + 土日・祝日分の余裕
    List<Holiday> holidays = holidayQuery.findBetween(startDate, searchEndDate);
    Set<LocalDate> holidayDates = new HashSet<>();
    for (Holiday holiday : holidays) {
      holidayDates.add(holiday.getHolidayDate());
    }

    return settings.calculateEndDate(startDate, holidayDates);
  }
}
