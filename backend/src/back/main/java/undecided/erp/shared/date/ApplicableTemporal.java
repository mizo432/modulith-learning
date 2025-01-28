package undecided.erp.shared.date;

import java.time.LocalDateTime;
import java.time.temporal.Temporal;

/**
 * 適用可能な日付と時間を表します。
 * <p>
 * このクラスは、特定のコンテキストに適用可能な日付と時間で作業するための機能を提供します。
 * 比較、差分の計算、フォーマット、適用可能なコンテキストでの日付と時間の解析など、さまざまな操作を実行するために使用できます。
 */
public class ApplicableTemporal {

  public static final Temporal
      MAX_DATE_TIME = LocalDateTime.of(10000, 1, 1, 0, 0, 0, 0)
      .minusNanos(100000).minusDays(1);

  public static final ApplicableTemporal MAX = new ApplicableTemporal(MAX_DATE_TIME);
  /**
   * 適用可能な日付と時刻の値を表します。
   *
   * <p>
   * このクラスは、特定のコンテキストで適用可能な日付と時刻の値を操作するための機能を提供します。比較、違いの計算、フォーマット、コンテキストに適用可能な日付と時間の解析などの様々な操作を行うために使用できます。
   * </p>
   *
   * @see ApplicableTemporal
   */
  private final Temporal value;

  private ApplicableTemporal(Temporal value) {
    this.value = value;
  }


  public static ApplicableTemporal of(Temporal value) {

    return new ApplicableTemporal(value);
  }

  @Override
  public String toString() {
    return String.valueOf(value);

  }
}
