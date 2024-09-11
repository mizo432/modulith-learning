package undecided.erp.shared.entity;

import static undecided.erp.common.primitive.Objects2.isNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ComparisonChain;
import java.beans.Transient;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import undecided.erp.common.snowflake.SnowflakeIdProvider;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode
public class SnowflakeId implements LongValue, Comparable<SnowflakeId> {

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

  @Override
  public int compareTo(SnowflakeId other) {
    if (isNull(other)) {
      return -1;
    }
    return ComparisonChain
        .start()
        .compare(this.value, other.getValue())
        .result();

  }
}
