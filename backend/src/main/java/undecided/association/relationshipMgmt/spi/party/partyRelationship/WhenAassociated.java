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
 * A（関連付け）が行われた日時を表す値オブジェクトです。
 *
 * <p>このクラスは {@link DateTimeValue} インターフェースを実装し、パーティ間の関連付けが行われた 日時を不変の値として保持します。
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class WhenAassociated implements DateTimeValue {

  private final LocalDateTime value;

  /** null の {@code LocalDateTime} を持つインスタンスを作成します。 */
  public WhenAassociated() {
    this.value = null;
  }

  /**
   * 指定された {@code LocalDateTime} から {@code WhenAassociated} インスタンスを作成します。
   *
   * @param dateTime 関連付け日時。{@code null} は許容されません。
   * @return 指定された日時を持つ {@code WhenAassociated} インスタンス
   * @throws IllegalArgumentException {@code dateTime} が {@code null} の場合
   */
  public static WhenAassociated of(LocalDateTime dateTime) {
    if (dateTime == null) {
      throw new IllegalArgumentException("dateTime must not be null");
    }
    return new WhenAassociated(dateTime);
  }

  /**
   * 現在の日時を持つ {@code WhenAassociated} インスタンスを作成します。
   *
   * @return 現在の日時を持つ {@code WhenAassociated} インスタンス
   */
  public static WhenAassociated current() {
    return new WhenAassociated(DateProvider.currentLocalDateTime());
  }

  /**
   * 空の {@code WhenAassociated} インスタンスを作成します。
   *
   * @return {@code null} の値を持つ {@code WhenAassociated} インスタンス
   */
  public static WhenAassociated empty() {
    return new WhenAassociated();
  }

  /**
   * 保持している日時を返します。
   *
   * @return 関連付け日時。{@code null} の可能性があります。
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

  /** WhenAassociatedとデータベースのLocalDateTimeを相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class WhenAassociatedConverter
      implements AttributeConverter<WhenAassociated, LocalDateTime> {

    @Override
    public LocalDateTime convertToDatabaseColumn(WhenAassociated attribute) {
      return attribute != null ? attribute.value() : null;
    }

    @Override
    public WhenAassociated convertToEntityAttribute(@Nullable LocalDateTime dbData) {
      if (dbData == null) {
        return WhenAassociated.empty();
      }
      return new WhenAassociated(dbData);
    }
  }
}
