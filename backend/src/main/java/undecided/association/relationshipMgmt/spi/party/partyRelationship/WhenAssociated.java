package undecided.association.relationshipMgmt.spi.party.partyRelationship;

import com.github.mizo432.shared.common.dateProvider.DateProvider;
import com.github.mizo432.shared.common.exception.BusinessException;
import com.github.mizo432.shared.common.value.DateTimeValue;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

/**
 * A（関連付け）が行われた日時を表す値オブジェクトです。
 *
 * <p>このクラスは {@link DateTimeValue} インターフェースを実装し、パーティ間の関連付けが行われた 日時を不変の値として保持します。
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class WhenAssociated implements DateTimeValue {

  private final LocalDateTime value;

  /** null の {@code LocalDateTime} を持つインスタンスを作成します。 */
  public WhenAssociated() {
    this.value = null;
  }

  /**
   * 指定された {@code LocalDateTime} から {@code WhenAssociated} インスタンスを作成します。
   *
   * @param dateTime 関連付け日時。{@code null} は許容されません。
   * @return 指定された日時を持つ {@code WhenAssociated} インスタンス
   * @throws BusinessException {@code dateTime} が {@code null} の場合
   */
  public static WhenAssociated of(LocalDateTime dateTime) {
    if (dateTime == null) {
      throw new BusinessException("dateTime must not be null");
    }
    return new WhenAssociated(dateTime);
  }

  /**
   * 現在の日時を持つ {@code WhenAssociated} インスタンスを作成します。
   *
   * @return 現在の日時を持つ {@code WhenAssociated} インスタンス
   */
  public static WhenAssociated current() {
    return new WhenAssociated(DateProvider.currentLocalDateTime());
  }

  /**
   * 空の {@code WhenAssociated} インスタンスを作成します。
   *
   * @return {@code null} の値を持つ {@code WhenAssociated} インスタンス
   */
  public static WhenAssociated empty() {
    return new WhenAssociated();
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

  /** WhenAssociatedとデータベースのLocalDateTimeを相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class WhenAssociatedConverter
      implements AttributeConverter<WhenAssociated, LocalDateTime> {

    @Override
    public LocalDateTime convertToDatabaseColumn(WhenAssociated attribute) {
      return attribute != null ? attribute.value() : null;
    }

    @Override
    public WhenAssociated convertToEntityAttribute(@Nullable LocalDateTime dbData) {
      if (dbData == null) {
        return WhenAssociated.empty();
      }
      return new WhenAssociated(dbData);
    }
  }
}
