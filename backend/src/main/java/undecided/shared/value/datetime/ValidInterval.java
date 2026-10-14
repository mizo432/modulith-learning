package undecided.shared.value.datetime;

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
 * <p>有効期間は有効開始日（validFrom）と有効終了日（validTo）で構成され、有効終了日は有効開始日より後である必要があります。
 * 有効終了日がnullの場合は、現在も有効であることを表します。
 *
 * <p>用途:
 *
 * <p>組織の契約期間、有効期間、活動期間などを表すために使用されます。
 */
@EqualsAndHashCode
public class ValidInterval {

  private final LocalDate validFrom;
  private final LocalDate validTo;

  /** null の {@code LocalDate} を持つインスタンスを作成します。 */
  public ValidInterval() {
    validFrom = null;
    validTo = null;
  }

  private ValidInterval(LocalDate validFrom, @Nullable LocalDate validTo) {
    this.validFrom = validFrom;
    this.validTo = validTo;
  }

  /**
   * 有効開始日と有効終了日から有効期間を作成します。
   *
   * @param validFrom 有効開始日。{@code null} は許容されません。
   * @param validTo 有効終了日。{@code null} の場合は現在も有効であることを表します。有効開始日より前であってはなりません。
   * @return 有効期間の値オブジェクト
   * @throws IllegalArgumentException 有効開始日が {@code null} の場合、または有効終了日が有効開始日より前の場合
   */
  public static ValidInterval of(LocalDate validFrom, @Nullable LocalDate validTo) {
    if (validFrom == null) {
      throw new IllegalArgumentException("validFrom must not be null");
    }
    if (validTo != null && validTo.isBefore(validFrom)) {
      throw new IllegalArgumentException("validTo must not be before validFrom");
    }
    return new ValidInterval(validFrom, validTo);
  }

  /**
   * 有効開始日のみから有効期間を作成します（有効終了日なし）。
   *
   * @param validFrom 有効開始日。{@code null} は許容されません。
   * @return 有効終了日が {@code null} の有効期間の値オブジェクト
   * @throws IllegalArgumentException 有効開始日が {@code null} の場合
   */
  public static ValidInterval of(LocalDate validFrom) {
    return of(validFrom, null);
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
   * 有効開始日を返します。
   *
   * @return 有効開始日。{@code null} の可能性があります。
   */
  public LocalDate validFrom() {
    return validFrom;
  }

  /**
   * 有効終了日を返します。
   *
   * @return 有効終了日。{@code null} の場合は現在も有効であることを表します。
   */
  @Nullable
  public LocalDate validTo() {
    return validTo;
  }

  /**
   * 有効期間が空かどうかを判定します。
   *
   * @return 有効開始日が {@code null} の場合は {@code true} を返し、そうでない場合は {@code false} を返します。
   */
  public boolean isEmpty() {
    return validFrom == null;
  }

  /**
   * 指定された日付が有効期間内かどうかを判定します。
   *
   * @param date 判定対象の日付
   * @return 指定された日付が有効期間内である場合は {@code true} を返し、そうでない場合は {@code false} を返します。 有効期間が空の場合は {@code
   *     false} を返します。
   */
  public boolean contains(@Nullable LocalDate date) {
    if (date == null || validFrom == null) {
      return false;
    }
    if (date.isBefore(validFrom)) {
      return false;
    }
    if (validTo != null && date.isAfter(validTo)) {
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
    if (validFrom == null) {
      return null;
    }
    if (validTo == null) {
      return validFrom.toString() + " ~ ";
    }
    return validFrom.toString() + " ~ " + validTo.toString();
  }

  /** ValidIntervalとデータベースの文字列を相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class ValidIntervalConverter implements AttributeConverter<ValidInterval, String> {

    /** 有効開始日と有効終了日の区切り文字。 */
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
      if (attribute.validTo() == null) {
        return attribute.validFrom().toString() + SEPARATOR;
      }
      return attribute.validFrom().toString() + SEPARATOR + attribute.validTo().toString();
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
