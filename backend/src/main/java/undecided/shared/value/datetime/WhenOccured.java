package undecided.shared.value.datetime;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import com.github.mizo432.shared.common.dateProvider.DateProvider;
import undecided.shared.value.DateTimeValue;

/**
 * 何らかのイベントが発生した日時を表す値オブジェクトです。
 *
 * <p>このクラスは {@link DateTimeValue} インターフェースを実装し、イベントが発生した 日時を不変の値として保持します。
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class WhenOccured implements DateTimeValue {
  private final LocalDateTime value;

  /** null の {@code LocalDateTime} を持つインスタンスを作成します。 */
  public WhenOccured() {
    value = null;
  }

  /**
   * 現在の日時を持つ {@code WhenOccured} インスタンスを作成します。
   *
   * @return 現在の日時を持つ {@code WhenOccured} インスタンス
   */
  public static WhenOccured current() {
    return new WhenOccured(DateProvider.currentLocalDateTime());
  }

  /**
   * 空の {@code WhenOccured} インスタンスを作成します。
   *
   * @return {@code null} の値を持つ {@code WhenOccured} インスタンス
   */
  public static WhenOccured empty() {
    return new WhenOccured();
  }

  /**
   * 保持している日時を返します。
   *
   * @return 発生日時。{@code null} の可能性があります。
   */
  @Override
  public LocalDateTime value() {
    return value;
  }

  /**
   * 日時を文字列として返します。
   *
   * @return 日時の文字列表現。{@code null} の場合は {@code "null"} 文字列を返します。
   */
  @Override
  public String toString() {
    return String.valueOf(value);
  }

  /** WhenOccuredとデータベースのLocalDateTimeを相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class WhenOccuredConverter
      implements AttributeConverter<WhenOccured, LocalDateTime> {

    /**
     * エンティティ属性をデータベースカラムに変換します。
     *
     * @param attribute 変換対象の {@code WhenOccured} 属性
     * @return データベースに保存する {@code LocalDateTime}。属性が {@code null} の場合は {@code null}
     */
    @Override
    public LocalDateTime convertToDatabaseColumn(WhenOccured attribute) {
      return attribute != null ? attribute.value() : null;
    }

    /**
     * データベースカラムをエンティティ属性に変換します。
     *
     * @param dbData データベースから読み込んだ {@code LocalDateTime}。{@code null} の可能性があります。
     * @return 変換された {@code WhenOccured} インスタンス。データベースデータが {@code null} の場合は空のインスタンス
     */
    @Override
    public WhenOccured convertToEntityAttribute(@Nullable LocalDateTime dbData) {
      if (dbData == null) {
        return WhenOccured.empty();
      }
      return new WhenOccured(dbData);
    }
  }
}
