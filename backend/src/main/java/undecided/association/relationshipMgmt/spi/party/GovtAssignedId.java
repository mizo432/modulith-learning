package undecided.association.relationshipMgmt.spi.party;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.EqualsAndHashCode;
import org.jspecify.annotations.Nullable;
import undecided.shared.primitive.Strings2;
import undecided.shared.value.StringCode;

/**
 * 政府発行の識別子を表す値オブジェクトクラス。
 *
 * <p>このクラスは、政府によって発行される個人や法人の識別子（例：マイナンバー、法人番号など）を管理します。
 *
 * <p>政府発行IDは文字列として格納され、行政手続きや身分証明において対象者を特定するために使用されます。 政府発行IDは半角数字のみで構成され、桁数は1桁以上50桁以下である必要があります。
 *
 * <p>用途:
 *
 * <p>個人や法人を政府レベルで一意に識別し、行政手続きや身分証明において正確に指定するために使用されます。
 */
@EqualsAndHashCode
public class GovtAssignedId implements StringCode<GovtAssignedId> {

  /** 政府発行IDの最小桁数。 */
  public static final int MIN_LENGTH = 1;

  /** 政府発行IDの最大桁数。 */
  public static final int MAX_LENGTH = 50;

  private final String value;

  protected GovtAssignedId() {
    value = null;
  }

  private GovtAssignedId(String value) {
    this.value = value;
  }

  /**
   * 文字列から政府発行IDを作成します。
   *
   * @param govtAssignedId 政府発行ID（半角数字のみ、1桁以上50桁以下）
   * @return 政府発行IDの値オブジェクト
   * @throws IllegalArgumentException 政府発行IDがnull、空、桁数制限を超えている、または半角数字のみで構成されていない場合
   */
  public static GovtAssignedId of(String govtAssignedId) {
    if (govtAssignedId == null) {
      throw new IllegalArgumentException("govtAssignedId must not be null");
    }
    String trimmed = govtAssignedId.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException("govtAssignedId must not be empty");
    }
    if (trimmed.length() < MIN_LENGTH || trimmed.length() > MAX_LENGTH) {
      throw new IllegalArgumentException(
          "govtAssignedId must be between "
              + MIN_LENGTH
              + " and "
              + MAX_LENGTH
              + " digits, but was: "
              + trimmed);
    }
    if (!trimmed.matches("\\d+")) {
      throw new IllegalArgumentException(
          "govtAssignedId must contain only digits, but was: " + trimmed);
    }
    return new GovtAssignedId(trimmed);
  }

  /**
   * 空の政府発行IDを表すインスタンスを返します。
   *
   * @return 空の {@link GovtAssignedId} インスタンス
   */
  public static GovtAssignedId empty() {
    return new GovtAssignedId();
  }

  /**
   * 政府発行IDの値を返します。
   *
   * @return 政府発行IDの文字列
   */
  @Override
  public String value() {
    return value;
  }

  /**
   * 政府発行IDが空かどうかを判定します。
   *
   * @return 政府発行IDが空である場合は {@code true} を返し、そうでない場合は {@code false} を返します。
   */
  @Override
  public boolean isEmpty() {
    return Strings2.isEmpty(value);
  }

  /**
   * 政府発行IDの文字列表現を返します。
   *
   * @return 政府発行IDの文字列
   */
  @Override
  public String toString() {
    return Strings2.toString(value);
  }

  /** GovtAssignedIdとデータベースの文字列を相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class GovtAssignedIdConverter
      implements AttributeConverter<GovtAssignedId, String> {

    @Override
    public String convertToDatabaseColumn(GovtAssignedId attribute) {
      return attribute != null ? attribute.value() : null;
    }

    @Override
    public GovtAssignedId convertToEntityAttribute(@Nullable String dbData) {
      if (dbData == null) {
        return GovtAssignedId.empty();
      }
      return new GovtAssignedId(dbData);
    }
  }
}
