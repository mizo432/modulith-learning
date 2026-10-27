package undecided.association.relationshipMgmt.spi.party.person;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import undecided.shared.sensitive.SensitiveDataMasker;
import undecided.shared.value.DateValue;

/**
 * 生年月日を表す値オブジェクトです。
 *
 * <p>このクラスは {@link DateValue} インターフェースを実装し、個人の生年月日を不変の値として保持します。 生年月日は過去の日付である必要があります。
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class DateOfBirth implements DateValue {

  private final LocalDate value;

  /** null の {@code LocalDate} を持つインスタンスを作成します。 */
  public DateOfBirth() {
    value = null;
  }

  /**
   * 指定された日付から {@code DateOfBirth} インスタンスを作成します。
   *
   * @param dateOfBirth 生年月日。{@code null} は許容されません。
   * @return 生年月日の {@code DateOfBirth} インスタンス
   * @throws IllegalArgumentException 生年月日が {@code null} の場合
   */
  public static DateOfBirth of(LocalDate dateOfBirth) {
    if (dateOfBirth == null) {
      throw new IllegalArgumentException("dateOfBirth must not be null");
    }
    return new DateOfBirth(dateOfBirth);
  }

  /**
   * 空の {@code DateOfBirth} インスタンスを作成します。
   *
   * @return {@code null} の値を持つ {@code DateOfBirth} インスタンス
   */
  public static DateOfBirth empty() {
    return new DateOfBirth();
  }

  /**
   * 保持している日付を返します。
   *
   * @return 生年月日。{@code null} の可能性があります。
   */
  @Override
  public LocalDate value() {
    return value;
  }

  /**
   * 日付を文字列として返します。
   *
   * @return 日付の文字列表現。{@code null} の場合は {@code "null"} 文字列を返します。
   */
  @Override
  public String toString() {
    return SensitiveDataMasker.maskDateOfBirth(value);
  }

  /** DateOfBirthとデータベースのLocalDateを相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class DateOfBirthConverter implements AttributeConverter<DateOfBirth, LocalDate> {

    /**
     * エンティティ属性をデータベースカラムに変換します。
     *
     * @param attribute 変換対象の {@code DateOfBirth} 属性
     * @return データベースに保存する {@code LocalDate}。属性が {@code null} の場合は {@code null}
     */
    @Override
    public LocalDate convertToDatabaseColumn(DateOfBirth attribute) {
      return attribute != null ? attribute.value() : null;
    }

    /**
     * データベースカラムをエンティティ属性に変換します。
     *
     * @param dbData データベースから読み込んだ {@code LocalDate}。{@code null} の可能性があります。
     * @return 変換された {@code DateOfBirth} インスタンス。データベースデータが {@code null} の場合は空のインスタンス
     */
    @Override
    public DateOfBirth convertToEntityAttribute(@Nullable LocalDate dbData) {
      if (dbData == null) {
        return DateOfBirth.empty();
      }
      return new DateOfBirth(dbData);
    }
  }
}
