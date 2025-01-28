package undecided.erp.shared.date;

import java.time.LocalDateTime;

/**
 * 適用可能な日付と時間を表します。
 * <p>
 * このクラスは、特定のコンテキストに適用可能な日付と時間で作業するための機能を提供します。
 * 比較、差分の計算、フォーマット、適用可能なコンテキストでの日付と時間の解析など、さまざまな操作を実行するために使用できます。
 */
public class ApplicableDateTime {

  public static final LocalDateTime MAX_DATE_TIME = LocalDateTime.of(10000, 1, 1, 0, 0, 0, 0)
      .minusNanos(100000).minusDays(1);

  public static final ApplicableDateTime MAX = new ApplicableDateTime(MAX_DATE_TIME);
  /**
   * 適用可能な日付と時刻の値を表します。
   *
   * <p>
   * このクラスは、特定のコンテキストで適用可能な日付と時刻の値を操作するための機能を提供します。比較、違いの計算、フォーマット、コンテキストに適用可能な日付と時間の解析などの様々な操作を行うために使用できます。
   * </p>
   *
   * @see ApplicableDateTime
   */
  private final LocalDateTime value;

  private ApplicableDateTime(LocalDateTime value) {
    this.value = value;
  }


  public static ApplicableDateTime of(LocalDateTime value) {
    if (value.isAfter(MAX_DATE_TIME)) {
      throw new IllegalArgumentException("Value cannot be after MAX_DATE_TIME");
    }

    return new ApplicableDateTime(value);
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
