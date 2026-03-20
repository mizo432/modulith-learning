package undecided.erp.common.entity;

import static undecided.shared.common.precondition.LongPrecondition.checkPositive;
import static undecided.shared.common.primitiveOld.Objects2.isNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.collect.ComparisonChain;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
import java.beans.Transient;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import undecided.shared.common.snowflake.SnowflakeIdProvider;

/**
 * SnowflakeIdクラスは、Snowflakeアルゴリズムによって生成される一意の長整数IDを表すクラスです。
 * IDをラップし、文字列表現や比較、データベースの永続化などのユーティリティメソッドを提供します。 このクラスは不変オブジェクトとして設計されています。
 *
 * <p>序列化・デシリアライズの過程でJSON形式で表現される際に利用されます。
 */
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Convert(converter = SnowflakeId.SnowflakeIdConverter.class)
public class SnowflakeId implements LongValue<SnowflakeId>, Comparable<SnowflakeId>, Serializable {
  /**
   * 空のSnowflakeIdを表す定数。 この定数は、値を持たないSnowflakeIdオブジェクトを表現するために使用されます。 主に、未設定や初期状態を明示的に示すために利用されます。
   *
   * <p>例外やエッジケースを処理する際に役立ち、空の状態であることを確認したり 比較や操作を実行する際の基準として使用されます。
   */
  public static final SnowflakeId EMPTY = new SnowflakeId(null);

  /**
   * クラスSnowflakeIdにおいて、シリアライズのバージョン管理を行うために使用される直列化識別子。
   * serialVersionUIDは、異なるJava仮想マシン間でのインスタンスの保存および読み込み（シリアライズおよびデシリアライズ）
   * プロセス中に、クラスの互換性を検証するために使用されます。 クラスの構造に大きな変更がない限り、独自に定義された値を保持することで、 シリアライズ済みオブジェクトの互換性を維持ができます。
   */
  @Serial private static final long serialVersionUID = 1L;

  private final Long value;

  /**
   * SnowflakeIdクラスの新しいインスタンスを作成します。
   *
   * @return SnowflakeIdクラスの新しいインスタンス
   */
  public static SnowflakeId newInstance() {
    return new SnowflakeId(SnowflakeIdProvider.generateId());
  }

  /**
   * 指定された正の値からSnowflakeIdオブジェクトを生成します。
   *
   * @param value SnowflakeIdオブジェクトを生成するための正の値。nullではない必要があります。
   * @return 指定された値を持つSnowflakeIdオブジェクト。
   * @throws IllegalArgumentException valueが正の値でない場合にスローされます。
   */
  @JsonCreator
  public static SnowflakeId of(@NonNull Long value) {
    if (isNull(value)) {
      throw new NullPointerException("value must not be null");
    }
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

  /**
   * 空のSnowflakeIdインスタンスを返します。
   *
   * @return 空のSnowflakeIdインスタンス。
   */
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

  /**
   * 現在のSnowflakeIdオブジェクトが空であるかどうかを判定します。
   *
   * @return オブジェクトが空の場合は{@code true}、そうでない場合は{@code false}
   */
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
   *     null の場合は -1 を返します。
   */
  @Override
  public int compareTo(@Nullable SnowflakeId other) {
    if (isNull(other)) {
      return -1;
    }
    if (isNull(this.value)) {
      return 1;
    }
    return ComparisonChain.start().compare(this.value(), other.value()).result();
  }

  /**
   * このインスタンスに格納されている値をBase-36エンコードされた文字列に変換します。
   *
   * <p>このメソッドは、変換を実行する前に値が空でないことを検証します。
   *
   * @return 現在のインスタンスの値をBase-36エンコードした文字列。
   * @throws IllegalStateException 値がnullまたは空の場合にスローされます。
   */
  public String toBase36String() {
    LongValues.checkNotEmpty(this, () -> new IllegalStateException("value is empty"));
    return Long.toString(value(), 36);
  }

  @Override
  public Long value() {
    return value;
  }

  /**
   * SnowflakeIdクラスとデータベースで格納されるLong型の値を相互に変換するためのコンバータークラスです。
   * このクラスはJPAのAttributeConverterインターフェースを実装しています。
   */
  @Converter(autoApply = true)
  public static class SnowflakeIdConverter implements AttributeConverter<SnowflakeId, Long> {

    /**
     * SnowflakeIdオブジェクトをデータベースに保存可能なLong型に変換します。
     *
     * @param attribute 変換対象のSnowflakeIdオブジェクト。この値がnullの場合、nullを返します。
     * @return 変換されたLong型の値。attributeがnullの場合はnullを返します。
     */
    @Override
    public Long convertToDatabaseColumn(SnowflakeId attribute) {
      return attribute != null ? attribute.value() : null;
    }

    /**
     * Convert a Long value retrieved from the database into a SnowflakeId entity.
     *
     * @param dbData the Long value read from the database; may be null
     * @return `SnowflakeId.EMPTY` if `dbData` is null, otherwise a `SnowflakeId` wrapping `dbData`
     */
    @Override
    public SnowflakeId convertToEntityAttribute(@org.jspecify.annotations.Nullable Long dbData) {
      return SnowflakeId.reconstruct(dbData);
    }
  }
}
