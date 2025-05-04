package undecided.erp.shared.date;

import static undecided.erp.common.primitive.Objects2.isNull;

import java.time.LocalDate;
import java.time.YearMonth;
import lombok.AllArgsConstructor;

/**
 * 会計の期首日付と期末日付を表ます。
 */
@AllArgsConstructor
public class AccountDatePeriod {

  private final DateInterval interval;

  /**
   * 提供されたAccountPeriodConfigとtargetDateを元にAccountDatePeriodを取得する 'performFrom' 操作を実行します。
   *
   * @param accountPeriodConfig クエリ期間の開始月を決定するために使用されるAccountPeriodConfig。
   * @param targetDate AccountDatePeriodを計算するための対象日。
   * @return 与えられた対象日の照会期間を示すAccountDatePeriod。
   */
  static AccountDatePeriod performFrom(AccountPeriodConfig accountPeriodConfig,
      ApplicableDate targetDate) {
    ApplicableMonth targetMonth = targetDate.getMonth();
    ApplicableMonth beginMonth = accountPeriodConfig.beginMonth();

    YearMonth beginYearMonth;
    YearMonth endYearMonth;
    if (targetMonth.isBefore(beginMonth)) {
      int targetYear = targetDate.getValue().getYear() - 1;
      beginYearMonth = YearMonth.of(targetYear, beginMonth.getValue());
      endYearMonth = YearMonth.of(targetYear, beginMonth.getValue());
    } else {
      int targetYear = targetDate.getValue().getYear();
      beginYearMonth = YearMonth.of(targetYear, beginMonth.getValue());
      endYearMonth = YearMonth.of(targetYear, beginMonth.getValue()).plusMonths(1L);

    }
    LocalDate beginDate = beginYearMonth.atDay(1);
    LocalDate endDate = endYearMonth.atEndOfMonth();

    return new AccountDatePeriod(
        DateInterval.create(ApplicableDate.of(beginDate),
            ApplicableDate.of(endDate)));

  }

  @Override
  public String toString() {
    if (isNull(interval)) {
      return null;
    }

    return interval.toString();
  }

  public ApplicableDate getBeginDate() {
    if (isNull(interval)) {
      return ApplicableDate.EMPTY;
    }
    return interval.getBeginDate();
  }

  public ApplicableDate getEndDate() {
    if (isNull(interval)) {
      return ApplicableDate.EMPTY;
    }
    return interval.getEndDate();
  }
}
