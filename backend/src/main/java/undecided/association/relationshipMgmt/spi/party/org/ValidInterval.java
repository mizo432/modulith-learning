package undecided.association.relationshipMgmt.spi.party.org;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import org.jspecify.annotations.Nullable;

/**
 * 有効期間を表す値オブジェクトクラス。
 *
 * <p>このクラスは、組織の有効期間（開始日から終了日までの範囲）を管理します。
 *
 * <p>有効期間は開始日（startDate）と終了日（endDate）で構成され、終了日は開始日より後である必要があります。 終了日がnullの場合は、現在も有効であることを表します。
 *
 * <p>用途:
 *
 * <p>組織の契約期間、有効期間、活動期間などを表すために使用されます。
 */
@EqualsAndHashCode
public class ValidInterval {

  private final LocalDate startDate;
  private final LocalDate endDate;

  /** null の {@code LocalDate} を持つインスタンスを作成します。 */
  public ValidInterval() {
    startDate = null;
    endDate = null;
  }

  private ValidInterval(LocalDate startDate, @Nullable LocalDate endDate) {
    this.startDate = startDate;
    this.endDate = endDate;
  }

  /**
   * 開始日と終了日から有効期間を作成します。
   *
   * @param startDate 開始日。{@code null} は許容されません。
   * @param endDate 終了日。{@code null} の場合は現在も有効であることを表します。開始日より前であってはなりません。
   * @return 有効期間の値オブジェクト
   * @throws IllegalArgumentException 開始日が {@code null} の場合、または終了日が開始日より前の場合
   */
  public static ValidInterval of(LocalDate startDate, @Nullable LocalDate endDate) {
    if (startDate == null) {
      throw new IllegalArgumentException("startDate must not be null");
    }
    if (endDate != null && endDate.isBefore(startDate)) {
      throw new IllegalArgumentException("endDate must not be before startDate");
    }
    return new ValidInterval(startDate, endDate);
  }

  /**
   * 開始日のみから有効期間を作成します（終了日なし）。
   *
   * @param startDate 開始日。{@code null} は許容されません。
   * @return 終了日が {@code null} の有効期間の値オブジェクト
   * @throws IllegalArgumentException 開始日が {@code null} の場合
   */
  public static ValidInterval of(LocalDate startDate) {
    return of(startDate, null);
  }

  /**
   * 空の有効期間を表すインスタンスを返します。
   *
   * @return 空の {@link ValidInterval} インスタンス
   */
  public static ValidInterval empty() {
    return new ValidInterval();
  }

  /**
   * 開始日を返します。
   *
   * @return 開始日。{@code null} の可能性があります。
   */
  public LocalDate startDate() {
    return startDate;
  }

  /**
   * 終了日を返します。
   *
   * @return 終了日。{@code null} の場合は現在も有効であることを表します。
   */
  @Nullable
  public LocalDate endDate() {
    return endDate;
  }

  /**
   * 有効期間が空かどうかを判定します。
   *
   * @return 開始日が {@code null} の場合は {@code true} を返し、そうでない場合は {@code false} を返します。
   */
  public boolean isEmpty() {
    return startDate == null;
  }

  /**
   * 指定された日付が有効期間内かどうかを判定します。
   *
   * @param date 判定対象の日付
   * @return 指定された日付が有効期間内である場合は {@code true} を返し、そうでない場合は {@code false} を返します。 有効期間が空の場合は {@code
   *     false} を返します。
   */
  public boolean contains(@Nullable LocalDate date) {
    if (date == null || startDate == null) {
      return false;
    }
    if (date.isBefore(startDate)) {
      return false;
    }
    if (endDate != null && date.isAfter(endDate)) {
      return false;
    }
    return true;
  }

  /**
   * 現在も有効かどうかを判定します。
   *
   * @return 現在が有効期間内である場合は {@code true} を返し、そうでない場合は {@code false} を返します。 有効期間が空の場合は {@code false}
   *     を返します。
   */
  public boolean isActive() {
    return contains(LocalDate.now());
  }

  /**
   * 有効期間の文字列表現を返します。
   *
   * @return 有効期間の文字列（例: "2024-01-01 ~ 2024-12-31"）。有効期間が空の場合は {@code null} を返します。
   */
  @Override
  public String toString() {
    if (startDate == null) {
      return null;
    }
    if (endDate == null) {
      return startDate.toString() + " ~ ";
    }
    return startDate.toString() + " ~ " + endDate.toString();
  }

  /** ValidIntervalとデータベースの文字列を相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class ValidIntervalConverter implements AttributeConverter<ValidInterval, String> {

    /** 開始日と終了日の区切り文字。 */
    private static final String SEPARATOR = " ~ ";

    /**
     * エンティティ属性をデータベースカラムに変換します。
     *
     * @param attribute 変換対象の {@code ValidInterval} 属性
     * @return データベースに保存する {@code String}。属性が {@code null} または空の場合は {@code null}
     */
    @Override
    public String convertToDatabaseColumn(ValidInterval attribute) {
      if (attribute == null || attribute.isEmpty()) {
        return null;
      }
      if (attribute.endDate() == null) {
        return attribute.startDate().toString() + SEPARATOR;
      }
      return attribute.startDate().toString() + SEPARATOR + attribute.endDate().toString();
    }

    /**
     * データベースカラムをエンティティ属性に変換します。
     *
     * @param dbData データベースから読み込んだ {@code String}。{@code null} の可能性があります。
     * @return 変換された {@code ValidInterval} インスタンス。データベースデータが {@code null} の場合は空のインスタンス
     */
    @Override
    public ValidInterval convertToEntityAttribute(@Nullable String dbData) {
      if (dbData == null || dbData.isEmpty()) {
        return ValidInterval.empty();
      }
      String[] parts = dbData.split(SEPARATOR, 2);
      if (parts.length == 2) {
        LocalDate start = LocalDate.parse(parts[0]);
        LocalDate end = parts[1].isEmpty() ? null : LocalDate.parse(parts[1]);
        return new ValidInterval(start, end);
      } else if (parts.length == 1) {
        LocalDate start = LocalDate.parse(parts[0]);
        return new ValidInterval(start, null);
      }
      return ValidInterval.empty();
    }
  }
}
