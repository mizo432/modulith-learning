package undecided.erp.shared.date;

import java.time.Year;
import lombok.Getter;
import lombok.NonNull;

/**
 * 特定の範囲内で適用可能な年を表します。
 */
@Getter
public class ApplicableYear {

  public static final Year MAX_YEAR = Year.of(9999);

  public static final ApplicableYear MAX = new ApplicableYear(MAX_YEAR);

  private final Year value;

  private ApplicableYear(Year value) {
    this.value = value;
  }


  /**
   * 指定された{@link Year}値とともに{@link ApplicableYear}インスタンスを構築します。
   *
   * @param value 新しい{@link ApplicableYear}インスタンスのための年の値。nullであってはならない
   * @return 指定された値を持つ{@link ApplicableYear}インスタンス
   * @throws IllegalArgumentException 値が許可される最大年より後の場合
   */
  public static ApplicableYear of(@NonNull Year value) {
    if (value.isAfter(MAX_YEAR)) {
      throw new IllegalArgumentException("Value cannot be after MAX_YEAR");
    }

    return new ApplicableYear(value);
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

}
