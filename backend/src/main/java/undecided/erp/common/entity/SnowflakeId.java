package undecided.erp.common.entity;

import static undecided.erp.common.precondition.LongPrecondition.checkPositive;
import static undecided.erp.common.primitive.Objects2.isNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ComparisonChain;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.beans.Transient;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import undecided.erp.common.snowflake.SnowflakeIdProvider;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode
public class SnowflakeId implements LongValue<SnowflakeId>,
    Comparable<SnowflakeId> {

  public static final SnowflakeId EMPTY = new SnowflakeId(null);
  @JsonValue
  private final Long value;

  /**
   * SnowflakeIdクラスの新しいインスタンスを作成します。
   *
   * @return SnowflakeIdクラスの新しいインスタンス
   */
  public static SnowflakeId newInstance() {
    return new SnowflakeId(SnowflakeIdProvider.generateId());
  }

  @JsonCreator
  public static SnowflakeId of(@NonNull Long value) {
    checkPositive(value, () -> new IllegalArgumentException("value must be positive"));
    return new SnowflakeId(value);
  }

  /**
   * 提供された値に基づいてSnowflakeIdオブジェクトを再構築します。
   *
   * @param value SnowflakeIdオブジェクトを再構築するために使用する値。
   * @return 再構築されたSnowflakeIdオブジェクト、または値がnullの場合はSnowflakeId.EMPTY。
   */
  public static SnowflakeId reconstruct(Long value) {
    if (isNull(value)) {
      return EMPTY;
    }
    return new SnowflakeId(value);
  }

  public static SnowflakeId empty() {
    return EMPTY;
  }

  /**
   * SnowflakeIdオブジェクトの文字列表現を返します。
   *
   * @return SnowflakeIdオブジェクトの文字列表現。
   */
  @Override
  public String toString() {
    return String.valueOf(value);

  }

  @Transient
  @Override
  public boolean isEmpty() {
    return isNull(value);
  }

  /**
   * この SnowflakeId インスタンスを他のインスタンスと比較して順序を決定します。
   *
   * @param other 比較対象の SnowflakeId インスタンス。null の可能性があります。
   * @return この SnowflakeId が指定された SnowflakeId より小さい場合は負の整数、 等しい場合は 0、大きい場合は正の整数を返します。 指定されたオブジェクトが
   * null の場合は -1 を返します。
   */
  @Override
  public int compareTo(@Nullable SnowflakeId other) {
    if (isNull(other)) {
      return -1;
    }
    if (isNull(this.value)) {
      return 1;
    }
    return ComparisonChain
        .start()
        .compare(this.getValue(), other.getValue())
        .result();

  }

  /**
   * このインスタンスに格納されている値をBase-36エンコードされた文字列に変換します。
   * <p>
   * このメソッドは、変換を実行する前に値が空でないことを検証します。
   *
   * @return 現在のインスタンスの値をBase-36エンコードした文字列。
   * @throws IllegalStateException 値がnullまたは空の場合にスローされます。
   */
  public String toBase36String() {
    LongValues.checkNotEmpty(this, () -> new IllegalStateException("value is empty"));
    return Long.toString(getValue(), 36);
  }

  /**
   * SnowflakeIdクラスとデータベースで格納されるLong型の値を相互に変換するためのコンバータークラスです。
   * このクラスはJPAのAttributeConverterインターフェースを実装しています。
   */
  @Converter
  public static class SnowflakeIdConverter implements
      AttributeConverter<SnowflakeId, Long> {

    /**
     * SnowflakeIdオブジェクトをデータベースに保存可能なLong型に変換します。
     *
     * @param attribute 変換対象のSnowflakeIdオブジェクト。この値がnullの場合、nullを返します。
     * @return 変換されたLong型の値。attributeがnullの場合はnullを返します。
     */
    @Override
    public Long convertToDatabaseColumn(SnowflakeId attribute) {
      return attribute.getValue();

    }

    /**
     * データベースから取得したLong型の値をSnowflakeIdエンティティに変換します。
     *
     * @param dbData データベースから取得したLong型の値
     * @return 変換されたSnowflakeIdオブジェクト。dbDataがnullの場合はSnowflakeId.EMPTYを返します。
     */
    @Override
    public SnowflakeId convertToEntityAttribute(Long dbData) {
      return SnowflakeId.reconstruct(dbData);
    }
  }
}
