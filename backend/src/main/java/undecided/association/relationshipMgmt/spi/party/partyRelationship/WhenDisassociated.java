package undecided.association.relationshipMgmt.spi.party.partyRelationship;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import undecided.shared.dateProvider.DateProvider;
import undecided.shared.value.DateTimeValue;

/**
 * Disassociation（関連付け解除）が行われた日時を表す値オブジェクトです。
 *
 * <p>このクラスは {@link DateTimeValue} インターフェースを実装し、パーティ間の関連付け解除が行われた 日時を不変の値として保持します。
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class WhenDisassociated implements DateTimeValue {

  private final LocalDateTime value;

  /** null の {@code LocalDateTime} を持つインスタンスを作成します。 */
  public WhenDisassociated() {
    this.value = null;
  }

  /**
   * 指定された {@code LocalDateTime} から {@code WhenDisassociated} インスタンスを作成します。
   *
   * @param dateTime 関連付け解除日時。{@code null} は許容されません。
   * @return 指定された日時を持つ {@code WhenDisassociated} インスタンス
   * @throws IllegalArgumentException {@code dateTime} が {@code null} の場合
   */
  public static WhenDisassociated of(LocalDateTime dateTime) {
    if (dateTime == null) {
      throw new IllegalArgumentException("dateTime must not be null");
    }
    return new WhenDisassociated(dateTime);
  }

  /**
   * 現在の日時を持つ {@code WhenDisassociated} インスタンスを作成します。
   *
   * @return 現在の日時を持つ {@code WhenDisassociated} インスタンス
   */
  public static WhenDisassociated current() {
    return new WhenDisassociated(DateProvider.currentLocalDateTime());
  }

  /**
   * 空の {@code WhenDisassociated} インスタンスを作成します。
   *
   * @return {@code null} の値を持つ {@code WhenDisassociated} インスタンス
   */
  public static WhenDisassociated empty() {
    return new WhenDisassociated();
  }

  /**
   * 保持している日時を返します。
   *
   * @return 関連付け解除日時。{@code null} の可能性があります。
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

  /** WhenDisassociatedとデータベースのLocalDateTimeを相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class WhenDisassociatedConverter
      implements AttributeConverter<WhenDisassociated, LocalDateTime> {

    @Override
    public LocalDateTime convertToDatabaseColumn(WhenDisassociated attribute) {
      return attribute != null ? attribute.value() : null;
    }

    @Override
    public WhenDisassociated convertToEntityAttribute(@Nullable LocalDateTime dbData) {
      if (dbData == null) {
        return WhenDisassociated.empty();
      }
      return new WhenDisassociated(dbData);
    }
  }
}
