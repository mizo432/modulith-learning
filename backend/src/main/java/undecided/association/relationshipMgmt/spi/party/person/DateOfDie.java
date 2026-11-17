package undecided.association.relationshipMgmt.spi.party.person;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import com.github.mizo432.shared.value.DateValue;

/**
 * 死亡日を表す値オブジェクトです。
 *
 * <p>このクラスは {@link DateValue} インターフェースを実装し、個人の死亡日を不変の値として保持します。
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class DateOfDie implements DateValue {

  private final LocalDate value;

  /** null の {@code LocalDate} を持つインスタンスを作成します。 */
  public DateOfDie() {
    value = null;
  }

  /**
   * 指定された日付から {@code DateOfDie} インスタンスを作成します。
   *
   * @param dateOfDie 死亡日。{@code null} は許容されません。
   * @return 死亡日の {@code DateOfDie} インスタンス
   * @throws IllegalArgumentException 死亡日が {@code null} の場合
   */
  public static DateOfDie of(LocalDate dateOfDie) {
    if (dateOfDie == null) {
      throw new IllegalArgumentException("dateOfDie must not be null");
    }
    return new DateOfDie(dateOfDie);
  }

  /**
   * 空の {@code DateOfDie} インスタンスを作成します。
   *
   * @return {@code null} の値を持つ {@code DateOfDie} インスタンス
   */
  public static DateOfDie empty() {
    return new DateOfDie();
  }

  /**
   * 保持している日付を返します。
   *
   * @return 死亡日。{@code null} の可能性があります。
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
    return value != null ? value.toString() : "null";
  }

  /** DateOfDieとデータベースのLocalDateを相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class DateOfDieConverter implements AttributeConverter<DateOfDie, LocalDate> {

    /**
     * エンティティ属性をデータベースカラムに変換します。
     *
     * @param attribute 変換対象の {@code DateOfDie} 属性
     * @return データベースに保存する {@code LocalDate}。属性が {@code null} の場合は {@code null}
     */
    @Override
    public LocalDate convertToDatabaseColumn(DateOfDie attribute) {
      return attribute != null ? attribute.value() : null;
    }

    /**
     * データベースカラムをエンティティ属性に変換します。
     *
     * @param dbData データベースから読み込んだ {@code LocalDate}。{@code null} の可能性があります。
     * @return 変換された {@code DateOfDie} インスタンス。データベースデータが {@code null} の場合は空のインスタンス
     */
    @Override
    public DateOfDie convertToEntityAttribute(@Nullable LocalDate dbData) {
      if (dbData == null) {
        return DateOfDie.empty();
      }
      return new DateOfDie(dbData);
    }
  }
}
