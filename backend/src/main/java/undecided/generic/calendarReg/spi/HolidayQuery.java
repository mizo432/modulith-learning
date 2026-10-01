package undecided.generic.calendarReg.spi;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

/** 祝日情報を検索するためのクエリインターフェース。 */
public interface HolidayQuery {

  /**
   * 指定された日付の祝日エンティティを検索します。
   *
   * @param date 検索対象の日付
   * @return 該当する祝日エンティティの Optional
   */
  @NonNull Optional<Holiday> findByHolidayDate(@NonNull LocalDate date);

  /**
   * 指定された年のすべての祝日を取得します。
   *
   * @param year 対象年 (例: 2024)
   * @return 該当する祝日エンティティのリスト（日付昇順）
   */
  @NonNull List<Holiday> findByYear(int year);

  /**
   * 指定された期間内のすべての祝日を取得します。
   *
   * @param from 開始日（含む）
   * @param to 終了日（含む）
   * @return 該当する祝日エンティティのリスト（日付昇順）
   */
  @NonNull List<Holiday> findBetween(@NonNull LocalDate from, @NonNull LocalDate to);
}
