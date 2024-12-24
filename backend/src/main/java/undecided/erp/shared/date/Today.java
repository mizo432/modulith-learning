package undecided.erp.shared.date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.lang.NonNull;
import undecided.erp.common.dateProvider.DateProvider;

/**
 * {@link LocalDate} をカプセル化したレコードで、今日の日付を表します。
 * <p>
 * このクラスは、カプセル化された日付を操作するためのユーティリティメソッド（1か月を加算したり、 現在の日付を元に新しいインスタンスを生成するなど）を提供します。
 * <p>
 * {@code Today} レコードは、不変性を確保し、日付処理に特化した機能を提供します。
 */
public record Today(LocalDate value) {

  public static Today of(@NonNull LocalDate value) {
    return new Today(value);

  }

  public static Today newInstance() {
    return new Today(DateProvider.currentLocalDate());

  }

  public LocalDate addOneMonth() {
    return value.plusMonths(1);
  }

  public int nextMonthInteger() {
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
    return Integer.parseInt(value.format(df));

  }
}
